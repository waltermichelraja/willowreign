package dev.willow.engine.gameplay;

public class GameContext{
    public final int target;
    public final boolean chasing;
    public final int ballsLeft;
    public final int wicketsLeft;

    public GameContext(int target, boolean chasing, int ballsLeft, int wicketsLeft) {
        this.target=target;
        this.chasing=chasing;
        this.ballsLeft=ballsLeft;
        this.wicketsLeft=wicketsLeft;
    }
}
