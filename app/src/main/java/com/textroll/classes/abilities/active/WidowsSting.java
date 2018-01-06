package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.effects.GenericStunEffect;
import com.textroll.classes.effects.VipersBiteEffect;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class WidowsSting extends ActiveAbility {
    public WidowsSting(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new WidowsStingAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "WidowsSting";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Widow's Sting (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Deal %d damage to target enemy. Stun for %d turn(s) and deal %d bonus damage if the enemy is poisoned by Viper's Bite.\nCooldown: %d", ((actor.getAttributes().getStrength().getEffectiveValue() * getCurrentRank()) / 5), 1, ((actor.getAttributes().getSpeed().getEffectiveValue() + actor.getAttributes().getIntelligence().getEffectiveValue()) * (2 + getCurrentRank())) / 4, 6);
    }
}

class WidowsStingAction extends Action implements Cooldown {

    private ActiveAbility ability;
    private int cooldown = 0;

    WidowsStingAction(ActiveAbility ability, Actor actor) {
        this.ability = ability;
        this.user = actor;
    }

    @Override
    public void execute() {
        user.beforeAttacking(target);
        if (user.isDead() || target.isDead()) return;
        if (!target.beforeAttacked(user)) return;
        if (user.isDead() || target.isDead()) return;
        if (target.hasEffect(VipersBiteEffect.class)) {
            int damageDealt = (user.getAttributes().getStrength().getEffectiveValue() * ability.getCurrentRank()) / 5 + ((user.getAttributes().getSpeed().getEffectiveValue() + user.getAttributes().getIntelligence().getEffectiveValue()) * (2 + ability.getCurrentRank())) / 4;
            target.takeDamage(damageDealt, user);
            GenericStunEffect stun = new GenericStunEffect(1);
            stun.apply(target);
        } else {
            int damageDealt = (user.getAttributes().getStrength().getEffectiveValue() * ability.getCurrentRank()) / 5;
            target.takeDamage(damageDealt, user);
        }
        target.afterAttacked(user);
        if (user.isDead()) return;
        user.afterAttacking(target);
        setRemainingCooldown(6);
    }

    @Override
    public boolean isAvailable() {
        return cooldown == 0;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return actor.getFaction() != target.getFaction();
    }

    @Override
    public int getThreat(Actor actor) {
        return actor.getThreat() + (actor.isStunned() ? 0 : 10) + (actor.hasEffect(VipersBiteEffect.class) ? 35 : 0);
    }

    @Override
    public int getPriority() {
        int maxPriority = 1;
        for (Actor a : getAvailableTargets()) {
            if (a.hasEffect(VipersBiteEffect.class)) {
                int priority = user.getAttributes().getSpeed().getEffectiveValue() + user.getAttributes().getIntelligence().getEffectiveValue() + (a.isStunned() ? 0 : 5);
                if (priority > maxPriority) return priority;
            }
        }
        return maxPriority;
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

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) return String.format("Widow's Sting (%d)", cooldown);
        else return "Widow's Sting";
    }
}
