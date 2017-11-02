package com.textroll.classes.abilities.active.generic;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;

public class Idle extends ActiveAbility {
    public Idle(Actor actor) {
        this.setMaxRank(1);
        this.setCurrentRank(1);
        this.setAction(new IdleAction(actor));
    }
}

class IdleAction extends Action {
    private Actor actor;

    public IdleAction(Actor actor) {
        this.actor = actor;
    }

    @Override
    public void execute() {
        Instances.turnManager.log(String.format("%s idles.\n", actor.getName()));
    }

    @Override
    public boolean isAvailable(Actor actor, Actor target) {
        return true;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return true;
    }
}
