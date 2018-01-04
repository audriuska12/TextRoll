package com.textroll.classes.effects;

import android.annotation.SuppressLint;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.Instances;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.ItemEffect;


public class ItemEffectAbaddonsStaff extends ItemEffect {
    private int period;
    private int count = 0;
    private int magnitude;

    public ItemEffectAbaddonsStaff(DataSnapshot snapshot) {
        this.magnitude = Integer.valueOf((String) snapshot.child("magnitude").getValue());
        this.period = Integer.valueOf((String) snapshot.child("period").getValue());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void beforeCasting(Actor target) {
        count++;
        if (count == period) {
            actor.getAttributes().getMagic().modifyBonus(magnitude);
            Instances.turnManager.log(String.format("%s gains %d MAG for this spell from Abaddon's Cloak\n", actor.getName(), magnitude));
        }
    }

    @Override
    public void afterCasting(Actor target) {
        if (count == period) {
            actor.getAttributes().getMagic().modifyBonus(-magnitude);
            count = 0;
        }
    }

    @Override
    public void onEndOfCombat() {
        count = 0;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Every %dth spell cast gains %d bonus MAG.", period, magnitude);
    }
}
