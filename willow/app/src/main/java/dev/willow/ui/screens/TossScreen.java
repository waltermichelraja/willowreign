package dev.willow.ui.screens;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import dev.willow.engine.GameConfig;
import dev.willow.engine.TossDecision;
import dev.willow.ui.Screen;
import dev.willow.ui.ScreenManager;
import dev.willow.ui.widgets.MenuWidget;

import java.util.List;
import java.util.Random;

public class TossScreen implements Screen{
    private enum State{
        WAITING,
        PLAYER_CHOICE,
        DONE
    }
    private final ScreenManager manager;
    private final GameConfig config;
    private final Random random=new Random();
    private State state=State.WAITING;
    private Boolean playerWonToss=null;
    private TossDecision decision=null;
    private final MenuWidget choiceMenu=new MenuWidget(List.of("BAT", "BOWL"));

    public TossScreen(ScreenManager manager, GameConfig config){
        this.manager=manager;
        this.config=config;
    }

    @Override
    public void render(){
        TextGraphics tg=manager.terminal().newTextGraphics();
        tg.putString(5, 2, "TOSS TIME");
        if(state==State.WAITING){tg.putString(5, 4, "press ENTER to toss the coin");}
        if(state==State.PLAYER_CHOICE){
            tg.putString(5, 4, "you won the toss!");
            tg.putString(5, 6, "choose:");
            choiceMenu.render(tg, 5, 8);
        }
        if(state==State.DONE){
            tg.putString(5, 4, playerWonToss?"you chose to " + decision.name():"opponent chose to " + decision.name());
            tg.putString(5, 6, "press ENTER to start innings");
        }
    }

    @Override
    public void handleInput(KeyStroke key){
        if(state==State.WAITING&&key.getKeyType()==KeyType.Enter){
            try{Thread.sleep(2000);}catch(InterruptedException e){Thread.currentThread().interrupt();}
            playerWonToss=random.nextBoolean();
            if(playerWonToss){
                state=State.PLAYER_CHOICE;
            }else{
                decision=random.nextBoolean()?TossDecision.BAT:TossDecision.BOWL;
                state=State.DONE;
            }
            return;
        }
        if(state==State.PLAYER_CHOICE){
            if(key.getKeyType()==KeyType.ArrowUp || key.getKeyType()==KeyType.ArrowDown){
                choiceMenu.handleInput(key);
                return;
            }
            if(choiceMenu.isSelected(key)){
                decision=choiceMenu.selectedIndex()==0?TossDecision.BAT:TossDecision.BOWL;
                state=State.DONE;
                return;
            }
        }
        if(state==State.DONE && key.getKeyType()==KeyType.Enter){manager.show(new InningsScreen(manager, config, decision, playerWonToss));}
    }
}
