package com.textroll.mechanics;

import java.util.ArrayList;


public abstract class EncounterChain {
    private ArrayList<Encounter> encounters;
    private int currentEncounter = 0;

    protected EncounterChain() {
        this.encounters = new ArrayList<>();
    }

    public EncounterChain(ArrayList<Encounter> encounters) {
        this.encounters = encounters;
    }

    public Encounter getCurrentEncounter() {
        return encounters.get(currentEncounter);
    }

    public boolean next() {
        if (hasCurrentEncounter()) {
            currentEncounter++;
            return true;
        } else return false;
    }

    protected void addEncounter(Encounter encounter) {
        encounters.add(encounter);
    }

    public boolean hasNextEncounter() {
        return currentEncounter < encounters.size() - 1;
    }

    public boolean hasCurrentEncounter() {
        return currentEncounter < encounters.size();
    }
}
