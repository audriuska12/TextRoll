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

    public void onStartOfCombat() {
    }

    public void onEndOfCombat() {
    }

    public void refresh() {
    }
}
