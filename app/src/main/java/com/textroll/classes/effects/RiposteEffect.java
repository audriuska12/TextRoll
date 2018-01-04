package com.textroll.classes.effects;

import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Effect;

public class RiposteEffect extends Effect {
    private int magnitude;

    public RiposteEffect(int magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public boolean beforeAttacked(Actor attacker) {
        if (actor.getFaction() == attacker.getFaction()) return true;
        attacker.takeDamage(magnitude, actor);
        remove();
        return false;
    }

    @Override
    public boolean beforeSpellHit(Actor attacker) {
        if (actor.getFaction() == attacker.getFaction()) return true;
        attacker.takeDamage(magnitude, actor);
        remove();
        return false;
    }

    @Override
    public String toString() {
        return "Riposte";
    }
}
