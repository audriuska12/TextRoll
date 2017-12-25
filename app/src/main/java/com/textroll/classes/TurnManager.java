package com.textroll.classes;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Enemy;
import com.textroll.menus.CombatActivity;

import java.util.ArrayList;
import java.util.Collection;

public class TurnManager implements Runnable {
    private ArrayList<Actor> actors;
    private CombatActivity combat;
    private boolean isKilled;
    private Actor current;

    public TurnManager(Collection<? extends Actor> characters, CombatActivity combat) {
        this.actors = new ArrayList<>();
        this.actors.addAll(characters);
        this.combat = combat;
        this.current = this.actors.get(0);
    }

    public void kill() {
        isKilled = true;
    }

    public void processDeath(Actor c) {
        log(String.format("%s dies!\n", c.getName()));
        combat.processDeath(c);
        actors.remove(c);
    }

    public void run() {
        while (!isKilled && fightOngoing()) {
            int maxEnergy = 0;
            for (int i = 0; i < actors.size(); i++) {
                Actor tmp = actors.get(i);
                tmp.addEnergy(tmp.getAttributes().getSpeed().getEffectiveValue());
                if (tmp.getEnergy() > maxEnergy) {
                    maxEnergy = tmp.getEnergy();
                    current = tmp;
                }
            }
            combat.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    combat.refreshViews();
                }
            });
            current.startTurn();
            combat.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    combat.refreshViews();
                }
            });
            if (current.isDead()) continue;
            if (current == Instances.pc) {
                combat.PlayerTurnStart();
                combat.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        combat.refreshViews();
                    }
                });
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            Action action = current.takeAction();
            if (action != null) {
                action.execute();
                combat.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        combat.refreshViews();
                    }
                });
                if (current.isDead()) continue;
                current.endTurn();
                current.setEnergy(0);
                combat.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        combat.refreshViews();
                    }
                });
            }

        }
        if (!isKilled) {
            if (playerIsAlive()) {
                combat.win();
            } else {
                combat.lose();
            }
        }
    }

    public void log(final String text) {
        combat.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                combat.log(text);
            }
        });
    }


    public boolean fightOngoing() {
        return (!isKilled && playerIsAlive() && anyEnemiesAlive());
    }

    public boolean playerIsAlive() {
        return !Instances.pc.isDead();
    }

    public boolean anyEnemiesAlive() {
        for (Enemy e : Instances.enemies) {
            if (!e.isDead()) return true;
        }
        return false;
    }

}
