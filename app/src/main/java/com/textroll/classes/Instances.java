package com.textroll.classes;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    public static FirebaseUser user;
    public static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public static DataSnapshot abilitySnap;
}
