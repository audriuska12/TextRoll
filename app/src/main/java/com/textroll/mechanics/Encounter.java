package com.textroll.mechanics;

import java.util.ArrayList;

public class Encounter {
    private ArrayList<Enemy> enemies;

    public Encounter(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}
