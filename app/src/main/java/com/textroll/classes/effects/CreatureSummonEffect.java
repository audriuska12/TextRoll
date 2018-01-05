package com.textroll.classes.effects;

import com.textroll.mechanics.Effect;
import com.textroll.mechanics.SummonedCreature;

public class CreatureSummonEffect extends Effect {

    private SummonedCreature creature;
    private int duration;
    private boolean terminating;

    public CreatureSummonEffect(SummonedCreature creature, int duration, boolean terminating) {
        this.creature = creature;
        this.duration = duration;
        this.terminating = terminating;
    }

    @Override
    public void onDeath() {
        if (terminating) {
            duration = 0;
            creature.die();
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void onRemove() {
        creature.die();
    }

    public void tickDuration() {
        duration--;
        if (duration <= 0) remove();
    }

    @Override
    public String toString() {
        return "Summoning";
    }
}
