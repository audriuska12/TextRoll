package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.Instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by audri on 2017-12-15.
 */

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
        boolean repeatable = (Integer.valueOf((String) (snapshot.child(key).child("repeatable")).getValue()) == 1);
        String name = (String) (snapshot.child(key).child("name").getValue());
        String description = (String) (snapshot.child(key).child("description").getValue());
        quests.put(key, new QuestNode(this, key, name, description, repeatable));
        for (DataSnapshot snap : snapshot.child(key).child("next").getChildren()) {
            String next = (String) snap.getValue();
            if (!quests.containsKey(next)) {
                recursiveNodeAdd(snapshot, next);
                quests.get(next).previous.add(quests.get(key));
                quests.get(key).next.add(quests.get(next));
            }
        }
    }

    private void questCheck(ArrayList<String> strings, String key) {
        if (!strings.contains(key)) {
            strings.add(key);
        }
        if (Instances.pc.getQuests().get(key) != null && Instances.pc.getQuests().get(key).completed) {
            for (QuestNode next : quests.get(key).getNext()) {
                if (!strings.contains(next.key)) strings.add(next.key);
            }
        }

    }
}
