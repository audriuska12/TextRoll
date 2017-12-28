package com.textroll.classes.abilities.active;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Enemy;
import com.textroll.mechanics.Player;

public class BasicAttack extends ActiveAbility {

    public BasicAttack(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.setAction(new BasicAttackAction(actor));
    }

    public String getFirebaseName() {
        return "BasicAttack";
    }

    @Override
    public String getStatName() {
        return "Basic Attack";
    }

    @Override
    public String getDescription() {
        return String.format("Attack your enemy, dealing %d damage", actor.getAttributes().getStrength().getEffectiveValue());
    }
}

class BasicAttackAction extends Action{

    BasicAttackAction(Actor actor) {
        this.user = actor;
    }

    public void execute() {
        int dmgDealt = user.getAttributes().getStrength().getEffectiveValue();
        target.takeDamage(dmgDealt, user);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return((actor instanceof Player && target instanceof Enemy)||(actor instanceof Enemy && target instanceof Player));
    }

    @Override
    public int getPriority() {
        return user.getAttributes().getStrength().getEffectiveValue();
    }

    @Override
    public int getThreat(Actor actor) {
        return actor.getThreat();
    }

    @Override
    public String toString(){
        return "Basic Attack";
    }
}
