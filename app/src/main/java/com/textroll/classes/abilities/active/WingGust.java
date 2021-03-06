package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.effects.GenericStunEffect;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class WingGust extends ActiveAbility {
    public WingGust(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new WingGustAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "WingGust";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Wing Gust (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Deal %d damage to all enemies and stun them.\nCooldown: %d", ((actor.getAttributes().getStrength().getEffectiveValue() + actor.getAttributes().getEndurance().getEffectiveValue()) * (2 + getCurrentRank())) / 3, 5);
    }
}

class WingGustAction extends Action implements Cooldown {
    private int cooldown = 0;
    private ActiveAbility ability;

    WingGustAction(ActiveAbility ability, Actor user) {
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
        if (user.isDead()) return;
        int damage = ((user.getAttributes().getStrength().getEffectiveValue() + user.getAttributes().getEndurance().getEffectiveValue()) * (2 + ability.getCurrentRank())) / 3;
        for (Actor a : getAvailableTargets()) {
            a.takeDamage(damage, user);
            GenericStunEffect effect = new GenericStunEffect(1);
            effect.apply(a);
        }
        setRemainingCooldown(5);
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
        return user.getAttributes().getStrength().getEffectiveValue() * getAvailableTargets().size() + 3 * ability.getCurrentRank();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Wing Gust (%d)", cooldown);
        } else return "Wing Gust";
    }
}