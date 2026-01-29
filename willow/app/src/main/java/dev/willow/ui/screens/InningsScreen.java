package dev.willow.ui.screens;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import dev.willow.engine.GameConfig;
import dev.willow.engine.InningsState;
import dev.willow.engine.MatchResult;
import dev.willow.engine.TossDecision;
import dev.willow.engine.gameplay.GameContext;
import dev.willow.engine.gameplay.GamePlayer;
import dev.willow.engine.gameplay.PlayerFactory;
import dev.willow.engine.gameplay.PlayerWillow;
import dev.willow.engine.rules.InningsRules;
import dev.willow.ui.Screen;
import dev.willow.ui.ScreenManager;

// import java.util.Random;

public class InningsScreen implements Screen{
    private enum Phase {FIRST, SECOND, PAUSED}

    private final ScreenManager manager;
    private final GameConfig config;

    private Phase phase=Phase.FIRST;
    private Phase resumePhase;

    private final InningsState state=new InningsState();
    private final PlayerWillow willow=PlayerFactory.create(GamePlayer.WILSON); // JUSTIN, WILSON, CLARK
    // private final PlayerWillow willow=new NormalMode();
    // private final Random random=new Random();

    private boolean playerBatting;
    private boolean playerChasing;

    private int playerScore=0;
    private int opponentScore=0;

    private Integer lastBattingMove;
    private Integer lastBowlingMove;
    private String lastResult;

    private int target;
    private MatchResult pendingResult;

    public InningsScreen(ScreenManager manager, GameConfig config, TossDecision decision, boolean playerWonToss) {
        this.manager=manager;
        this.config=config;
        if(playerWonToss){playerBatting=(decision==TossDecision.BAT);} 
        else{playerBatting=(decision==TossDecision.BOWL);}
    }

    @Override
    public void render(){
        TextGraphics tg=manager.terminal().newTextGraphics();
        tg.putString(5, 2, phase==Phase.FIRST?"FIRST INNINGS":phase==Phase.SECOND?"SECOND INNINGS":"INNINGS");
        tg.putString(5, 3, playerBatting?"you are BATTING":"opponent is BATTING");
        if(phase==Phase.SECOND){tg.putString(5, 4, "target : "+target);}
        if (lastBattingMove!=null){
            tg.putString(5, 6, "batting move : "+lastBattingMove);
            tg.putString(5, 7, "bowling move : "+lastBowlingMove);
            tg.putString(5, 9, "result       : "+lastResult);
            tg.putString(5,10, "--------------------------------");
        }
        tg.putString(5, 12, "runs    : "+state.getRuns());
        tg.putString(5, 13, "balls   : "+state.getBalls());
        tg.putString(5, 14, "wickets : "+state.getWickets());
        tg.putString(5, 16, phase==Phase.PAUSED? "press ENTER to continue":"play your move [0-6]");
    }

    @Override
    public void handleInput(KeyStroke key){
        if(phase==Phase.PAUSED && key.getKeyType()==KeyType.Enter){
            if(resumePhase==Phase.SECOND){
                startSecondInnings();
                return;
            }
            if(pendingResult!=null){
                manager.show(new ResultScreen(manager, pendingResult, config, playerScore, opponentScore));
            }
            return;
        }
        if(phase==Phase.PAUSED){return;}
        if(key.getKeyType()!=KeyType.Character){return;}
        
        char c=key.getCharacter();
        if(c<'0' || c>'6'){return;}

        int playerInput=c-'0';
        int ballsLeft=config.getOversPerInnings().isPresent()?config.getOversPerInnings().getAsInt()*6-state.getBalls():Integer.MAX_VALUE;
        int wicketsLeft=config.getWicketsPerInnings().isPresent()?config.getWicketsPerInnings().getAsInt()-state.getWickets():Integer.MAX_VALUE;
        int effectiveTarget=(phase==Phase.SECOND)?target:-1;
        GameContext context=new GameContext(effectiveTarget, !playerBatting, ballsLeft, wicketsLeft);

        int opponentInput=willow.nextMove(!playerBatting, state, context);
        int battingMove=playerBatting?playerInput:opponentInput;
        int bowlingMove=playerBatting?opponentInput:playerInput;
        lastBattingMove=battingMove;
        lastBowlingMove=bowlingMove;

        if(battingMove==bowlingMove){state.wicket(); lastResult="OUT!";}
        else{
            int runs=battingMove==0?bowlingMove:battingMove;
            state.addRuns(runs);
            lastResult="+"+runs+" runs";
        }
        if(phase==Phase.SECOND && state.getRuns()>=target){
            assignSecondInningsScore();
            pendingResult=playerChasing?MatchResult.WIN:MatchResult.LOSE;
            phase=Phase.PAUSED;
            return;
        }
        if(InningsRules.isOver(state, config)){
            if(phase==Phase.FIRST){
                assignFirstInningsScore();
                target=state.getRuns()+1;
                resumePhase=Phase.SECOND;
                phase=Phase.PAUSED;
                return;
            }
            assignSecondInningsScore();
            pendingResult=state.getRuns()==target-1?MatchResult.TIE:(playerChasing?MatchResult.LOSE:MatchResult.WIN);
            phase=Phase.PAUSED;
        }
    }

    private void assignFirstInningsScore(){
        if(playerBatting){playerScore=state.getRuns();}
        else{opponentScore=state.getRuns();}
    }

    private void assignSecondInningsScore() {
        if(playerBatting){playerScore=state.getRuns();}
        else{opponentScore=state.getRuns();}
    }

    private void startSecondInnings() {
        state.reset();
        lastBattingMove=null;
        lastBowlingMove=null;
        lastResult=null;
        playerBatting=!playerBatting;
        playerChasing=playerBatting;
        phase=Phase.SECOND;
        resumePhase=null;
    }
}
