package com.textroll.classes.abilities.active;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Enemy;
import com.textroll.mechanics.Player;

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
}

class MagicMissileAction extends Action {
    public MagicMissileAction(Actor actor) {
        this.user = actor;
    }

    @Override
    public void execute() {
        int damageDealt = user.getAttributes().getMagic().getEffectiveValue();
        target.takeDamage(damageDealt, user);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return ((actor instanceof Player && target instanceof Enemy) || (actor instanceof Enemy && target instanceof Player));
    }

    @Override
    public int getPriority() {
        return user.getAttributes().getMagic().getEffectiveValue();
    }

    @Override
    public String toString() {
        return "Magic Missile";
    }
}
