package dev.willow.ui.screens;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import dev.willow.ui.Screen;
import dev.willow.ui.ScreenManager;
import dev.willow.ui.widgets.MenuWidget;

import java.util.List;

public class MainMenuScreen implements Screen{
    private final ScreenManager manager;
    private final MenuWidget menu;

    public MainMenuScreen(ScreenManager manager){
        this.manager=manager;
        this.menu=new MenuWidget(List.of("start game", "quit"));
    }

    @Override
    public void render(){
        TextGraphics tg=manager.terminal().newTextGraphics();
        tg.putString(5, 2, "WELCOME TO THE  W I L L O W");
        tg.putString(5, 3, "---------------------------");
        tg.putString(5, 5, "use ARROW-KEYS and ENTER");
        menu.render(tg, 5, 7);
    }

    @Override
    public void handleInput(KeyStroke key){
        if(key.getKeyType()==KeyType.ArrowUp || key.getKeyType()==KeyType.ArrowDown){menu.handleInput(key);}
        if(menu.isSelected(key)){
            if(menu.selectedIndex()==0){manager.show(new OpponentConfigScreen(manager));}
            if(menu.selectedIndex()==1){System.exit(0);}
        }
    }
}
