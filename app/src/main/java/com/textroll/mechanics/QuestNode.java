package com.textroll.mechanics;

import java.util.ArrayList;

public class QuestNode {
    private String name;
    private QuestLog log;
    private ArrayList<QuestNode> previous;
    private ArrayList<QuestNode> next;
    private String key;
    private boolean completed = false;
    private String description;
    private boolean repeatable;

    public QuestNode(QuestLog log, String key, String name, String description, boolean repeatable) {
        this.log = log;
        this.key = key;
        this.name = name;
        this.description = description;
        this.repeatable = repeatable;
        this.previous = new ArrayList<>();
        this.next = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public ArrayList<QuestNode> getPrevious() {
        return previous;
    }

    public void setPrevious(ArrayList<QuestNode> previous) {
        this.previous = previous;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public ArrayList<QuestNode> getNext() {
        return next;
    }

    public void setNext(ArrayList<QuestNode> next) {
        this.next = next;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isAvailable() {
        if (previous.size() == 0) {
            return true;
        }
        for (QuestNode p : previous) {
            if (p.isCompleted()) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
