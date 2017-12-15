package com.textroll.mechanics;

public abstract class ActiveAbility implements Ability {
    protected Actor actor;
    protected int currentRank;
    protected int maxRank;
    protected Action action;

    public ActiveAbility(Actor actor, int maxRank, int currentRank) {
        this.actor = actor;
        this.maxRank = maxRank;
        this.currentRank = currentRank;
    }
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