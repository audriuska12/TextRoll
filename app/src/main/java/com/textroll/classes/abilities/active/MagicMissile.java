package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;

/**
 * Created by audri on 2017-12-25.
 */

public class MagicMissile extends ActiveAbility {
    public MagicMissile(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new MagicMissileAction(actor);
    }

    @Override
    public String getFirebaseName() {
        return "MagicMissile";
    }

    @Override
    public String getStatName() {
        return "Magic Missile";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Deal %d damage to a single enemy", actor.getAttributes().getMagic().getEffectiveValue());
    }
}

class MagicMissileAction extends Action {
    public MagicMissileAction(Actor actor) {
        this.user = actor;
    }

    @Override
    public void execute() {
        user.beforeCasting(target);
        if (!target.beforeSpellHit(user)) return;
        int damageDealt = user.getAttributes().getMagic().getEffectiveValue();
        target.takeDamage(damageDealt, user);
        target.afterSpellHit(user);
        if (user.isDead()) return;
        user.afterCasting(target);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return (!actor.getClass().equals(target.getClass()));
    }

    @Override
    public int getPriority() {
        return user.getAttributes().getMagic().getEffectiveValue();
    }

    @Override
    public String toString() {
        return "Magic Missile";
    }

    @Override
    public int getThreat(Actor actor) {
        return actor.getThreat();
    }
}
