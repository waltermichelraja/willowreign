package dev.willow.ui.screens;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import dev.willow.engine.gameplay.GamePlayer;
import dev.willow.ui.Screen;
import dev.willow.ui.ScreenManager;
import dev.willow.ui.widgets.MenuWidget;

import java.util.List;

public class OpponentConfigScreen implements Screen {
    private final ScreenManager manager;
    private final MenuWidget menu;

    public OpponentConfigScreen(ScreenManager manager){
        this.manager=manager;
        this.menu=new MenuWidget(List.of("JUSTIN", "WILSON", "CLARK"));
    }

    @Override
    public void render(){
        TextGraphics tg=manager.terminal().newTextGraphics();
        tg.putString(5, 2, "CHOOSE OPPONENT");
        tg.putString(5, 4, "select who you play against");
        menu.render(tg, 5, 6);
    }

    @Override
    public void handleInput(KeyStroke key){
        if(key.getKeyType()==KeyType.ArrowUp || key.getKeyType()==KeyType.ArrowDown){
            menu.handleInput(key);
            return;
        }
        if(menu.isSelected(key)){
            GamePlayer player=switch(menu.selectedIndex()){
                case 0->GamePlayer.JUSTIN;
                case 1->GamePlayer.WILSON;
                case 2->GamePlayer.CLARK;
                default->GamePlayer.JUSTIN;
            };
            manager.show(new OversConfigScreen(manager, player));
        }
    }
}
