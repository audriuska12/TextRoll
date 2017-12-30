package com.textroll.classes.effects;

import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Effect;

public class FireballEffect extends Effect {

    private Actor user;
    private int duration;
    private int magnitude;

    public FireballEffect(Actor user, int duration, int magnitude) {
        this.user = user;
        this.duration = duration;
        this.magnitude = magnitude;
    }

    @Override
    protected void onApply() {
        actor.addStun();
    }

    @Override
    protected void onRemove() {
        actor.removeStun();
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
        return "Fireball";
    }
}
