package com.textroll.classes;

import com.textroll.classes.Abilities.Active.Generic.BasicAttack;

import java.util.ArrayList;

/**
 * Created by audri on 2017-09-23.
 */

public class Enemy extends Actor {
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

