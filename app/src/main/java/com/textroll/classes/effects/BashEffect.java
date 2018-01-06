package com.textroll.classes.effects;

import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Effect;

public class BashEffect extends Effect {

    private int magnitude;
    private boolean attacking = false;

    public BashEffect(int magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public void beforeAttacking(Actor target) {
        actor.getAttributes().getStrength().modifyBonus(magnitude);
        attacking = true;
    }

    @Override
    public void afterAttacking(Actor target) {
        if (attacking) {
            attacking = false;
            actor.getAttributes().getStrength().modifyBonus(-magnitude);
            remove();
        }
    }

    @Override
    public String toString() {
        return "Empowered";
    }
}
