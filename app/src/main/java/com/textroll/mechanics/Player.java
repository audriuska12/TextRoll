package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.textroll.classes.Instances;
import com.textroll.classes.abilities.active.BasicAttack;
import com.textroll.classes.abilities.active.Idle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player extends Actor {

    private Action nextAction;
    private int characterPoints;
    private int gold = 0;
    private HashMap<String, QuestEntry> quests;

    public Player(String name){
        super(name);
        addAbility(new BasicAttack(this, 1, 1));
        this.addAbility(new Idle(this, 1, 1));
        this.quests = new HashMap<>();
        refresh();
    }

    public Player(DataSnapshot snapshot) {
        super(snapshot);
        this.characterPoints = Integer.valueOf((String) (snapshot.child("CP").getValue()));
        this.gold = Integer.valueOf((String) (snapshot.child("gold")).getValue());
        this.quests = new HashMap<>();
        for (DataSnapshot snapQuest : snapshot.child("log").getChildren()) {
            QuestEntry q = new QuestEntry();
            q.key = (String) snapQuest.getKey();
            q.completed = (Integer.valueOf((String) snapQuest.child("completed").getValue()) == 1);
            quests.put(q.key, q);
        }
    }

    public int getCharacterPoints() {
        return characterPoints;
    }

    public void setCharacterPoints(int characterPoints) {
        this.characterPoints = characterPoints;
    }

    public void addCharacterPoints(int points) {
        this.characterPoints += points;
    }

    public boolean removeCharacterPoints(int points) {
        if (this.characterPoints >= points) {
            characterPoints -= points;
            return true;
        }
        return false;
    }

    public void setNextAction(Action action){
        this.nextAction = action;
    }

    @Override
    public void saveToFirebase() {
        super.saveToFirebase();
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("CP").setValue(String.valueOf(this.characterPoints));
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("gold").setValue(String.valueOf(this.gold));
        for (Map.Entry<String, QuestEntry> qe : quests.entrySet()) {
            QuestEntry q = qe.getValue();
            DatabaseReference ref = Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("log").child(q.key);
            ref.child("completed").setValue(String.valueOf((q.completed) ? 1 : 0));
        }
    }

    @Override
    public Action takeAction() {
        Action action = nextAction;
        nextAction = null;
        return action;
    }

    public void deleteFromDatabase() {
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).setValue(null);
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public boolean removeGold(int gold) {
        if (this.gold >= gold) {
            this.gold -= gold;
            return true;
        }
        return false;
    }

    public HashMap<String, QuestEntry> getQuests() {
        return quests;
    }

    public void setQuests(HashMap<String, QuestEntry> quests) {
        this.quests = quests;
    }
}
