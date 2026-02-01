package dev.willow.engine.gameplay;

public class PlayerFactory{
    public static PlayerWillow create(GamePlayer player){
        return switch(player){
            case JUSTIN->new Justin();
            case WILSON->new Wilson();
            case CLARK->new Clark();
        };
    }
}
