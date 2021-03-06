package com.textroll.mechanics;

import java.io.Serializable;

public class QuestEntry implements Serializable {
    public String key;
    public int encounter;
    public boolean completed;

    public QuestEntry() {
    }

    public QuestEntry(String key, int encounter, boolean completed) {
        this.key = key;
        this.encounter = encounter;
        this.completed = completed;
    }
}
