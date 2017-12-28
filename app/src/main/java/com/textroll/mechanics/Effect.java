package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

public abstract class Effect implements Serializable {
    protected Actor actor;

    protected Effect() {
    }

    public Effect(DataSnapshot snapshot) {
    }

    public void apply(Actor target) {
        actor = target;
        actor.effects.add(this);
        onApply();
    }

    protected void onApply() {
    }

    public void remove() {
        onRemove();
        actor.effects.remove(this);
        this.actor = null;
    }

    protected void onRemove() {
    }
    public abstract void onTurnStart();

    public abstract void onTurnEnd();

    int onTakeDamage(int damage, Actor source) {
        return damage;
    }

    boolean onDying() {
        return true;
    }

    void onDeath() {
    }

    public int onReceiveHealing(int healing, Actor source) {
        return healing;
    }
}
