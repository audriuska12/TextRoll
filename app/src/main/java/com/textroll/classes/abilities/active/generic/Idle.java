package com.textroll.classes.abilities.active.generic;

import com.google.firebase.database.DatabaseReference;
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
