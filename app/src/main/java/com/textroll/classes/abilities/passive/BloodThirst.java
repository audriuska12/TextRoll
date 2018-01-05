package com.textroll.classes.abilities.passive;

import android.annotation.SuppressLint;

import com.textroll.mechanics.Actor;
import com.textroll.mechanics.PassiveAbility;

public class BloodThirst extends PassiveAbility {
    private int startHealth;
    private int endHealth;

    public BloodThirst(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
    }

    @Override
    public String getFirebaseName() {
        return "BloodThirst";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Blood Thirst (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Heal for %d%% of damage dealt when below 50%% health.", getCurrentRank() * 10);
    }

    @Override
    public void refresh() {
        startHealth = 0;
        endHealth = 0;
    }

    @Override
    public void beforeAttacking(Actor target) {
        if (actor.getCurrentHealth() <= actor.getMaximumHealth() / 2) {
            startHealth = target.getCurrentHealth();
        } else {
            startHealth = 0;
        }
    }

    @Override
    public void afterAttacking(Actor target) {
        if (startHealth > 0) {
            endHealth = target.getCurrentHealth();
            if (startHealth - endHealth > 0) {
                int healing = (10 * getCurrentRank() * (startHealth - endHealth)) / 100;
                if (healing > 0) {
                    actor.heal(healing, actor);
                }
            }
        }
        endHealth = 0;
    }
}
