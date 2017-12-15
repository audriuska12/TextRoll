package com.textroll.mechanics;

import java.util.ArrayList;

public class QuestNode {
    QuestLog log;
    ArrayList<QuestNode> previous;
    ArrayList<QuestNode> next;
    String key;
    boolean completed = false;
    boolean repeatable;

    public QuestNode(QuestLog log, String key, boolean repeatable) {
        this.log = log;
        this.key = key;
        this.repeatable = repeatable;
        this.previous = new ArrayList<>();
        this.next = new ArrayList<>();
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
}
