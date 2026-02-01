package dev.willow.ui.screens;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import dev.willow.engine.gameplay.GamePlayer;
import dev.willow.ui.Screen;
import dev.willow.ui.ScreenManager;
import dev.willow.ui.widgets.MenuWidget;

import java.util.List;
import java.util.OptionalInt;

public class OversConfigScreen implements Screen{
    private final ScreenManager manager;
    private final GamePlayer player;
    private final MenuWidget menu;

    public OversConfigScreen(ScreenManager manager, GamePlayer player){
        this.manager=manager;
        this.player=player;
        this.menu=new MenuWidget(List.of("1 over", "2 overs", "custom", "unlimited"));
    }

    @Override
    public void render(){
        TextGraphics tg=manager.terminal().newTextGraphics();
        tg.putString(5, 2, "MATCH CONFIGURATION");
        tg.putString(5, 4, "overs per innings");
        menu.render(tg, 5, 6);
    }

    @Override
    public void handleInput(KeyStroke key){
        if(key.getKeyType()==KeyType.ArrowUp || key.getKeyType()==KeyType.ArrowDown){
            menu.handleInput(key);
            return;
        }
        if(menu.isSelected(key)){
            switch(menu.selectedIndex()){
                case 0->manager.show(new WicketsConfigScreen(manager, player, OptionalInt.of(1)));
                case 1->manager.show(new WicketsConfigScreen(manager, player, OptionalInt.of(2)));
                case 2->manager.show(new CustomConfigScreen(manager, "overs per innings", 15, value->manager.show(
                    new WicketsConfigScreen(manager, player, OptionalInt.of(value))), this));
                case 3->manager.show(new WicketsConfigScreen(manager, player, OptionalInt.empty()));
            }
        }
    }
}
