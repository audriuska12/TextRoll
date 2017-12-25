package com.textroll.classes.effects;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.textroll.classes.Instances;
import com.textroll.mechanics.ItemEffect;

/**
 * Created by audri on 2017-12-20.
 */

public class ItemRegenerationEffect extends ItemEffect {

    private int magnitude;

    public ItemRegenerationEffect(DataSnapshot snapshot) {
        this.magnitude = Integer.valueOf((String) snapshot.child("magnitude").getValue());
    }

    @Override
    public void onTurnStart() {
        actor.heal(this.magnitude);
        Instances.turnManager.log(String.format("%s regenerates for %d health!\n", actor.getName(), this.magnitude));
    }

    @Override
    public void onTurnEnd() {

    }

    @Override
    public String getDescription() {
        return String.format("Regeneration %d", magnitude);
    }
}
