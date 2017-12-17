package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.Instances;

import java.lang.reflect.Constructor;
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

    public ArrayList<String> getAvailableQuests(Player player) {
        ArrayList<String> questKeys = new ArrayList<>();
        for (Map.Entry<String, QuestEntry> quest : player.getQuests().entrySet()) {
            String questName = quest.getValue().key;
            recursiveQuestCheck(questKeys, questName);
        }
        for (Map.Entry<String, QuestEntry> quest : player.getQuests().entrySet()) {
            String questName = quest.getValue().key;
            if (!quests.get(questName).isRepeatable()) questKeys.remove(questName);
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

    private void recursiveQuestCheck(ArrayList<String> strings, String key) {
        if (strings.contains(key) && !quests.get(key).isRepeatable()) {
            strings.remove(key);
        } else {
            strings.add(key);
            for (QuestNode next : quests.get(key).getNext()) {
                recursiveQuestCheck(strings, next.key);
            }
        }
    }
}
