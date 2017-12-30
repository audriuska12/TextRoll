package com.textroll.classes.effects;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Effect;

/**
 * Created by audri on 2017-12-20.
 */
public class BerserkEffect extends Effect {

    Actor actor;
    int duration;
    int power;

    public BerserkEffect(int power, int duration) {
        this.duration = duration;
        this.power = power;
    }

    @Override
    public void apply(Actor target) {
        actor = target;
        actor.getAttributes().getStrength().modifyBonus(power);
        actor.getEffects().add(this);
        Instances.turnManager.log(String.format("%s enrages for %d turns and gains %d strength!\n", actor.getName(), duration, power));
    }

    @Override
    public void remove() {
        actor.getAttributes().getStrength().modifyBonus(-power);
        actor.getEffects().remove(this);
        Instances.turnManager.log(String.format("%s stops raging.\n", actor.getName()));
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
        return "Berserk";
    }
}
