package com.textroll.classes;

import com.textroll.mechanics.Action;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.Enemy;
import com.textroll.menus.CombatActivity;

import java.util.ArrayList;
import java.util.Collection;

public class TurnManager implements Runnable{
    private ArrayList<Actor> characters;
    private int currentActor;
    private CombatActivity combat;
    private boolean isKilled;
    private Actor current;
    public TurnManager(Collection<? extends Actor> characters, CombatActivity combat){
        currentActor = 0;
        this.characters = new ArrayList<>();
        this.characters.addAll(characters);
        this.combat = combat;
    }

    public void kill(){
        isKilled = true;
    }

    public void processDeath(Actor c){
        log(String.format("%s dies!\n", c.getName()));
        combat.processDeath(c);
    }
    public void run(){
        while(!isKilled && fightOngoing()){
            if(!characters.get(currentActor).isDead()){
                current = characters.get(currentActor);
                current.startTurn();
                if(characters.get(currentActor) == Instances.pc){
                    combat.PlayerTurnStart();
                    synchronized (this){
                        try {
                            wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
                Action action = characters.get(currentActor).takeAction();
                if(action != null) {
                    action.execute();
                    current.endTurn();
                    combat.runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            combat.refreshHealthBars();
                        }
                    });
                    currentActor = (currentActor + 1)%characters.size();}
            } else {currentActor = (currentActor + 1)%characters.size(); }

        }
        if(!isKilled) {
            if (playerIsAlive()) {
                log("You win! \n");
                Instances.encounters.next();
            } else {
                log("You lose... \n");
            }
        }
        if(!isKilled)combat.startExitTimer();
    }

    public void log(final String text){
        combat.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                combat.log(text);
            }
        });
    }

    public boolean fightOngoing(){
        return(!isKilled && playerIsAlive() && anyEnemiesAlive());
    }

    public boolean playerIsAlive(){
        return !Instances.pc.isDead();
    }

    public boolean anyEnemiesAlive(){
        for(Enemy e: Instances.enemies){
            if(!e.isDead()) return true;
        }
        return false;
    }

}
