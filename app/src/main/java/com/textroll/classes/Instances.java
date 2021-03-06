package com.textroll.classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.textroll.mechanics.AbilityMap;
import com.textroll.mechanics.EncounterChain;
import com.textroll.mechanics.Enemy;
import com.textroll.mechanics.Player;
import com.textroll.mechanics.QuestLog;

import java.util.List;
import java.util.Random;


public class Instances {
    public static int version = 1;
    public static Player pc;
    public static List<Enemy> enemies;
    public static Random rng = new Random();
    public static TurnManager turnManager;
    public static EncounterChain encounters;
    public static FirebaseAuth mAuth;
    public static FirebaseUser user;
    public static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public static DataSnapshot abilitySnap;
    public static DataSnapshot enemySnap;
    public static DataSnapshot itemSnap;
    public static QuestLog questLog;
    public static AbilityMap abilityMap;
    public static String displayName;
    public static DataSnapshot shopSnap;
    public static DataSnapshot summonSnap;
}
