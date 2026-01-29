package dev.willow.engine.gameplay;

import dev.willow.engine.InningsState;

import java.util.Random;

public class Justin implements PlayerWillow{
    private final Random random=new Random();
    private Integer lastMove=null;

    public int nextMove(boolean batting, InningsState state, GameContext context){
        int move;
        if(batting){move=bat(state, context);}
        else{move=bowl(context);}
        lastMove=move;
        return move;
    }

    private int bat(InningsState state, GameContext context){
        if(context.chasing && context.target>0 && context.target-state.getRuns()<=6){return randomBetween(1, 3);}
        if(context.wicketsLeft<=1){return randomBetween(1, 4);}
        return weightedRandom(new int[]{0,1,2,3,4,5,6}, new int[]{1,2,3,3,3,2,1});
    }

    private int bowl(GameContext context){
        int candidate;
        if(lastMove==null){return randomBetween(0, 6);}
        do{candidate=randomBetween(0, 6);}while(candidate==lastMove);
        return candidate;
    }

    private int randomBetween(int min, int max){return random.nextInt(max-min+1)+min;}

    private int weightedRandom(int[] values, int[] weights){
        int total=0;
        for(int w: weights){total+=w;}
        int r=random.nextInt(total);
        for(int i=0;i<values.length;i++){
            r-=weights[i];
            if(r<0){return values[i];}
        }
        return values[0];
    }
}
