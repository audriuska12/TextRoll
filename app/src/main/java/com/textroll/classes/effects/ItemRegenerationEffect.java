package com.textroll.classes.effects;

import android.annotation.SuppressLint;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.Instances;
import com.textroll.mechanics.ItemEffect;

public class ItemRegenerationEffect extends ItemEffect {

    private int magnitude;

    public ItemRegenerationEffect(DataSnapshot snapshot) {
        this.magnitude = Integer.valueOf((String) snapshot.child("magnitude").getValue());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onTurnStart() {
        actor.heal(this.magnitude, actor);
        Instances.turnManager.log(String.format("%s regenerates for %d health!\n", actor.getName(), this.magnitude));
    }

    @Override
    public void onTurnEnd() {

    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Regeneration %d", magnitude);
    }

    @Override
    public String toString() {
        return "Item Regeneration";
    }
}
