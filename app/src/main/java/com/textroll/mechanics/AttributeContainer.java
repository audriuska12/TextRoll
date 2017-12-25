package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;


public class AttributeContainer implements Serializable {
    private Attribute strength;
    private Attribute speed;
    private Attribute endurance;
    private Attribute intelligence;
    private Attribute magic;
    private DerivedAttribute maxHealth;
    public AttributeContainer(){
        this.strength = new Attribute();
        this.speed = new Attribute();
        this.endurance = new Attribute();
        this.intelligence = new Attribute();
        this.magic = new Attribute();
        ArrayList<Attribute> bases = new ArrayList<>();
        bases.add(endurance);
        ArrayList<Double> multipliers = new ArrayList<>();
        multipliers.add(10d);
        this.maxHealth = new DerivedAttribute(bases, multipliers, 0);
    }

    public Attribute getStrength() {
        return strength;
    }

    public Attribute getSpeed() {
        return speed;
    }

    public Attribute getEndurance() {
        return endurance;
    }

    public Attribute getIntelligence() {
        return intelligence;
    }

    public Attribute getMagic() {
        return magic;
    }

    public DerivedAttribute getMaxHealth() {
        return maxHealth;
    }

    public void getFromSnapshot(DataSnapshot snapshot) {
        DataSnapshot snap = snapshot.child("attributes");
        this.strength.setBase(Integer.valueOf((String) snap.child("str").getValue()));
        this.speed.setBase(Integer.valueOf((String) snap.child("spd").getValue()));
        this.endurance.setBase(Integer.valueOf((String) snap.child("end").getValue()));
        this.intelligence.setBase(Integer.valueOf((String) snap.child("int").getValue()));
        this.magic.setBase(Integer.valueOf((String) snap.child("mag").getValue()));
        this.getMaxHealth().setBase((snapshot.child("hp").exists()) ? Integer.valueOf((String) snap.child("hp").getValue()) : 0);
    }

    public void recordToFirebase(DatabaseReference attributes) {
        attributes.child("str").setValue(String.valueOf(this.strength.getBaseValue()));
        attributes.child("spd").setValue(String.valueOf(this.speed.getBaseValue()));
        attributes.child("end").setValue(String.valueOf(this.endurance.getBaseValue()));
        attributes.child("int").setValue(String.valueOf(this.intelligence.getBaseValue()));
        attributes.child("mag").setValue(String.valueOf(this.magic.getBaseValue()));
        attributes.child("hp").setValue(String.valueOf(this.getMaxHealth().getBaseValue()));
    }
}
