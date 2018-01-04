package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.effects.FireballEffect;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class Fireball extends ActiveAbility {

    public Fireball(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new FireballAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "Fireball";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Fireball (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Deal %d damage to all enemies, as well as igniting target enemy, stunning them for %d turn(s) and dealing %d damage per turn.\nCooldown: %d", (actor.getAttributes().getMagic().getEffectiveValue() * (2 + getCurrentRank())) / 5, 1 + getCurrentRank() / 5, actor.getAttributes().getMagic().getEffectiveValue() / 3, 5);
    }
}

class FireballAction extends Action implements Cooldown {

    private ActiveAbility ability;
    private int cooldown = 0;

    public FireballAction(ActiveAbility ability, Actor actor) {
        this.ability = ability;
        this.user = actor;
    }

    @Override
    public void execute() {
        user.beforeCasting(target);
        if (user.isDead()) return;
        int damageDealt = (user.getAttributes().getMagic().getEffectiveValue() * (2 + ability.getCurrentRank())) / 5;
        if (target.beforeSpellHit(user)) {
            FireballEffect effect = new FireballEffect(user, 1 + ability.getCurrentRank() / 5, user.getAttributes().getMagic().getEffectiveValue() / 3);
            effect.apply(target);
        }
        for (Actor a : getAvailableTargets()) {
            a.takeDamage(damageDealt, user);
            a.afterSpellHit(user);
        }
        setRemainingCooldown(5);
        if (user.isDead()) return;
        user.afterCasting(user);
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
        return (user.getAttributes().getMagic().getEffectiveValue() + 3) * getAvailableTargets().size();
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
        if (cooldown > 0) return String.format("Fireball (%d)", cooldown);
        else return "Fireball";
    }

    @Override
    public int getThreat(Actor actor) {
        return actor.getThreat() - (actor.isStunned() ? 5 : 0);
    }
}

