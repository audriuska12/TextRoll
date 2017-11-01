package com.textroll.classes.Abilities.Active.Generic;

import com.textroll.classes.Action;
import com.textroll.classes.ActiveAbility;
import com.textroll.classes.Actor;
import com.textroll.classes.Enemy;
import com.textroll.classes.Instances;
import com.textroll.classes.Player;

/**
 * Created by audri on 2017-10-22.
 */

public class BasicAttack extends ActiveAbility {
    public BasicAttack(Actor actor){
        this.setMaxRank(1);
        this.setCurrentRank(1);
        this.setAction(new BasicAttackAction(actor));
    }
}

class BasicAttackAction extends Action{

    public BasicAttackAction(Actor actor){
        this.user = actor;
    }

    public boolean execute(){
        int dmgDealt = user.getAttributes().getStrength().getEffectiveValue();
        target.takeDamage(dmgDealt);
        Instances.turnManager.log(String.format("%s dealt %d damage to %s! \n", user.getName(), dmgDealt, target.getName()));
        return true;
    }

    @Override
    public boolean isAvailable(Actor actor, Actor target) {
        return validForTarget(actor, target);
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return((actor instanceof Player && target instanceof Enemy)||(actor instanceof Enemy && target instanceof Player));
    }

    @Override
    public String toString(){
        return "Basic Attack";
    }
}
