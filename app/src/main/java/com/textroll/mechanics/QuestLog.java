package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;

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

    public ArrayList<String> getAvailableQuestKeys() {
        ArrayList<String> values = new ArrayList<>();
        for (Map.Entry<String, QuestNode> node : quests.entrySet()) {
            if (node.getValue().isAvailable()) {
                values.add(node.getValue().getKey());
            }
        }
        return values;
    }

    private void recursiveNodeAdd(DataSnapshot snapshot, String key) {
        boolean repeatable = (Integer.valueOf((String) (snapshot.child(key).child("repeatable")).getValue()) == 1);
        quests.put(key, new QuestNode(this, key, repeatable));
        for (DataSnapshot snap : snapshot.child(key).child("next").getChildren()) {
            String next = (String) snap.getValue();
            if (!quests.containsKey(next)) {
                recursiveNodeAdd(snapshot, next);
                quests.get(next).previous.add(quests.get(key));
                quests.get(key).next.add(quests.get(next));
            }
        }
    }
}
