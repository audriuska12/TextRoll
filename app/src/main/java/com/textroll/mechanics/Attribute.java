package com.textroll.mechanics;

import java.io.Serializable;

public class Attribute implements Serializable {
    private int baseValue;
    private int bonus;
    private double multiplier;

    public Attribute(){
        this.baseValue=5;
        this.bonus = 0;
        this.multiplier = 1;
    }
    public Attribute(int value){
        this.baseValue=value;
        this.bonus = 0;
        this.multiplier = 1;
    }

    public void setBase(int baseValue){
        this.baseValue = baseValue;
    }

    public int modifyBase(int change) {
        baseValue = Math.max(baseValue + change, 0);
        return baseValue;
    }

    public void modifyBonus(int change) {
        bonus +=change;
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
        return (int)Math.max((baseValue + bonus)*multiplier, 0);
    }

}
