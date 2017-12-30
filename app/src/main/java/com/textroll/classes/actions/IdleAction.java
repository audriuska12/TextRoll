package com.textroll.classes.actions;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.Actor;

/**
 * Created by audri on 2017-12-30.
 */
public class IdleAction extends Action {

    public IdleAction(Actor actor) {
        this.user = actor;
    }

    @Override
    public void execute() {
        Instances.turnManager.log(String.format("%s idles.\n", user.getName()));
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return actor == target;
    }

    @Override
    public int getPriority() {
        return -1;
    }

    @Override
    public String toString() {
        return "Idle";
    }
}
