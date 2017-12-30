package com.textroll.classes.effects;

import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Effect;


public class VipersBiteEffect extends Effect {

    private Actor user;
    private int duration;
    private int magnitude;

    public VipersBiteEffect(Actor user, int duration, int magnitude) {
        this.user = user;
        this.duration = duration;
        this.magnitude = magnitude;
    }

    @Override
    public void onTurnStart() {

    }

    @Override
    public void onTurnEnd() {
        actor.takeDamage(magnitude, user);
        duration--;
        if (duration <= 0) remove();
    }

    @Override
    public String toString() {
        return "Viper's Bite";
    }
}
