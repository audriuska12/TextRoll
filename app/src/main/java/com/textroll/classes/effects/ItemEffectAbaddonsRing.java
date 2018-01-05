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

    @Override
    public boolean onDying() {
        if (used) return true;
        actor.heal(magnitude, actor);
        used = true;
        duration = durationMax;
        actor.addStun();
        return actor.getCurrentHealth() <= 0;
    }

    @Override
    public void onTurnEnd() {
        if (used && duration > 0) {
            actor.heal(magnitude, actor);
            duration--;
            if (duration == 0) actor.removeStun();
        }
    }

    @Override
    public void onEndOfCombat() {
        used = false;
        duration = 0;
    }

    @Override
    public void refresh() {
        used = false;
        duration = 0;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("On taking lethal damage, regenerate to %d health instantly as well as regenerating %d health per turn for %d turns. You are stunned for the duration. Once per combat.\n", magnitude, magnitude, durationMax);
    }
}
