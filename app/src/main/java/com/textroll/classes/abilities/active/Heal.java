package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class Heal extends ActiveAbility {
    public Heal(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new HealAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "Heal";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Heal (%d/%d", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Restore %d health to target ally.\nCooldown: %d", actor.getAttributes().getIntelligence().getEffectiveValue() + (actor.getAttributes().getMagic().getEffectiveValue() * 2 + getCurrentRank()) / 4, 4);
    }
}

class HealAction extends Action implements Cooldown {

    private ActiveAbility ability;
    private int cooldown;

    public HealAction(ActiveAbility ability, Actor user) {
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
        user.beforeCasting(target);
        if (user.isDead() || target.isDead()) return;
        int healing = user.getAttributes().getIntelligence().getEffectiveValue() + (user.getAttributes().getMagic().getEffectiveValue() * 2 + ability.getCurrentRank()) / 4;
        target.heal(healing, user);
        setRemainingCooldown(4);
        user.afterCasting(target);
    }

    @Override
    public boolean isAvailable() {
        return cooldown <= 0;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return actor.getFaction() == target.getFaction();
    }

    @Override
    public int getPriority() {
        for (Actor a : getAvailableTargets()) {
            if (a.getCurrentHealth() <= a.getMaximumHealth() / 2)
                return user.getAttributes().getMagic().getEffectiveValue() * 2;
        }
        return 1;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Heal (%d)", cooldown);
        } else {
            return "Heal";
        }
    }
}
