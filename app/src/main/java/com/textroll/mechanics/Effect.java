package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public abstract class Effect implements Serializable {
    protected Actor actor;

    public Effect() {
    }

    public Effect(DataSnapshot snapshot) {
    }

    public void apply(Actor target) {
        actor = target;
        actor.effects.add(this);
    }

    public void remove() {
        actor.effects.remove(this);
        this.actor = null;
    }
    public abstract void onTurnStart();

    public abstract void onTurnEnd();

    public int onTakeDamage(int damage, Actor source) {
        return damage;
    }

    public boolean onDying() {
        return true;
    }

    public void onDeath() {
    }
}
