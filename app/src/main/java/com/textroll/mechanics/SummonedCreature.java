package com.textroll.mechanics;


import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.actions.StunnedAction;
import com.textroll.classes.effects.CreatureSummonEffect;

import java.util.List;

public class SummonedCreature extends Actor {
    private Actor summoner;
    private CreatureSummonEffect effect;

    public SummonedCreature(String name, Actor summoner, int duration, boolean terminating, Faction faction) {
        super(name);
        refresh();
        this.summoner = summoner;
        this.effect = new CreatureSummonEffect(this, duration, terminating);
        effect.apply(summoner);
        this.faction = faction;
    }

    public SummonedCreature(DataSnapshot snapshot, Actor summoner, int duration, boolean terminating, Faction faction) {
        super(snapshot);
        refresh();
        this.summoner = summoner;
        this.effect = new CreatureSummonEffect(this, duration, terminating);
        effect.apply(summoner);
        this.faction = faction;
    }

    public Actor getSummoner() {
        return summoner;
    }

    public void setSummoner(Actor summoner) {
        this.summoner = summoner;
    }

    public CreatureSummonEffect getEffect() {
        return effect;
    }

    public void setEffect(CreatureSummonEffect effect) {
        this.effect = effect;
    }

    @Override
    public Action takeAction() {
        if (stunCounter > 0) {
            return new StunnedAction(this);
        }
        updateAvailableActions();
        Action action = actions.get(0);
        int maxPriority = -1;
        for (Action ac : availableActions) {
            int priority = ac.getPriority();
            if (priority > maxPriority) {
                maxPriority = priority;
                action = ac;
            }
        }
        List<Actor> targets = action.getAvailableTargets();
        Actor target = targets.get(0);
        int maxThreat = action.getThreat(target);
        for (int i = 1; i < targets.size(); i++) {
            int threat = action.getThreat(targets.get(i));
            if (threat > maxThreat) {
                target = targets.get(i);
                maxThreat = threat;
            }
        }
        action.setTarget(target);
        return action;
    }

    @Override
    public void endTurn() {
        super.endTurn();
        effect.tickDuration();
    }
}
