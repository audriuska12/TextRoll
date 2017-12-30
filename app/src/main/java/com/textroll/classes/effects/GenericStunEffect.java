package com.textroll.classes.effects;

import com.textroll.mechanics.Effect;

public class GenericStunEffect extends Effect {

    private int duration;

    public GenericStunEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void onRemove() {
        actor.removeStun();
    }

    @Override
    public void onApply() {
        actor.addStun();
    }

    @Override
    public void onTurnStart() {

    }

    @Override
    public void onTurnEnd() {
        duration--;
        if (duration == 0) remove();
    }

    @Override
    public String toString() {
        return "Stun";
    }
}
