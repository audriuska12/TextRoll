package com.textroll.classes.abilities.passive;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;
import com.textroll.mechanics.PassiveAbility;

public class Resurrect extends PassiveAbility implements Cooldown {
    private int cooldown;

    public Resurrect(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
    }

    @Override
    public boolean onDying() {
        if (cooldown == 0) {
            Instances.turnManager.log(String.format("%s revives.\n", actor));
            actor.heal((actor.getMaximumHealth() / 5) * currentRank, actor);
            cooldown = 20 - 2 * currentRank;
            return actor.getCurrentHealth() <= 0;
        } else return true;
    }

    @Override
    public String getFirebaseName() {
        return "Resurrect";
    }

    @Override
    public String getStatName() {
        return "Resurrect";
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
        if (cooldown > 0) cooldown--;
    }
}
