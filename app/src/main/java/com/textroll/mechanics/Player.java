package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.textroll.classes.Instances;
import com.textroll.classes.abilities.active.BasicAttack;
import com.textroll.classes.abilities.active.Idle;
import com.textroll.classes.actions.StunnedAction;

import java.util.HashMap;
import java.util.Map;

public class Player extends Actor {

    private Action nextAction;
    private int characterPoints;
    private int gold;
    private HashMap<String, QuestEntry> quests;
    private String currentQuestKey;
    private int currentQuestEncounterId;
    private String firebaseKey;

    public Player(String name) {
        super(name);
        addAbility(new BasicAttack(this, 1, 1));
        this.addAbility(new Idle(this, 1, 1));
        this.quests = new HashMap<>();
        refresh();
    }

    public Player(DataSnapshot snapshot) {
        super(snapshot);
        this.firebaseKey = snapshot.getKey();
        this.characterPoints = Integer.valueOf((String) (snapshot.child("CP").getValue()));
        this.gold = Integer.valueOf((String) (snapshot.child("gold")).getValue());
        this.quests = new HashMap<>();
        for (DataSnapshot snapQuest : snapshot.child("log").getChildren()) {
            QuestEntry q = new QuestEntry();
            q.key = snapQuest.getKey();
            q.encounter = (snapQuest.child("encounter").exists()) ? (Integer.valueOf((String) snapQuest.child("encounter").getValue())) : (0);
            q.completed = (Integer.valueOf((String) snapQuest.child("completed").getValue()) == 1);
            quests.put(q.key, q);
        }
        this.currentQuestKey = (snapshot.child("currentQuestKey").exists()) ? (String) snapshot.child("currentQuestKey").getValue() : null;
        this.currentQuestEncounterId = (snapshot.child("currentQuestEncounterId").exists()) ? Integer.valueOf((String) snapshot.child("currentQuestEncounterId").getValue()) : 0;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
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

    public void setNextAction(Action action) {
        this.nextAction = action;
    }

    public void saveToFirebase() {
        if (firebaseKey == null || firebaseKey.equals("")) {
            firebaseKey = Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").push().getKey();
        }
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("name").setValue(name);
        DatabaseReference ref = Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey);
        attributes.recordToFirebase(ref.child("attributes"));
        AbilityDao.recordToFirebase(this, ref.child("abilities"));
        InventoryDao.recordToFirebase(this, ref.child("inventory"));
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("CP").setValue(String.valueOf(this.characterPoints));
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("gold").setValue(String.valueOf(this.gold));
        for (Map.Entry<String, QuestEntry> qe : quests.entrySet()) {
            QuestEntry q = qe.getValue();
            DatabaseReference refQ = Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("log").child(q.key);
            refQ.child("encounter").setValue(String.valueOf(q.encounter));
            refQ.child("completed").setValue(String.valueOf((q.completed) ? 1 : 0));
        }
        if (currentQuestKey == null) {
            Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("currentQuestKey").setValue(null);
            Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("currentQuestEncounterId").setValue(null);
        } else {
            Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("currentQuestKey").setValue(this.currentQuestKey);
            Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("currentQuestEncounterId").setValue(String.valueOf(currentQuestEncounterId));
        }
    }

    @Override
    public Action takeAction() {
        if (stunCounter > 0) {
            return new StunnedAction(this);
        }
        Action action = nextAction;
        nextAction = null;
        return action;
    }

    @Override
    public Faction getFaction() {
        return Faction.PLAYER;
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

    public String getCurrentQuestKey() {
        return currentQuestKey;
    }

    public void setCurrentQuestKey(String currentQuestKey) {
        this.currentQuestKey = currentQuestKey;
    }

    public int getCurrentQuestEncounterId() {
        return currentQuestEncounterId;
    }

    public void setCurrentQuestEncounterId(int currentQuestEncounterId) {
        this.currentQuestEncounterId = currentQuestEncounterId;
    }
}
