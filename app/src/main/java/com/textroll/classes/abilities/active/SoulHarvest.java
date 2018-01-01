package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.effects.SoulHarvestBuff;
import com.textroll.classes.effects.SoulHarvestDebuff;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class SoulHarvest extends ActiveAbility {
    public SoulHarvest(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new SoulHarvestAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "SoulHarvest";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Soul Harvest (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Deal %d damage to all other characters and steal %d STR, SPD, INT and MAG from them, then heal for %d per target.\nCooldown: %d", actor.getAttributes().getMagic().getEffectiveValue() / 2, getCurrentRank(), actor.getAttributes().getMagic().getEffectiveValue() / 5, 5);
    }
}

class SoulHarvestAction extends Action implements Cooldown {

    private int cooldown;
    private ActiveAbility ability;

    public SoulHarvestAction(ActiveAbility ability, Actor user) {
        this.ability = ability;
        this.user = user;
    }

    @Override
    public void execute() {
        user.beforeCasting(target);
        if (user.isDead()) return;
        int damageTaken = user.getAttributes().getMagic().getEffectiveValue() / 2;
        int count = 0;
        for (Actor a : getAvailableTargets()) {
            a.takeDamage(damageTaken, user);
            SoulHarvestDebuff debuff = new SoulHarvestDebuff(ability.getCurrentRank());
            debuff.apply(a);
            SoulHarvestBuff buff = new SoulHarvestBuff(ability.getCurrentRank());
            buff.apply(user);
            count++;
        }
        user.heal((count * user.getAttributes().getMagic().getEffectiveValue() / 5), user);
        if (user.isDead()) return;
        cooldown = 5;
        user.afterCasting(target);
    }

    @Override
    public boolean isAvailable() {
        return cooldown <= 0;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return actor != target;
    }

    @Override
    public int getPriority() {
        return user.getAttributes().getMagic().getEffectiveValue() * 5;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Soul Harvest (%d)", cooldown);
        } else {
            return "Soul Harvest";
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
    public int getThreat(Actor target) {
        return (user.getClass().equals(target.getClass()) ? 0 : target.getThreat());
    }

    @Override
    public void coolDown() {
        if (cooldown > 0) cooldown--;
    }
}