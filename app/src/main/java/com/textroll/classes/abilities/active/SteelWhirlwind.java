package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class SteelWhirlwind extends ActiveAbility {

    public SteelWhirlwind(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new SteelWhirlwindAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "SteelWhirlwind";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Steel Whirlwind (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Deal %d damage to all enemies, increased by %d if your health is 50%% or lower.\nCooldown: %d", (3 * actor.getAttributes().getStrength().getEffectiveValue()) / 4, ((2 + getCurrentRank()) * actor.getAttributes().getStrength().getEffectiveValue()) / 4, 6 - getCurrentRank() / 2);
    }
}

class SteelWhirlwindAction extends Action implements Cooldown {
    private ActiveAbility ability;
    private int cooldown = 0;

    public SteelWhirlwindAction(ActiveAbility ability, Actor user) {
        this.ability = ability;
        this.user = user;
    }

    @Override
    public void execute() {
        user.beforeCasting(target);
        if (user.isDead()) return;
        int damageDealt = (user.getCurrentHealth() > user.getMaximumHealth() / 2) ? ((3 * user.getAttributes().getStrength().getEffectiveValue()) / 4) : ((3 * user.getAttributes().getStrength().getEffectiveValue()) / 4 + ((2 + ability.getCurrentRank()) * user.getAttributes().getStrength().getEffectiveValue()) / 4);
        for (Actor a : getAvailableTargets()) {
            a.takeDamage(damageDealt, user);
            a.afterSpellHit(user);
            if (user.isDead()) break;
        }
        setRemainingCooldown(6 - ability.getCurrentRank() / 2);
        if (user.isDead()) return;
        user.afterCasting(target);
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
        int priority = (user.getCurrentHealth() <= user.getMaximumHealth() / 2) ? (user.getAttributes().getStrength().getEffectiveValue() * 3) / 2 : (user.getAttributes().getStrength().getEffectiveValue() * 3) / 4;
        return priority * getAvailableTargets().size();
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
        if (cooldown > 0) {
            return String.format("Steel Whirlwind (%d)", getRemainingCooldown());
        } else {
            return "Steel Whirlwind";
        }
    }
}