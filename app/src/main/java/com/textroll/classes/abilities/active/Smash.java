package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.effects.BashEffect;
import com.textroll.classes.effects.GenericStunEffect;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class Smash extends ActiveAbility {
    public Smash(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new SmashAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "Smash";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Smash (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Deal %d damage to target enemy. If you're empowered by Bash, deal %d extra and stun the enemy for 1 turn.\nCooldown: %d", actor.getAttributes().getStrength().getEffectiveValue(), actor.getAttributes().getStrength().getEffectiveValue() * getCurrentRank(), 6);
    }
}

class SmashAction extends Action implements Cooldown {
    private int cooldown = 0;
    private ActiveAbility ability;

    SmashAction(ActiveAbility ability, Actor user) {
        this.ability = ability;
        this.user = user;
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
        int dmgDealt = user.getAttributes().getStrength().getEffectiveValue() + (user.hasEffect(BashEffect.class) ? user.getAttributes().getStrength().getEffectiveValue() * ability.getCurrentRank() : 0);
        target.takeDamage(dmgDealt, user);
        GenericStunEffect stun = new GenericStunEffect(1);
        stun.apply(target);
        target.afterAttacked(user);
        setRemainingCooldown(6);
        if (user.isDead()) return;
        user.afterAttacking(target);
    }

    @Override
    public boolean isAvailable() {
        return (cooldown <= 0);
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return actor.getFaction() != target.getFaction();
    }

    @Override
    public int getPriority() {
        if (user.hasEffect(BashEffect.class)) {
            return 3 * user.getAttributes().getStrength().getEffectiveValue();
        } else return user.getAttributes().getStrength().getEffectiveValue() + 3;
    }

    @Override
    public int getThreat(Actor target) {
        return target.getThreat() + (target.isStunned() ? 0 : 5);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Smash (%d)", getRemainingCooldown());
        } else return "Smash";
    }
}
