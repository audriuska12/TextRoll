package com.textroll.classes.Abilities.Active.Player;

import com.textroll.classes.Action;
import com.textroll.classes.ActiveAbility;
import com.textroll.classes.Actor;
import com.textroll.classes.Cooldown;
import com.textroll.classes.Effect;
import com.textroll.classes.Instances;

/**
 * Created by audri on 2017-10-30.
 */

public class Berserk extends ActiveAbility {
    public Berserk(Actor actor){
        this.setMaxRank(1);
        this.setCurrentRank(1);
        this.setAction(new BerserkAction(actor));
    }
}

class BerserkAction extends Action implements Cooldown{

    private Actor actor;
    private int cooldown = 0;
    public BerserkAction(Actor actor){
        this.actor = actor;
    }
    @Override
    public boolean execute() {
        BerserkEffect effect = new BerserkEffect();
        effect.apply(actor);
        setRemainingCooldown(4);
        return true;
    }

    @Override
    public boolean isAvailable(Actor actor, Actor target) {
        return (cooldown == 0 && validForTarget(actor, target));
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return actor == target;
    }

    @Override
    public int getRemainingCooldown() {
        return cooldown;
    }

    @Override
    public void setRemainingCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public void coolDown() {
        if(cooldown > 0) cooldown--;
    }

    @Override
    public String toString(){
        if(cooldown > 0){
            return String.format("Berserk (%d)", cooldown);
        } else
        return "Berserk";
    }
}

class BerserkEffect extends Effect{

    Actor actor;
    int duration;
    @Override
    public void apply(Actor target) {
        actor = target;
        duration = 3;
        actor.getAttributes().getStrength().modifyBonus(10);
        actor.getEffects().add(this);
        Instances.turnManager.log(String.format("%s enrages and gains 10 strength!\n", actor.getName()));
    }

    @Override
    public void remove() {
        actor.getAttributes().getStrength().modifyBonus(-10);
        actor.getEffects().remove(this);
        Instances.turnManager.log(String.format("%s stops raging.\n", actor.getName()));
    }

    @Override
    public void onTurnStart() {

    }

    @Override
    public void onTurnEnd() {
        duration--;
        if (duration == 0) remove();
    }
}
