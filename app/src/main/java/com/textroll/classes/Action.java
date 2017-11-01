package com.textroll.classes;

/**
 * Created by audri on 2017-09-29.
 */

public abstract class Action {
    protected Actor user;
    protected Actor target;
    public void setTarget(Actor target){
        this.target = target;
    }
    public abstract boolean execute();
    public abstract boolean isAvailable(Actor actor, Actor target);
    public abstract boolean validForTarget(Actor actor, Actor target);
}
