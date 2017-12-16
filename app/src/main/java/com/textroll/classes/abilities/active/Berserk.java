package com.textroll.classes.abilities.active;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;
import com.textroll.mechanics.Effect;
import com.textroll.classes.Instances;

public class Berserk extends ActiveAbility {
    public Berserk(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.setAction(new BerserkAction(this, actor));
    }

    @Override
    public String getFirebaseName() {
        return "Berserk";
    }

    @Override
    public String getStatName() {
        return String.format("Berserk (%d/%d)", getCurrentRank(), getMaximumRank());
    }
}

class BerserkAction extends Action implements Cooldown{

    private ActiveAbility ability;
    private int cooldown = 0;

    public BerserkAction(ActiveAbility ability, Actor actor) {
        this.ability = ability;
        this.user = actor;
    }
    @Override
    public void execute() {
        BerserkEffect effect = new BerserkEffect(5 * ability.getCurrentRank(), ability.getCurrentRank() / 2 + 2);
        effect.apply(user);
        setRemainingCooldown(4);
    }

    @Override
    public boolean isAvailable() {
        return (cooldown == 0);
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
    public int getPriority() {
        if (user.getCurrentHealth() < user.getMaximumHealth() / 2) {
            return user.getAttributes().getStrength().getEffectiveValue() + 5 * this.ability.getCurrentRank();
        } else return 1;
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
    int power;

    public BerserkEffect(int power, int duration) {
        this.duration = duration;
        this.power = power;
    }
    @Override
    public void apply(Actor target) {
        actor = target;
        actor.getAttributes().getStrength().modifyBonus(power);
        actor.getEffects().add(this);
        Instances.turnManager.log(String.format("%s enrages for %d turns and gains %d strength!\n", actor.getName(), duration, power));
    }

    @Override
    public void remove() {
        actor.getAttributes().getStrength().modifyBonus(-power);
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