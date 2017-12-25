package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by audri on 2017-12-20.
 */

public class Item implements Serializable {
    private int strength;
    private int speed;
    private int endurance;
    private int intelligence;
    private int magic;
    private int maxHealth;
    private String firebaseName;
    private int price;
    private ArrayList<ItemEffect> effects;
    private String name;
    private itemType type;

    public Item(DataSnapshot snapshot) {
        this((String) snapshot.child("name").getValue(), Enum.valueOf(itemType.class, (String) snapshot.child("type").getValue()));
        this.firebaseName = snapshot.getKey();
        this.price = Integer.valueOf((String) snapshot.child("price").getValue());
        DataSnapshot snap = snapshot.child("attributes");
        this.strength = ((snap.child("str").exists()) ? Integer.valueOf((String) snap.child("str").getValue()) : 0);
        this.speed = ((snap.child("spd").exists()) ? Integer.valueOf((String) snap.child("spd").getValue()) : 0);
        this.endurance = ((snap.child("end").exists()) ? Integer.valueOf((String) snap.child("end").getValue()) : 0);
        this.intelligence = ((snap.child("int").exists()) ? Integer.valueOf((String) snap.child("int").getValue()) : 0);
        this.magic = ((snap.child("mag").exists()) ? Integer.valueOf((String) snap.child("mag").getValue()) : 0);
        this.maxHealth = ((snap.child("hp").exists()) ? Integer.valueOf((String) snap.child("hp").getValue()) : 0);
        for (DataSnapshot effectSnap : snapshot.child("effects").getChildren()) {
            this.effects.add(EffectDao.getFromSnapshot(effectSnap));
        }
    }

    public Item(String name, itemType type) {
        this.name = name;
        this.type = type;
        this.effects = new ArrayList<ItemEffect>();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public itemType getType() {
        return type;
    }

    public void setType(itemType type) {
        this.type = type;
    }

    public void onUnequip(Actor actor) {
        AttributeContainer attribs = actor.getAttributes();
        attribs.getMaxHealth().modifyBonus(-this.maxHealth);
        attribs.getMagic().modifyBonus(-this.magic);
        attribs.getIntelligence().modifyBonus(-this.intelligence);
        attribs.getEndurance().modifyBonus(-this.endurance);
        attribs.getSpeed().modifyBonus(-this.speed);
        attribs.getStrength().modifyBonus(-this.strength);
        for (Effect e : effects) {
            e.remove();
        }
    }

    public void onEquip(Actor actor) {
        AttributeContainer attribs = actor.getAttributes();
        attribs.getMaxHealth().modifyBonus(this.maxHealth);
        attribs.getMagic().modifyBonus(this.magic);
        attribs.getIntelligence().modifyBonus(this.intelligence);
        attribs.getEndurance().modifyBonus(this.endurance);
        attribs.getSpeed().modifyBonus(this.speed);
        attribs.getStrength().modifyBonus(this.strength);
        for (Effect e : effects) {
            e.apply(actor);
        }
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append("\n");
        builder.append("Price: ");
        builder.append(price);
        builder.append("\n\n");
        if (strength != 0) {
            builder.append(strength);
            builder.append(" STR\n");
        }
        if (speed != 0) {
            builder.append(speed);
            builder.append(" SPD\n");
        }
        if (endurance != 0) {
            builder.append(endurance);
            builder.append(" END\n");
        }
        if (intelligence != 0) {
            builder.append(intelligence);
            builder.append(" INT\n");
        }
        if (magic != 0) {
            builder.append(magic);
            builder.append(" MAG\n");
        }
        for (ItemEffect eff : effects) {
            builder.append(eff.getDescription());
            builder.append("\n");
        }
        return builder.toString();
    }

    public String getFirebaseName() {
        return firebaseName;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getEndurance() {
        return endurance;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ItemEffect> getEffects() {
        return effects;
    }

    public void setEffects(ArrayList<ItemEffect> effects) {
        this.effects = effects;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public enum itemType {WEAPON, ARMOR, ACCESSORY}
}
