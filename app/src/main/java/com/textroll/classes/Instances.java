package com.textroll.classes;

import com.textroll.mechanics.EncounterChain;
import com.textroll.mechanics.Enemy;
import com.textroll.mechanics.Player;

import java.util.List;
import java.util.Random;


public class Instances {
    public static Player pc;
    public static List<Enemy> enemies;
    public static Random rng = new Random();
    public static TurnManager turnManager;
    public static EncounterChain encounters;
}
