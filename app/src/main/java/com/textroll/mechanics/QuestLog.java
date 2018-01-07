package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.Instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestLog {
    HashMap<String, QuestNode> quests;

    public QuestLog(DataSnapshot snapshot, String startPoint) {
        quests = new HashMap<>();
        recursiveNodeAdd(snapshot, startPoint);
    }

    public HashMap<String, QuestNode> getQuests() {
        return quests;
    }

    public void setQuests(HashMap<String, QuestNode> quests) {
        this.quests = quests;
    }

    public ArrayList<String> getAvailableQuests() {
        ArrayList<String> questKeys = new ArrayList<>();
        for (Map.Entry<String, QuestEntry> quest : Instances.pc.getQuests().entrySet()) {
            String questName = quest.getValue().key;
            questCheck(questKeys, questName);
        }
        for (Map.Entry<String, QuestEntry> quest : Instances.pc.getQuests().entrySet()) {
            String questName = quest.getValue().key;
            if (quest.getValue().completed && !quests.get(questName).isRepeatable()) {
                questKeys.remove(questName);
            }
        }
        return questKeys;
    }

    private void recursiveNodeAdd(DataSnapshot snapshot, String key) {
        int version = (snapshot.child("version").exists() ? Integer.valueOf((String) snapshot.child("version").getValue()) : 0);
        if (version > Instances.version) return;
        boolean repeatable = (Integer.valueOf((String) (snapshot.child(key).child("repeatable")).getValue()) == 1);
        String name = (String) (snapshot.child(key).child("name").getValue());
        String description = (String) (snapshot.child(key).child("description").getValue());
        quests.put(key, new QuestNode(this, key, name, description, repeatable));
        for (DataSnapshot snap : snapshot.child(key).child("next").getChildren()) {
            String next = (String) snap.getValue();
            if (!quests.containsKey(next)) {
                recursiveNodeAdd(snapshot, next);
                quests.get(next).getPrevious().add(quests.get(key));
                quests.get(key).getNext().add(quests.get(next));
            }
        }
    }

    private void questCheck(ArrayList<String> strings, String key) {
        if (!strings.contains(key)) {
            strings.add(key);
        }
        if (Instances.pc.getQuests().get(key) != null && Instances.pc.getQuests().get(key).completed) {
            for (QuestNode next : quests.get(key).getNext()) {
                if (!strings.contains(next.getKey())) strings.add(next.getKey());
            }
        }

    }
}
