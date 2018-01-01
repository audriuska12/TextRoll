package com.textroll.classes.effects;

import android.annotation.SuppressLint;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.Instances;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.ItemEffect;

public class ItemEffectAbaddonsCloak extends ItemEffect {
    private int magnitude;
    private int remaining;

    public ItemEffectAbaddonsCloak(DataSnapshot snapshot) {
        this.magnitude = Integer.valueOf((String) snapshot.child("magnitude").getValue());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Block the first %d damage received each turn.", magnitude);
    }

    @Override
    public void onTurnStart() {
        remaining = magnitude;
    }

    @Override
    public void refresh() {
        remaining = magnitude;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public int onTakeDamage(int damage, Actor source) {
        if (damage <= remaining) {
            remaining -= damage;
            Instances.turnManager.log(String.format("%s's Abaddon's Cloak blocks %d damage.\n", actor.getName(), damage));
            return 0;
        } else {
            damage -= remaining;
            Instances.turnManager.log(String.format("%s's Abaddon's Cloak blocks %d damage.\n", actor.getName(), remaining));
            remaining = 0;
            return damage;
        }
    }
}
