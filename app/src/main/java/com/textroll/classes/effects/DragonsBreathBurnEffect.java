package com.textroll.classes.effects;

import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Effect;

public class DragonsBreathBurnEffect extends Effect {

    int duration;
    int magnitude;
    Actor source;

    public DragonsBreathBurnEffect(int magnitude, int duration, Actor source) {
        this.duration = duration;
        this.magnitude = magnitude;
        this.source = source;
    }

    @Override
    public void onTurnEnd() {
        actor.takeDamage(magnitude, source);
        duration--;
        if (duration <= 0) {
            remove();
        }
    }

    @Override
    public String toString() {
        return "Dragonfire Burn";
    }
}
