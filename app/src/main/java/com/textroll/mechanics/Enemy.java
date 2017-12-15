package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.Instances;

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
        action.setTarget(action.getAvailableTargets().get(0));
        return action;
    }

}

