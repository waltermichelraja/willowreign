package dev.willow.engine;

import java.util.OptionalInt;

import dev.willow.engine.gameplay.GamePlayer;

public class GameConfig {
    private final GamePlayer player;
    private final OptionalInt oversPerInnings;
    private final OptionalInt wicketsPerInnings;

    public GameConfig(GamePlayer player, OptionalInt oversPerInnings, OptionalInt wicketsPerInnings){
        this.player=player;
        this.oversPerInnings=oversPerInnings;
        this.wicketsPerInnings=wicketsPerInnings;
    }

    public OptionalInt getOversPerInnings(){return oversPerInnings;}

    public OptionalInt getWicketsPerInnings(){return wicketsPerInnings;}

    public GamePlayer getPlayer(){return player;}
}
