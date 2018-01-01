package com.textroll.classes.effects;

import android.annotation.SuppressLint;

import com.google.firebase.database.DataSnapshot;
import com.textroll.mechanics.ItemEffect;

public class ItemEffectAbaddonsRing extends ItemEffect {
    private int magnitude;
    private int durationMax;
    private int duration = 0;
    private boolean used = false;

    public ItemEffectAbaddonsRing(DataSnapshot snapshot) {
        used = false;
        duration = 0;
        this.magnitude = Integer.valueOf((String) snapshot.child("magnitude").getValue());
        this.durationMax = Integer.valueOf((String) snapshot.child("duration").getValue());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("On taking lethal damage, regenerate %d health per turn for %d turns. You are stunned for the duration. Once per combat.\n", magnitude, durationMax);
    }
}
