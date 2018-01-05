package com.textroll.classes.abilities.passive;

import android.annotation.SuppressLint;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.PassiveAbility;


public class BloodRage extends PassiveAbility {
    private int stacks = 0;

    public BloodRage(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
    }

    @Override
    public void onEndOfCombat() {
        actor.getAttributes().getStrength().modifyBonus(-getCurrentRank() * stacks);
        stacks = 0;
    }

    @Override
    public void refresh() {
        actor.getAttributes().getStrength().modifyBonus(-getCurrentRank() * stacks);
        stacks = 0;
    }
    @SuppressLint("DefaultLocale")
    @Override
    public int onTakeDamage(int damageTaken, Actor source) {
        actor.getAttributes().getStrength().modifyBonus(getCurrentRank());
        stacks++;
        Instances.turnManager.log(String.format("%s gains %d STR from Blood Rage.\n", actor, getCurrentRank()));
        return damageTaken;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTurnEnd() {
        if (stacks > 0) {
            Instances.turnManager.log(String.format("Blood Rage expires. %s loses %d STR.\n", actor, stacks * getCurrentRank()));
            actor.getAttributes().getStrength().modifyBonus(-getCurrentRank() * stacks);
            stacks = 0;
        }
    }

    @Override
    public String getFirebaseName() {
        return "BloodRage";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Blood Rage (%d/%d)", currentRank, maxRank);
    }

    @Override
    public String getDescription() {
        return String.format("Gain %d STR until the end of your turn when you take damage.", getCurrentRank());
    }


}