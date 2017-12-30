package com.textroll.classes.abilities.passive;

import android.annotation.SuppressLint;

import com.textroll.classes.effects.VenomsteelEffect;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.PassiveAbility;

public class VenomsteelBlades extends PassiveAbility {

    private int counter = 0;

    public VenomsteelBlades(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
    }

    @Override
    public String getFirebaseName() {
        return "VenomsteelBlades";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Venomsteel Blades (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @Override
    public void afterAttacking(Actor target) {
        counter++;
        if (counter >= 6 - getCurrentRank() && !target.hasEffect(VenomsteelEffect.class)) {
            counter = 0;
            VenomsteelEffect effect = new VenomsteelEffect(0.05 * getCurrentRank(), 3);
            effect.apply(target);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Every %d attacks, reduce the target's Strength and Speed by %d%% for 3 turns. Does not stack.", 6 - getCurrentRank(), 5 * getCurrentRank());
    }
}
