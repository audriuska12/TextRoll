package com.textroll.mechanics;

import java.util.ArrayList;

/**
 * Created by audri on 2017-12-16.
 */

public class AbilityNode {
    AbilityMap map;
    ArrayList<AbilityNode> previous;
    ArrayList<AbilityNode> next;
    String key;
    int baseCost;
    int costPerRank;
    boolean forPlayers = false;
    int maxRank;
    String description;

    public AbilityNode(AbilityMap map, String key, boolean forPlayers, int maxRank, int baseCost, int costPerRank, String description) {
        this.map = map;
        this.key = key;
        this.baseCost = baseCost;
        this.costPerRank = costPerRank;
        this.maxRank = maxRank;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
