package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.textroll.classes.Instances;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Actor implements Serializable {

    public ArrayList<ItemEffect> itemEffects;
    protected ArrayList<ActiveAbility> abilities;
    protected ArrayList<Action> actions;
    protected ArrayList<Action> availableActions;
    protected AttributeContainer attributes;
    protected int currentHealth;
    protected ArrayList<Effect> effects;
    protected transient ActorUIContainer ui;
    protected String name;
    protected String firebaseKey;
    protected int energy = 0;
    protected transient int threat = 0;
    protected HashMap<Item.itemType, Item> equippedItems;
    protected ArrayList<Item> inventory;

    public Actor(String name) {
        this.setAttributes(new AttributeContainer());
        this.abilities = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.availableActions = new ArrayList<>();
        this.effects = new ArrayList<>();
        this.setName(name);
        this.setUi(new ActorUIContainer());
        this.inventory = new ArrayList<>();
        this.equippedItems = new HashMap<>();
        this.itemEffects = new ArrayList<>();
        this.refresh();
    }

    public Actor(DataSnapshot snapshot) {
        this((String) snapshot.child("name").getValue());
        this.firebaseKey = snapshot.getKey();
        attributes.getFromSnapshot(snapshot);
        AbilityDao.getFromSnapshot(this, snapshot);
        InventoryDao.getFromSnapshot(this, snapshot.child("inventory"));
        this.refresh();
    }

    public int getThreat() {
        return threat;
    }

    public void setThreat(int threat) {
        this.threat = threat;
    }

    public void modifyThreat(int threat) {
        this.threat += threat;
    }

    public HashMap<Item.itemType, Item> getEquippedItems() {
        return equippedItems;
    }

    public void setEquippedItems(HashMap<Item.itemType, Item> equippedItems) {
        this.equippedItems = equippedItems;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Item> inventory) {
        this.inventory = inventory;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void addEnergy(int energy) {
        this.energy += energy;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public void saveToFirebase() {
        if (firebaseKey == null || firebaseKey == "") {
            firebaseKey = Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").push().getKey();
        }
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("name").setValue(name);
        DatabaseReference ref = Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey);
        attributes.recordToFirebase(ref.child("attributes"));
        AbilityDao.recordToFirebase(this, ref.child("abilities"));
        InventoryDao.recordToFirebase(this, ref.child("inventory"));
    }

    public void equipItem(Item item) {
        Item.itemType type = item.getType();
        if (equippedItems.get(type) != null) {
            unequipItem(equippedItems.get(type));
        }
        this.equippedItems.put(type, item);
        inventory.remove(item);
        item.onEquip(this);
    }

    public void unequipItem(Item item) {
        item.onUnequip(this);
        this.equippedItems.remove(item.getType());
        this.inventory.add(item);
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

    public void addAbility(Ability ability) {
        if (ability instanceof ActiveAbility) {
            abilities.add((ActiveAbility) ability);
            actions.add(((ActiveAbility) ability).getAction());
        }
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
        for (Action a : actions) {
            if (a instanceof Cooldown) {
                ((Cooldown) a).setRemainingCooldown(0);
            }
        }
        currentHealth = getMaximumHealth();
        energy = 0;
        threat = 0;
    }

    public void takeDamage(int damage, Actor source) {
        for (Effect e : effects) {
            damage = e.onTakeDamage(damage, source);
        }
        for (ItemEffect e : itemEffects) {
            damage = e.onTakeDamage(damage, source);
        }
        if (damage > 0) {
            currentHealth -= damage;
            Instances.turnManager.log(String.format("%s dealt %d damage to %s! \n", source.getName(), damage, getName()));
        }
        if (currentHealth <= 0) {
            onDying();
        }
    }

    private void onDying() {
        boolean stillDying = true;
        for (Effect e : effects) {
            stillDying = e.onDying();
            if (!stillDying) break;
        }
        if (stillDying) {
            for (ItemEffect e : itemEffects) {
                stillDying = e.onDying();
                if (!stillDying) break;
            }
        }
        if (stillDying) {
            die();
        }
    }

    private void die() {
        for (Effect e : effects) {
            e.onDeath();
        }
        for (Effect e : itemEffects) {
            e.onDeath();
        }
        Instances.turnManager.processDeath(this);
    }

    public abstract Action takeAction();

    public void updateAvailableActions() {
        availableActions.clear();
        for (Action action : actions) {
            if (action.isAvailable()) {
                action.setTarget(null);
                    availableActions.add(action);
                }
            }
        }

    public void startCombat() {
    }

    public void startTurn() {
        for (Effect e : itemEffects) {
            e.onTurnStart();
        }
        for (Effect e : effects) {
            e.onTurnStart();
        }
        for (Action a : actions) {
            if (a instanceof Cooldown) {
                ((Cooldown) a).coolDown();
            }
        }
    }

    public void endTurn() {
        for (Effect e : itemEffects) {
            e.onTurnEnd();
        }
        for (Effect e : effects) {
            e.onTurnEnd();
        }
    }

    @Override
    public String toString() {
        return this.name;
    }

    public void heal(int healing) {
        if (healing > 0) currentHealth = Math.min(getMaximumHealth(), currentHealth + healing);
    }
}
