package dev.willow.engine.gameplay;

import dev.willow.engine.InningsState;

import java.util.Random;

public class Clark implements PlayerWillow{
    private final Random random=new Random();
    private Integer lastMove=null;

    @Override
    public int nextMove(boolean batting, InningsState state, GameContext context){
        int move=batting?bat(state,context):bowl(context,state);
        lastMove=move;
        return move;
    }

    private int bat(InningsState state, GameContext context){
        int runs=state.getRuns();
        if(context.chasing && context.target>0){
            int req=context.target-runs;
            if(req<=2){return randomBetween(1, 2);}
            if(req<=4){return randomBetween(2, 3);}
            if(req<=6){return randomBetween(2, 4);}
            if(req<=12){return randomBetween(3, 5);}
        }
        if(context.wicketsLeft<=1){return randomBetween(1, 3);}
        if(context.ballsLeft>12){return weightedRandom(new int[]{1,2,3,4,5,6}, new int[]{1,2,3,4,3,2});}
        return weightedRandom(new int[]{0,1,2,3,4,5,6}, new int[]{1,2,3,3,3,2,1});
    }

    private int bowl(GameContext context, InningsState state){
        if(lastMove==null){return randomBetween(0, 6);}
        if(context.chasing && context.target>0){
            int req=context.target-state.getRuns();
            if(req<=6){return randomBetween(1, 3);}
        }
        int candidate;
        int safety=0;
        do{
            candidate=randomBetween(0, 6);
            safety++;
        }while(candidate==lastMove && safety<10);
        return candidate;
    }

    private int randomBetween(int min,int max){return random.nextInt(max-min+1)+min;}

    private int weightedRandom(int[] values,int[] weights){
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
