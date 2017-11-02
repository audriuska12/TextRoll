package com.textroll.mechanics;

public abstract class Effect {
    private Actor actor;
    public abstract void apply(Actor target);
    public abstract void remove();
    public abstract void onTurnStart();
    public abstract void onTurnEnd();
}
