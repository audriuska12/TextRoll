package com.textroll.mechanics;

import com.textroll.classes.Instances;

public abstract class Enemy extends Actor {
    public Enemy(String name){
        super(name);
        refresh();
    }

    @Override
    public Action takeAction() {
        updateAvailableActions(Instances.pc);
        Action action = actions.get(0);
        action.setTarget(Instances.pc);
        return action;
    }

}

