package dev.willow.engine.gameplay;

import dev.willow.engine.InningsState;

public interface PlayerWillow{
    int nextMove(boolean batting, InningsState state, GameContext context);
}
