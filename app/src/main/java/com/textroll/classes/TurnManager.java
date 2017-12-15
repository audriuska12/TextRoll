package com.textroll.classes;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Enemy;
import com.textroll.menus.CombatActivity;

import java.util.ArrayList;
import java.util.Collection;

public class TurnManager implements Runnable {
    private ArrayList<Actor> characters;
    private CombatActivity combat;
    private boolean isKilled;
    private Actor current;

    public TurnManager(Collection<? extends Actor> characters, CombatActivity combat) {
        this.characters = new ArrayList<>();
        this.characters.addAll(characters);
        this.combat = combat;
        this.current = this.characters.get(0);
    }

    public void kill() {
        isKilled = true;
    }

    public void processDeath(Actor c) {
        log(String.format("%s dies!\n", c.getName()));
        combat.processDeath(c);
    }

    public void run() {
        while (!isKilled && fightOngoing()) {
            int maxEnergy = 0;
            for (int i = 0; i < characters.size(); i++) {
                Actor tmp = characters.get(i);
                tmp.addEnergy(tmp.getAttributes().getSpeed().getEffectiveValue());
                if (tmp.getEnergy() > maxEnergy) {
                    maxEnergy = tmp.getEnergy();
                    current = tmp;
                }
            }
            current.startTurn();
            if (current == Instances.pc) {
                combat.PlayerTurnStart();
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
                current.endTurn();
                current.setEnergy(0);
                combat.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        combat.refreshHealthBars();
                    }
                });
            }

        }
        if (!isKilled) {
            if (playerIsAlive()) {
                log("You win! \n");
                log(String.format("Gained %d character points and %d gold!\n", Instances.encounters.getCurrentEncounter().getRewardCP(), Instances.encounters.getCurrentEncounter().getRewardG()));
                Instances.pc.addCharacterPoints(Instances.encounters.getCurrentEncounter().getRewardCP());
                Instances.pc.addGold(Instances.encounters.getCurrentEncounter().getRewardG());
                Instances.encounters.next();
                combat.win();
            } else {
                log("You lose... \n");
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
