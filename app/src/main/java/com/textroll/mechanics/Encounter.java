package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.Instances;

import java.util.ArrayList;

public class Encounter {
    private ArrayList<Enemy> enemies;
    private int rewardCP;
    private int rewardG;

    public Encounter() {
        this.enemies = new ArrayList<>();
    }

    public Encounter(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public Encounter(DataSnapshot snapshot) {
        this();
        this.rewardCP = Integer.valueOf((String) snapshot.child("rewardCP").getValue());
        this.rewardG = Integer.valueOf((String) snapshot.child("rewardG").getValue());
        for (DataSnapshot enemy : snapshot.child("enemies").getChildren()) {
            enemies.add(new Enemy(Instances.enemySnap.child(String.valueOf(enemy.getValue()))));
        }
    }

    public void reset() {
        for (Enemy e : enemies) {
            e.refresh();
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public int getRewardCP() {
        return rewardCP;
    }

    public void setRewardCP(int rewardCP) {
        this.rewardCP = rewardCP;
    }

    public int getRewardG() {
        return rewardG;
    }

    public void setRewardG(int rewardGold) {
        this.rewardG = rewardGold;
    }
}
