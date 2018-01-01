package com.textroll.mechanics;

public abstract class ItemEffect extends Effect {

    @Override
    public void apply(Actor actor) {
        this.actor = actor;
        actor.itemEffects.add(this);
    }

    @Override
    public void remove() {
        actor.itemEffects.remove(this);
    }

    public abstract String getDescription();

    void onStartOfCombat() {
    }

    void onEndOfCombat() {
    }

    public int onReceiveHealing(int healing, Actor source) {
        return healing;
    }

    public void refresh() {
    }
}
