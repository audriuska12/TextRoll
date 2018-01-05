package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.effects.DragonsBreathBurnEffect;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class DragonBreath extends ActiveAbility {
    public DragonBreath(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new DragonBreathAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "DragonBreath";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Dragon Breath (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Deal %d damage to all enemies and burn them for %d damage per turn for %d turns", actor.getAttributes().getStrength().getEffectiveValue(), actor.getAttributes().getEndurance().getEffectiveValue(), getCurrentRank());
    }
}

class DragonBreathAction extends Action implements Cooldown {
    private ActiveAbility ability;
    private int cooldown = 0;

    DragonBreathAction(ActiveAbility ability, Actor user) {
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
        int damage = user.getAttributes().getStrength().getEffectiveValue();
        for (Actor a : getAvailableTargets()) {
            a.takeDamage(damage, user);
            DragonsBreathBurnEffect effect = new DragonsBreathBurnEffect(user.getAttributes().getEndurance().getEffectiveValue(), ability.getCurrentRank(), user);
            effect.apply(a);
        }
        setRemainingCooldown(6);
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
        return 5 + getAvailableTargets().size() * (user.getAttributes().getStrength().getEffectiveValue() + user.getAttributes().getEndurance().getEffectiveValue() + ability.getCurrentRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Dragon Breath (%d)", cooldown);
        } else return "Dragon Breath";
    }
}
