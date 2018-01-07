package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;


public class EncounterChain {
    private ArrayList<Encounter> encounters;
    private int currentEncounter = 0;
    private String name;
    private String key;
    private boolean repeatable = false;
    private String description;

    protected EncounterChain() {
        this.encounters = new ArrayList<>();
    }

    protected EncounterChain(String name) {
        this.name = name;
        this.encounters = new ArrayList<>();
    }

    public EncounterChain(DataSnapshot snapshot) {
        this();
        this.key = snapshot.getKey();
        this.name = (String) (snapshot.child("name").getValue());
        this.repeatable = (Integer.valueOf((String) snapshot.child("repeatable").getValue()) == 1);
        this.description = (String) snapshot.child("description").getValue();
        for (DataSnapshot encounter : snapshot.child("encounters").getChildren()) {
            addEncounter(new Encounter(encounter));
        }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCurrentEncounterId() {
        return currentEncounter;
    }
}
