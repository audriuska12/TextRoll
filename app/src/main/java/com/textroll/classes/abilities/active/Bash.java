package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.effects.BashEffect;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class Bash extends ActiveAbility {
    public Bash(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new BashAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "Bash";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Bash (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Deal %d damage to an enemy and gain %d%% of your END as STR for your next attack.\nCooldown: %d", getCurrentRank() * (actor.getAttributes().getStrength().getEffectiveValue() + actor.getAttributes().getEndurance().getEffectiveValue()) / 2, 10 * getCurrentRank(), 6 - getCurrentRank() / 2);
    }
}

class BashAction extends Action implements Cooldown {
    private int cooldown = 0;
    private ActiveAbility ability;

    BashAction(ActiveAbility ability, Actor user) {
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
        int dmgDealt = (ability.getCurrentRank() * (user.getAttributes().getStrength().getEffectiveValue() + user.getAttributes().getEndurance().getEffectiveValue())) / 2;
        target.takeDamage(dmgDealt, user);
        BashEffect effect = new BashEffect((10 * ability.getCurrentRank() * user.getAttributes().getEndurance().getEffectiveValue()) / 100);
        effect.apply(user);
        target.afterAttacked(user);
        setRemainingCooldown(6 - ability.getCurrentRank() / 2);
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
        return user.getAttributes().getStrength().getEffectiveValue() * 2 + 5;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Bash (%d)", getRemainingCooldown());
        } else return "Bash";
    }
}