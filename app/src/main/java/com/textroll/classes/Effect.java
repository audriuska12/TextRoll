package com.textroll.classes;

/**
 * Created by audri on 2017-10-30.
 */

public abstract class Effect {
    private Actor actor;
    public abstract void apply(Actor target);
    public abstract void remove();
    public abstract void onTurnStart();
    public abstract void onTurnEnd();
}
