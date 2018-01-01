package com.textroll.classes.effects;

import com.textroll.mechanics.Effect;

/**
 * Created by audri on 2018-01-01.
 */

public class SoulHarvestDebuff extends Effect {
    private int magnitude;

    public SoulHarvestDebuff(int magnitude) {
        this.magnitude = magnitude;
    }

    @Override
    public void onApply() {
        actor.getAttributes().getStrength().modifyBonus(-magnitude);
        actor.getAttributes().getIntelligence().modifyBonus(-magnitude);
        actor.getAttributes().getSpeed().modifyBonus(-magnitude);
        actor.getAttributes().getMagic().modifyBonus(-magnitude);
    }

    @Override
    public void onRemove() {
        actor.getAttributes().getStrength().modifyBonus(magnitude);
        actor.getAttributes().getIntelligence().modifyBonus(magnitude);
        actor.getAttributes().getSpeed().modifyBonus(magnitude);
        actor.getAttributes().getMagic().modifyBonus(magnitude);
    }
}
