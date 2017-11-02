package com.textroll.mechanics;

public abstract class Action {
    protected Actor user;
    protected Actor target;
    public void setTarget(Actor target){
        this.target = target;
    }

    public abstract void execute();
    public abstract boolean isAvailable(Actor actor, Actor target);
    public abstract boolean validForTarget(Actor actor, Actor target);
}
