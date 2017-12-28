package com.textroll.classes.actions;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.Actor;

/**
 * Created by audri on 2017-12-28.
 */

public class StunnedAction extends Action {
    public StunnedAction(Actor actor) {
        this.user = actor;
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isAvailable() {
        return user.isStunned();
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return (actor == target && actor.isStunned());
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
