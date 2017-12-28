package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;
import com.textroll.mechanics.Effect;

/**
 * Created by audri on 2017-12-28.
 */

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
        return String.format("Deal %d damage to all enemies, as well as igniting target enemy, stunning them for %d turns and dealing %d damage per turn.", (actor.getAttributes().getMagic().getEffectiveValue() * (2 + getCurrentRank())) / 5, 1 + getCurrentRank() / 5, actor.getAttributes().getMagic().getEffectiveValue() / 3);
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
        int damageDealt = (user.getAttributes().getMagic().getEffectiveValue() * (2 + ability.getCurrentRank())) / 5;
        for (Actor a : getAvailableTargets()) {
            a.takeDamage(damageDealt, user);
            if (a == target) {
                FireballEffect effect = new FireballEffect(user, 1 + ability.getCurrentRank() / 5, user.getAttributes().getMagic().getEffectiveValue() / 3);
                effect.apply(a);
            }
        }
        cooldown = 5;
    }

    @Override
    public boolean isAvailable() {
        return cooldown == 0;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return (!actor.getClass().equals(target.getClass()));
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

class FireballEffect extends Effect {

    Actor user;
    int duration;
    int magnitude;

    public FireballEffect(Actor user, int duration, int magnitude) {
        this.user = user;
        this.duration = duration;
        this.magnitude = magnitude;
    }

    @Override
    protected void onApply() {
        actor.addStun();
    }

    @Override
    protected void onRemove() {
        actor.removeStun();
    }

    @Override
    public void onTurnStart() {
    }

    @Override
    public void onTurnEnd() {
        actor.takeDamage(magnitude, user);
        duration--;
        if (duration <= 0) remove();
    }
}