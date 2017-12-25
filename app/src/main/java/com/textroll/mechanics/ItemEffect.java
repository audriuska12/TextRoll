package com.textroll.mechanics;

import com.google.firebase.database.DatabaseReference;
import com.textroll.mechanics.Effect;

/**
 * Created by audri on 2017-12-20.
 */

public abstract class ItemEffect extends Effect {

    @Override
    public void apply(Actor actor) {
        this.actor = actor;
        actor.itemEffects.add(this);
    }

    @Override
    public void remove() {
        actor.itemEffects.remove(this);
    }

    public abstract String getDescription();
}
