package com.textroll.classes.abilities.active.generic;

import com.google.firebase.database.DatabaseReference;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Enemy;
import com.textroll.classes.Instances;
import com.textroll.mechanics.Player;

public class BasicAttack extends ActiveAbility {

    public BasicAttack(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.setAction(new BasicAttackAction(actor));
    }

    public String getFirebaseName() {
        return "Basic Attack";
    }
}

class BasicAttackAction extends Action{

    public BasicAttackAction(Actor actor){
        this.user = actor;
    }

    public void execute() {
        int dmgDealt = user.getAttributes().getStrength().getEffectiveValue();
        target.takeDamage(dmgDealt);
        Instances.turnManager.log(String.format("%s dealt %d damage to %s! \n", user.getName(), dmgDealt, target.getName()));
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
