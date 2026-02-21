package dev.willow.ui.screens;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;

import dev.willow.engine.GameConfig;
import dev.willow.engine.MatchResult;
import dev.willow.ui.Screen;
import dev.willow.ui.ScreenManager;

public class ResultScreen implements Screen{
    private final ScreenManager manager;
    private final MatchResult result;
    private final GameConfig config;
    private final int yourScore;
    private final int oppScore;

    public ResultScreen(ScreenManager manager, MatchResult result, GameConfig config, int yourScore, int oppScore){
        this.manager=manager;
        this.result=result;
        this.config=config;
        this.yourScore=yourScore;
        this.oppScore=oppScore;
    }

    @Override
    public void render(){
        TextGraphics tg=manager.terminal().newTextGraphics();
        tg.putString(5, 2, "MATCH RESULT");
        tg.enableModifiers(SGR.BOLD);
        switch(result){
            case WIN->tg.setForegroundColor(TextColor.ANSI.GREEN);
            case LOSE->tg.setForegroundColor(TextColor.ANSI.RED);
            case TIE->tg.setForegroundColor(TextColor.ANSI.YELLOW);
        }
        String headline=switch(result){
            case WIN->"YOU WON!";
            case LOSE->"YOU LOST!";
            case TIE->"IT'S A TIE!";
        };
        tg.putString(5, 4, headline);
        tg.disableModifiers(SGR.BOLD);
        tg.setForegroundColor(TextColor.ANSI.DEFAULT);

        tg.setForegroundColor(TextColor.ANSI.CYAN);
        tg.putString(5, 6, "your score     : "+yourScore);
        tg.setForegroundColor(TextColor.ANSI.MAGENTA);
        tg.putString(5, 7, "opponent score : "+oppScore);

        tg.setForegroundColor(TextColor.ANSI.DEFAULT);
        tg.putString(5, 10, "press ENTER to start a new game");
    }

    @Override
    public void handleInput(KeyStroke key){
        if(key.getKeyType()==KeyType.Enter){
            manager.show(new TossScreen(manager, config));
        }
    }
}
