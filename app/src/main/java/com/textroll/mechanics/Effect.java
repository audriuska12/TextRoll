package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.Instances;

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
        Instances.turnManager.log(String.format("%s ends on %s.\n", this.toString(), actor));
        actor.effects.remove(this);
        this.actor = null;
    }

    protected void onRemove() {
    }

    public void onTurnStart() {
    }

    public void onTurnEnd() {
    }

    public int onTakeDamage(int damage, Actor source) {
        return damage;
    }

    public boolean onDying() {
        return true;
    }

    public void onDeath() {
    }

    public int onReceiveHealing(int healing, Actor source) {
        return healing;
    }

    public void beforeAttacking(Actor target) {
    }

    public void afterAttacking(Actor target) {
    }

    public boolean beforeAttacked(Actor attacker) {
        return true;
    }

    public void afterAttacked(Actor attacker) {
    }

    public void beforeCasting(Actor target) {
    }

    public void afterCasting(Actor target) {
    }

    public boolean beforeSpellHit(Actor attacker) {
        return true;
    }

    public void afterSpellHit(Actor attacker) {
    }
}
