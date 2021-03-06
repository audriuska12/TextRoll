package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.actions.StunnedAction;

import java.util.List;

public class Enemy extends Actor {
    public Enemy(String name){
        super(name);
        refresh();
        this.faction = Faction.ENEMY;
    }

    public Enemy(DataSnapshot snapshot) {
        super(snapshot);
        refresh();
        this.faction = Faction.ENEMY;
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

}

