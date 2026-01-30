package dev.willow.ui.screens;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import dev.willow.engine.GameConfig;
import dev.willow.engine.gameplay.GamePlayer;
import dev.willow.ui.Screen;
import dev.willow.ui.ScreenManager;
import dev.willow.ui.widgets.MenuWidget;

import java.util.List;
import java.util.OptionalInt;

public class WicketsConfigScreen implements Screen{
    private final ScreenManager manager;
    private final GamePlayer player;
    private final OptionalInt overs;
    private final MenuWidget menu;

    public WicketsConfigScreen(ScreenManager manager, GamePlayer player, OptionalInt overs){
        this.manager=manager;
        this.player=player;
        this.overs=overs;
        this.menu=new MenuWidget(List.of("1 wicket", "2 wickets", "custom", "unlimited"));
    }

    @Override
    public void render(){
        TextGraphics tg=manager.terminal().newTextGraphics();
        tg.putString(5, 2, "MATCH CONFIGURATION");
        tg.putString(5, 4, "wickets per innings");
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
                case 0->finish(OptionalInt.of(1));
                case 1->finish(OptionalInt.of(2));
                case 2->manager.show(new CustomConfigScreen(manager, "wickets per innings", 6, value->finish(OptionalInt.of(value)), this));
                case 3->finish(OptionalInt.empty());
            }
        }
    }

    private void finish(OptionalInt wickets){
        GameConfig config=new GameConfig(player, overs, wickets);
        manager.show(new TossScreen(manager, config));
    }
}
