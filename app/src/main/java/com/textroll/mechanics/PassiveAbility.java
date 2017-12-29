package com.textroll.mechanics;

import java.io.Serializable;

public abstract class PassiveAbility implements Ability, Serializable {
    protected Actor actor;
    protected int currentRank;
    protected int maxRank;

    public PassiveAbility(Actor actor, int maxRank, int currentRank) {
        this.actor = actor;
        this.maxRank = maxRank;
        this.currentRank = currentRank;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public int onTakeDamage(int damage, Actor source) {
        return damage;
    }

    public boolean onDying() {
        return true;
    }

    void onDeath() {
    }

    void onStartOfCombat() {
    }

    void onTurnStart() {
    }

    public void onTurnEnd() {
    }

    void onEndOfCombat() {
    }

    int onReceiveHealing(int healing, Actor source) {
        return healing;
    }

    @Override
    public void setMaxRank(int rank) {
        if (rank > 0) this.maxRank = rank;
        if (currentRank > maxRank) currentRank = maxRank;
    }

    @Override
    public void changeRank(int change) {
        if (currentRank + change >= 0) currentRank = Math.min(currentRank + change, maxRank);
    }

    @Override
    public int getCurrentRank() {
        return currentRank;
    }

    @Override
    public void setCurrentRank(int rank) {
        if (rank >= 0 && rank <= maxRank) this.currentRank = rank;
    }

    @Override
    public int getMaximumRank() {
        return maxRank;
    }

    public void beforeAttacking(Actor target) {
    }

    public void afterAttacking(Actor target) {
    }

    public void beforeAttacked(Actor attacker) {
    }

    public void afterAttacked(Actor attacker) {
    }
}
