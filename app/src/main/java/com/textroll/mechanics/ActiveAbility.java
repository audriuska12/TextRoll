package com.textroll.mechanics;

public abstract class ActiveAbility implements Ability {
    int currentRank;
    int maxRank;
    Action action;

    @Override
    public void setMaxRank(int rank) {
        if(rank > 0)this.maxRank = rank;
        if(currentRank >maxRank) currentRank = maxRank;
    }

    @Override
    public void changeRank(int change) {
        if(currentRank + change >= 0) currentRank = Math.min(currentRank + change, maxRank);
    }

    @Override
    public int getCurrentRank() {
        return currentRank;
    }

    @Override
    public void setCurrentRank(int rank) {
        if (rank >= 0 && rank <= maxRank) this.currentRank = rank;
    }

    @Override
    public int getMaximumRank() {
        return maxRank;
    }

    public Action getAction(){
        return action;
    }

    public void setAction(Action action){
        this.action = action;
    }
}