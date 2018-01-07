package com.textroll.mechanics;

import java.util.ArrayList;

public class AbilityNode {
    private AbilityMap map;
    private ArrayList<AbilityNode> previous;
    private ArrayList<AbilityNode> next;
    private String key;
    private int baseCost;
    private int costPerRank;
    private boolean forPlayers;
    private int maxRank;

    public AbilityNode(AbilityMap map, String key, boolean forPlayers, int maxRank, int baseCost, int costPerRank) {
        this.map = map;
        this.key = key;
        this.baseCost = baseCost;
        this.costPerRank = costPerRank;
        this.maxRank = maxRank;
        this.forPlayers = forPlayers;
        this.previous = new ArrayList<>();
        this.next = new ArrayList<>();
    }

    public int getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(int baseCost) {
        this.baseCost = baseCost;
    }

    public int getCostPerRank() {
        return costPerRank;
    }

    public void setCostPerRank(int costPerRank) {
        this.costPerRank = costPerRank;
    }

    public boolean isForPlayers() {
        return forPlayers;
    }

    public void setForPlayers(boolean forPlayers) {
        this.forPlayers = forPlayers;
    }

    public int getMaxRank() {
        return maxRank;
    }

    public void setMaxRank(int maxRank) {
        this.maxRank = maxRank;
    }

    public ArrayList<AbilityNode> getPrevious() {
        return previous;
    }

    public void setPrevious(ArrayList<AbilityNode> previous) {
        this.previous = previous;
    }

    public ArrayList<AbilityNode> getNext() {
        return next;
    }

    public void setNext(ArrayList<AbilityNode> next) {
        this.next = next;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
