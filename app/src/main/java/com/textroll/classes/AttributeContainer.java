package com.textroll.classes;

import java.util.ArrayList;

/**
 * Created by audri on 2017-10-21.
 */

public class AttributeContainer {
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

    private Attribute strength;
    private Attribute speed;
    private Attribute endurance;
    private Attribute intelligence;
    private Attribute magic;

    public DerivedAttribute getMaxHealth() {
        return maxHealth;
    }

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
        multipliers.add(new Double(10));
        this.maxHealth = new DerivedAttribute(bases, multipliers, 0);
    }
}
