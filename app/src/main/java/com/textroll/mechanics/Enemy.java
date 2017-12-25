package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class Enemy extends Actor {
    public Enemy(String name){
        super(name);
        refresh();
    }

    public Enemy(DataSnapshot snapshot) {
        super(snapshot);
    }
    @Override
    public Action takeAction() {
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

