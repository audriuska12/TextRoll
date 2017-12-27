package com.textroll.mechanics;

import com.textroll.classes.Instances;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Action implements Serializable {
    protected Actor user;
    protected Actor target;

    public Actor getUser() {
        return user;
    }

    public Actor getTarget() {
        return target;
    }

    public void setTarget(Actor target){
        this.target = target;
    }

    public abstract void execute();

    public abstract boolean isAvailable();
    public abstract boolean validForTarget(Actor actor, Actor target);

    public abstract int getPriority();

    List<Actor> getAvailableTargets() {
        ArrayList<Actor> targets = new ArrayList<>();
        ArrayList<Actor> actors = new ArrayList<>();
        actors.add(Instances.pc);
        actors.addAll(Instances.enemies);
        for (Actor t : actors) {
            if (validForTarget(user, t)) targets.add(t);
        }
        return targets;
    }

    public int getThreat(Actor actor) {
        return 0;
    }
}
