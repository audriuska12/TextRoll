package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class ViciousBite extends ActiveAbility {
    public ViciousBite(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.setAction(new ViciousBiteAction(this, actor));
    }

    public String getFirebaseName() {
        return "ViciousBite";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Vicious Bite (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Bite the enemy, dealing %d damage plus %d%% of their missing health.\nCooldown: %d", actor.getAttributes().getStrength().getEffectiveValue(), 5 * getCurrentRank(), 4);
    }
}

class ViciousBiteAction extends Action implements Cooldown {
    private ActiveAbility ability;
    private int cooldown;

    ViciousBiteAction(ActiveAbility ability, Actor actor) {
        this.ability = ability;
        this.user = actor;
    }

    public void execute() {
        user.beforeAttacking(target);
        if (user.isDead() || target.isDead()) return;
        if (!target.beforeAttacked(user)) return;
        if (user.isDead() || target.isDead()) return;
        int dmgDealt = user.getAttributes().getStrength().getEffectiveValue() + ((100 - 5 * ability.getCurrentRank()) * (target.getMaximumHealth() - target.getCurrentHealth())) / 100;
        target.takeDamage(dmgDealt, user);
        target.afterAttacked(user);
        if (user.isDead()) return;
        cooldown = 4;
        user.afterAttacking(target);
    }

    @Override
    public boolean isAvailable() {
        return cooldown <= 0;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return (actor.getFaction() != target.getFaction());
    }

    @Override
    public int getPriority() {
        return user.getAttributes().getStrength().getEffectiveValue() + 5;
    }

    @Override
    public int getThreat(Actor actor) {
        return actor.getThreat();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Vicious Bite (%d)", cooldown);
        } else {
            return "Vicious Bite";
        }
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
}
