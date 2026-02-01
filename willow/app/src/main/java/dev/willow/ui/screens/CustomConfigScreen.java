package dev.willow.ui.screens;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import dev.willow.ui.Screen;
import dev.willow.ui.ScreenManager;

import java.util.function.IntConsumer;

public class CustomConfigScreen implements Screen {
    private final ScreenManager manager;
    private final String title;
    private final int maxValue;
    private final IntConsumer onValidInput;
    private final Screen fallback;

    private StringBuilder buffer=new StringBuilder();

    public CustomConfigScreen(ScreenManager manager, String title, int maxValue, IntConsumer onValidInput, Screen fallback){
        this.manager=manager;
        this.title=title;
        this.maxValue=maxValue;
        this.onValidInput=onValidInput;
        this.fallback=fallback;
    }

    @Override
    public void render(){
        TextGraphics tg=manager.terminal().newTextGraphics();
        tg.putString(5, 2, title);
        tg.putString(5, 4, "press ENTER to configure");
        tg.putString(5, 6, "> " + buffer.toString());
        tg.putString(5, 8, "config range: [1-"+maxValue+"]");
    }

    @Override
    public void handleInput(KeyStroke key){
        if(key.getKeyType()==KeyType.Enter){
            try{
                int value=Integer.parseInt(buffer.toString());
                if (value>=1 && value<=maxValue){
                    onValidInput.accept(value);
                    return;
                }
            }catch(NumberFormatException ignored){}
            buffer.setLength(0);
            manager.show(fallback);
            return;
        }
        if(key.getKeyType()==KeyType.Backspace && buffer.length()>0){
            buffer.deleteCharAt(buffer.length()-1);
            return;
        }
        if(key.getKeyType()==KeyType.Character){
            char c=key.getCharacter();
            if(Character.isDigit(c)){buffer.append(c);}
        }
    }
}
