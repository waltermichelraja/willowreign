package dev.willow.engine.gameplay;

import dev.willow.engine.InningsState;

import java.util.Random;

public class Wilson implements PlayerWillow{
    private final Random random=new Random();
    private Integer lastMove=null;
    private Integer secondLastMove=null;

    @Override
    public int nextMove(boolean batting, InningsState state, GameContext context){
        int move=batting?bat(state,context):bowl();
        secondLastMove=lastMove;
        lastMove=move;
        return move;
    }

    private int bat(InningsState state, GameContext context){
        if(context.chasing && context.target>0){
            int req=context.target-state.getRuns();
            if(req<=3){return randomBetween(3, 6);}
            if(req<=6){return randomBetween(2, 6);}
            if(req<=12){return randomBetween(2, 5);}
        }
        if(context.wicketsLeft<=1){return randomBetween(1,4);}
        return weightedRandom(new int[]{0,1,2,3,4,5,6}, new int[]{1,1,2,3,4,3,2});
    }

    private int bowl(){
        int candidate;
        int safety=0;
        if(lastMove==null && secondLastMove==null){return randomBetween(0, 6);}
        do{
            candidate=randomBetween(0, 6);
            safety++;
        }while(((lastMove!=null && candidate==lastMove) || (secondLastMove!=null && candidate==secondLastMove)) && safety<10);
        return candidate;
    }

    private int randomBetween(int min, int max){return random.nextInt(max-min+1)+min;}

    private int weightedRandom(int[] values, int[] weights){
        int total=0;
        for(int w:weights){total+=w;}
        int r=random.nextInt(total);
        for(int i=0;i<values.length;i++){
            r-=weights[i];
            if(r<0){return values[i];}
        }
        return values[0];
    }
}
