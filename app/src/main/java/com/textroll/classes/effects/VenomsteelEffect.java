package com.textroll.classes.effects;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Effect;

public class VenomsteelEffect extends Effect {

    private double magnitude;
    private int duration;

    public VenomsteelEffect(double magnitude, int duration) {
        this.magnitude = magnitude;
        this.duration = duration;
    }

    @Override
    public void onApply() {
        actor.getAttributes().getSpeed().modifyMultiplier(-magnitude);
        actor.getAttributes().getStrength().modifyMultiplier(-magnitude);
        Instances.turnManager.log(String.format("%s is suffering from Venomsteel Blades!\n", actor.getName()));
    }

    @Override
    public void onRemove() {
        actor.getAttributes().getSpeed().modifyMultiplier(magnitude);
        actor.getAttributes().getStrength().modifyMultiplier(magnitude);
    }

    @Override
    public void onTurnStart() {

    }

    @Override
    public void onTurnEnd() {
        duration--;
        if (duration <= 0) remove();
    }

    @Override
    public String toString() {
        return "Venomsteel Poisoning";
    }
}
