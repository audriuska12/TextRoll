package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.effects.RiposteEffect;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class Riposte extends ActiveAbility {
    public Riposte(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new RiposteAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "Riposte";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Riposte (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Block next enemy attack or spell targeted at you and deal %d damage to the caster.\nCooldown: %d", ((2 + getCurrentRank()) * (actor.getAttributes().getSpeed().getEffectiveValue() + 2 * actor.getAttributes().getIntelligence().getEffectiveValue())) / 5, 7 - getCurrentRank() / 3);
    }
}

class RiposteAction extends Action implements Cooldown {

    private ActiveAbility ability;
    private int cooldown;

    public RiposteAction(ActiveAbility ability, Actor user) {
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
        user.beforeCasting(user);
        if (user.isDead()) return;
        RiposteEffect effect = new RiposteEffect(((2 + ability.getCurrentRank()) * (user.getAttributes().getSpeed().getEffectiveValue() + 2 * user.getAttributes().getIntelligence().getEffectiveValue())) / 5);
        effect.apply(user);
        cooldown = 7 - ability.getCurrentRank() / 3;
        user.afterCasting(user);
    }

    @Override
    public boolean isAvailable() {
        return cooldown == 0;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return actor == target;
    }

    @Override
    public int getPriority() {
        return (user.getCurrentHealth() <= (user.getMaximumHealth() * 3) / 2) ? (user.getAttributes().getSpeed().getEffectiveValue() + user.getAttributes().getIntelligence().getEffectiveValue()) : (1);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Riposte (%d)", cooldown);
        } else {
            return "Riposte";
        }
    }
}
