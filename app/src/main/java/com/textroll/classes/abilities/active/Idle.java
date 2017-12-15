package com.textroll.classes.abilities.active;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;

public class Idle extends ActiveAbility {
    public Idle(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.setAction(new IdleAction(actor));
    }

    @Override
    public String getFirebaseName() {
        return "Idle";
    }

    @Override
    public String getStatName() {
        return "Idle";
    }
}

class IdleAction extends Action {

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
