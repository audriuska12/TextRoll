package com.textroll.classes.effects;

import android.annotation.SuppressLint;

import com.google.firebase.database.DataSnapshot;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;
import com.textroll.mechanics.ItemEffect;

public class ItemEffectAscalonsWrath extends ItemEffect {
    private int maxCooldown;
    private int magnitude;
    private AscalonsWrathAction action;

    public ItemEffectAscalonsWrath(DataSnapshot snapshot) {
        this.maxCooldown = Integer.valueOf((String) snapshot.child("cooldown").getValue());
        this.magnitude = Integer.valueOf((String) snapshot.child("magnitude").getValue());
        this.action = new AscalonsWrathAction(this, actor);
    }

    public int getMaxCooldown() {
        return maxCooldown;
    }

    public void setMaxCooldown(int maxCooldown) {
        this.maxCooldown = maxCooldown;
    }

    public int getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(int magnitude) {
        this.magnitude = magnitude;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Active: Ascalon's Wrath\nDeal %d damage to target enemy and stun them.\nCooldown:%d", actor.getAttributes().getStrength().getEffectiveValue(), maxCooldown);
    }

    @Override
    public void onStartOfCombat() {
        actor.getActions().add(action);
        action.setRemainingCooldown(0);
    }

    @Override
    public void onEndOfCombat() {
        actor.getActions().remove(action);
    }

    @Override
    public void refresh() {
        action.setRemainingCooldown(0);
        actor.getActions().remove(action);
    }

}

class AscalonsWrathAction extends Action implements Cooldown {

    private ItemEffectAscalonsWrath effect;
    private int cooldown = 0;

    public AscalonsWrathAction(ItemEffectAscalonsWrath effect, Actor actor) {
        this.effect = effect;
        this.user = actor;
    }

    @Override
    public int getRemainingCooldown() {
        return cooldown;
    }

    @Override
    public void setRemainingCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public void coolDown() {
        if (cooldown > 0) cooldown--;
    }

    @Override
    public void execute() {
        user.beforeAttacking(target);
        if (user.isDead() || target.isDead()) return;
        if (!target.beforeAttacked(user)) return;
        if (user.isDead() || target.isDead()) return;
        int dmgDealt = user.getAttributes().getStrength().getEffectiveValue() + effect.getMagnitude();
        target.takeDamage(dmgDealt, user);
        GenericStunEffect stun = new GenericStunEffect(1);
        stun.apply(target);
        target.afterAttacked(user);
        if (user.isDead()) return;
        user.afterAttacking(target);
    }

    @Override
    public boolean isAvailable() {
        return cooldown <= 0;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return actor.getFaction() != target.getFaction();
    }

    @Override
    public int getPriority() {
        return user.getAttributes().getStrength().getEffectiveValue() + effect.getMagnitude() + 5;
    }

    @Override
    public int getThreat(Actor actor) {
        return actor.getThreat() + (actor.isStunned() ? 0 : 5);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Ascalon's Wrath (%d)", cooldown);
        } else {
            return "Ascalon's Wrath";
        }
    }
}