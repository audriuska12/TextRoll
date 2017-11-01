package com.textroll.classes;

import com.textroll.classes.Abilities.Active.Generic.BasicAttack;
import com.textroll.classes.Abilities.Active.Player.Berserk;

import java.util.ArrayList;

/**
 * Created by audri on 2017-09-23.
 */

public class Player extends Actor {

    private Action nextAction;

    public Player(String name){
        super(name);
        attributes.getStrength().setBase(10);
        attributes.getEndurance().setBase(10);
        addAbility(new Berserk(this));
        refresh();
    }

    public void setNextAction(Action action){
        this.nextAction = action;
    }

    @Override
    public Action takeAction() {
        Action action = nextAction;
        nextAction = null;
        return action;
    }
}
