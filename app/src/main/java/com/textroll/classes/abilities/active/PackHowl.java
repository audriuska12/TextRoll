package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;
import com.textroll.mechanics.SummonedCreature;


public class PackHowl extends ActiveAbility {
    public PackHowl(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new PackHowlAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "PackHowl";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Howl of the Pack (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Summon %d Pack Wolves for %d turns.\nCooldown: %d", getCurrentRank(), 99, 10);
    }
}

class PackHowlAction extends Action implements Cooldown {

    private int cooldown = 0;
    private ActiveAbility ability;

    PackHowlAction(ActiveAbility ability, Actor actor) {
        this.ability = ability;
        this.user = actor;
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
        for (int i = 0; i < ability.getCurrentRank(); i++) {
            SummonedCreature summon = new SummonedCreature(Instances.summonSnap.child("WolfPack01"), user, 99, false, user.getFaction());
            Instances.turnManager.addCharacter(summon);
        }
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
        return 25 * ability.getCurrentRank();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Howl of the Pack (%d)", cooldown);
        } else {
            return "Howl of the Pack";
        }
    }
}
