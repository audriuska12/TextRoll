package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.effects.VipersBiteEffect;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class VipersBite extends ActiveAbility {
    public VipersBite(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new VipersBiteAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "VipersBite";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Viper's Bite (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Deal %d damage to target enemy and poison them for %d turns, dealing %d damage per turn. \nCooldown: %d", actor.getAttributes().getStrength().getEffectiveValue() / 2, 3 + getCurrentRank(), actor.getAttributes().getIntelligence().getEffectiveValue() / 2, 7 - getCurrentRank());
    }
}

class VipersBiteAction extends Action implements Cooldown {
    private int cooldown;
    private ActiveAbility ability;

    public VipersBiteAction(ActiveAbility ability, Actor user) {
        this.user = user;
        this.ability = ability;
        cooldown = 0;
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
        int damageDealt = user.getAttributes().getStrength().getEffectiveValue() / 2;
        target.takeDamage(damageDealt, user);
        VipersBiteEffect effect = new VipersBiteEffect(user, 3 + ability.getCurrentRank(), user.getAttributes().getIntelligence().getEffectiveValue() / 2);
        effect.apply(target);
        setRemainingCooldown(7 - ability.getCurrentRank());
        target.afterAttacked(user);
        if (user.isDead()) return;
        user.afterAttacking(target);
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
    public int getPriority() {
        return user.getAttributes().getStrength().getEffectiveValue() / 2 + (user.getAttributes().getIntelligence().getEffectiveValue() * ability.getCurrentRank()) / 2;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) return String.format("Viper's Bite (%d)", cooldown);
        else return "Viper's Bite";
    }
}
