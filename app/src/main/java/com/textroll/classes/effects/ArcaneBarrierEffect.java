package com.textroll.classes.effects;

import android.annotation.SuppressLint;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Effect;


public class ArcaneBarrierEffect extends Effect {

    private int duration;
    private int magnitude;

    public ArcaneBarrierEffect(int duration, int magnitude) {
        this.magnitude = magnitude;
        this.duration = duration;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public int onTakeDamage(int damage, Actor source) {
        if (damage <= magnitude) {
            magnitude -= damage;
            Instances.turnManager.log(String.format("%s's Arcane Barrier blocks %d damage. %d remaining.\n", actor.getName(), damage, magnitude));
            return 0;
        } else {
            damage -= magnitude;
            Instances.turnManager.log(String.format("%s's Arcane Barrier blocks %d damage and breaks.\n", actor.getName(), magnitude));
            remove();
            return damage;
        }
    }

    @Override
    public void onTurnEnd() {
        duration--;
        if (duration <= 0) {
            remove();
        }
    }

    @Override
    public String toString() {
        return "Arcane Barrier";
    }
}
