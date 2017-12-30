package com.textroll.classes.abilities.active;

import android.annotation.SuppressLint;

import com.textroll.classes.effects.ArcaneBarrierEffect;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Cooldown;

public class ArcaneBarrier extends ActiveAbility {
    public ArcaneBarrier(Actor actor, int maxRank, int currentRank) {
        super(actor, maxRank, currentRank);
        this.action = new ArcaneBarrierAction(this, actor);
    }

    @Override
    public String getFirebaseName() {
        return "ArcaneBarrier";
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getStatName() {
        return String.format("Arcane Barrier (%d/%d)", getCurrentRank(), getMaximumRank());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getDescription() {
        return String.format("Gain a shield for %d turns or %d damage.\nCooldown: %d", 3 + getCurrentRank() / 2, ((actor.getAttributes().getMagic().getEffectiveValue() + actor.getAttributes().getIntelligence().getEffectiveValue() / 2) * (2 + getCurrentRank())) / 3, 8 - getCurrentRank() / 2);
    }
}

class ArcaneBarrierAction extends Action implements Cooldown {

    private int cooldown;
    private ActiveAbility ability;

    public ArcaneBarrierAction(ActiveAbility ability, Actor user) {
        this.ability = ability;
        this.user = user;
    }

    @Override
    public void execute() {
        user.beforeCasting(user);
        if (user.isDead()) return;
        if (!user.beforeSpellHit(user)) return;
        if (user.isDead()) return;
        ArcaneBarrierEffect effect = new ArcaneBarrierEffect(3 + ability.getCurrentRank() / 2, (user.getAttributes().getMagic().getEffectiveValue() + user.getAttributes().getIntelligence().getEffectiveValue() / 2) * (2 + ability.getCurrentRank()) / 3);
        effect.apply(target);
        this.cooldown = 8 - ability.getCurrentRank() / 2;
        if (user.isDead()) return;
        user.afterSpellHit(user);
        if (user.isDead()) return;
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
        return user.getAttributes().getMagic().getEffectiveValue() + (user.getCurrentHealth() < user.getMaximumHealth() / 2 ? 10 : 0);
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

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if (cooldown > 0) {
            return String.format("Arcane Barrier (%d)", getRemainingCooldown());
        } else {
            return "Arcane Barrier";
        }
    }
}
