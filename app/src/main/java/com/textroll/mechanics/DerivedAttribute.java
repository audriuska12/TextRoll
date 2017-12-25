package com.textroll.mechanics;

import java.io.Serializable;
import java.util.ArrayList;


public class DerivedAttribute implements Serializable {
    private ArrayList<Attribute> baseAttributes;
    private ArrayList<Double> multipliers;
    private int baseValue;
    private int bonus;
    private double multiplier;

    public DerivedAttribute(ArrayList<Attribute> baseAttributes, ArrayList<Double> multipliers, int baseValue){
        this.baseAttributes = new ArrayList<>();
        this.multipliers = new ArrayList<>();
        for(int i = 0; i< baseAttributes.size(); i++){
            this.baseAttributes.add(baseAttributes.get(i));
            this.multipliers.add(multipliers.get(i));
        }
        this.baseValue = baseValue;
        this.bonus = 0;
        this.multiplier = 1;
    }

    public void setBase(int baseValue){
        this.baseValue = baseValue;
    }

    public int modifyBase(int change) {
        baseValue = Math.min(baseValue+change, 0);
        return baseValue;
    }

    public int modifyBonus(int change){
        bonus +=change;
        return bonus;
    }

    public double modifyMultiplier(double change){
        multiplier = Math.min(multiplier+change, 0);
        return multiplier;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public int getBonus(){
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public double getMultiplier(){
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public int getEffectiveValue(){
        double sum = baseValue + bonus;
        for(int i = 0; i<baseAttributes.size(); i++){
            sum += baseAttributes.get(i).getEffectiveValue()*multipliers.get(i);
        }
        return (int)Math.max(sum*multiplier, 0);
    }
}
