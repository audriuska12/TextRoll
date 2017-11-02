package com.textroll.mechanics;

import com.textroll.classes.Instances;

import java.util.ArrayList;

public abstract class Actor {

    protected ArrayList<ActiveAbility> abilities;
    protected ArrayList<Action> actions;
    protected ArrayList<Action> availableActions;
    protected AttributeContainer attributes;
    private int currentHealth;
    private ArrayList<Effect> effects;
    private ActorUIContainer ui;
    private String name;
    public Actor(String name){
        this.setAttributes(new AttributeContainer());
        this.abilities = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.availableActions = new ArrayList<>();
        this.effects = new ArrayList<>();
        this.setName(name);
        this.setUi(new ActorUIContainer());
        this.refresh();
    }

    public ArrayList<Action> getAvailableActions() {
        return availableActions;
    }

    public void setAvailableActions(ArrayList<Action> availableActions) {
        this.availableActions = availableActions;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

    public void setEffects(ArrayList<Effect> effects) {
        this.effects = effects;
    }

    public ArrayList<ActiveAbility> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<ActiveAbility> abilities) {
        this.abilities = abilities;
    }

    public void addAbility(ActiveAbility ability){
        abilities.add(ability);
        actions.add(ability.getAction());
    }

    public void removeAbility(ActiveAbility ability) {
        actions.remove(ability.getAction());
        abilities.remove(ability);
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public AttributeContainer getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributeContainer attributes) {
        this.attributes = attributes;
    }

    public ActorUIContainer getUi() {
        return ui;
    }

    public void setUi(ActorUIContainer ui) {
        this.ui = ui;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void setCurrentHeatlh(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getMaximumHealth() {
        return attributes.getMaxHealth().getEffectiveValue();
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public void refresh() {
        for (Effect e : effects) {
            e.remove();
        }
        currentHealth = getMaximumHealth();
    }

    public void takeDamage(int damage) {
        if (damage > 0) {
            currentHealth -= damage;
        }
        if (isDead()) Instances.turnManager.processDeath(this);
    }

    public abstract Action takeAction();

    public void updateAvailableActions(Actor target) {
        availableActions.clear();
        if (target != null) {
            for (Action action: actions) {
                if (action.isAvailable(this, target)) {
                    action.setTarget(target);
                    availableActions.add(action);
                }
            }
        }
    }

    public void startCombat() {
    }
    public void startTurn(){
        for(Effect e: effects){
            e.onTurnStart();
        }
        for(Action a: actions){
            if(a instanceof Cooldown){
                ((Cooldown) a).coolDown();
            }
        }
    }

    public void endTurn(){
        for(Effect e: effects){
            e.onTurnEnd();
        }
    }
}
