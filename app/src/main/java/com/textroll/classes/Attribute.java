package com.textroll.classes;

/**
 * Created by audri on 2017-10-20.
 */

public class Attribute {
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

    public void setBonus(int bonus){
        this.bonus= bonus;
    }

    public void setMultiplier(double multiplier){
        this.multiplier = multiplier;
    }

    public int ModifyBase(int change){
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

    public double getMultiplier(){
        return multiplier;
    }

    public int getEffectiveValue(){
        return (int)Math.max((baseValue + bonus)*multiplier, 0);
    }

}
