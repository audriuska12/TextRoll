package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;
import com.textroll.mechanics.SummonedCreature;

public class SummonManaElemental extends ActiveAbility {
    public SummonManaElemental(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new SummonManaElementalAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "SummonManaElemental";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Summon Mana Elemental (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Summon a Mana Elemental for %d turns. Increasing the ability's rank increases the elemental's power.\nCooldown: %d", 5, 10);
    }
}

class SummonManaElementalAction extends Action implements Cooldown {

    private int cooldown = 0;
    private ActiveAbility ability;

    SummonManaElementalAction(ActiveAbility ability, Actor user) {
        this.ability = ability;
        this.user = user;
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

    @Override
    public void execute() {
        user.beforeCasting(user);
        if (user.isDead()) return;
        SummonedCreature summon = new SummonedCreature(Instances.summonSnap.child("ManaElemental0" + ability.getCurrentRank()), user, 5, true, user.getFaction());
        Instances.turnManager.addCharacter(summon);
        setRemainingCooldown(10);
        user.afterCasting(user);
    }

    @Override
    public boolean isAvailable() {
        return cooldown <= 0;
    }

    @Override
    public boolean validForTarget(Actor actor, Actor target) {
        return actor == target;
    }

    @Override
    public int getPriority() {
        return 15 * ability.getCurrentRank() + 10;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Summon Mana Elemental (%d)", cooldown);
        } else {
            return "Summon Mana Elemental";
        }
    }
}
