package com.textroll.classes.abilities.active;

import com.textroll.classes.actions.IdleAction;
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

    @Override
    public String getDescription() {
        return "Do nothing this turn.";
    }
}

