package com.textroll.mechanics;

import com.textroll.classes.abilities.active.generic.BasicAttack;
import com.textroll.classes.abilities.active.player.Berserk;

public class Player extends Actor {

    private Action nextAction;

    public Player(String name){
        super(name);
        attributes.getStrength().setBase(10);
        attributes.getEndurance().setBase(10);
        addAbility(new BasicAttack(this));
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
