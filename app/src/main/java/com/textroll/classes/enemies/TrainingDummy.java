package com.textroll.classes.enemies;

import com.textroll.classes.abilities.active.generic.Idle;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.Enemy;

public class TrainingDummy extends Enemy {
    public TrainingDummy() {
        super("Training Dummy");
        this.abilities.clear();

        this.addAbility(new Idle(this));
        this.updateAvailableActions(this);
    }

    @Override
    public Action takeAction() {
        return actions.get(0);
    }
}
