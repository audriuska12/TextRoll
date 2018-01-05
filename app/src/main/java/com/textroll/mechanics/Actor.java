package com.textroll.mechanics;

import android.annotation.SuppressLint;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.textroll.classes.Instances;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Actor implements Serializable {

    protected ArrayList<ItemEffect> itemEffects;
    protected ArrayList<ActiveAbility> abilities;
    protected ArrayList<PassiveAbility> passives;
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
    protected transient int stunCounter = 0;
    protected HashMap<Item.itemType, Item> equippedItems;
    protected ArrayList<Item> inventory;
    protected transient boolean dead = false;
    protected transient boolean dying = false;
    protected Faction faction;
    public Actor(String name) {
        this.setAttributes(new AttributeContainer());
        this.abilities = new ArrayList<>();
        this.passives = new ArrayList<>();
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

    public int getStunCounter() {
        return stunCounter;
    }

    public void setStunCounter(int stunCounter) {
        this.stunCounter = stunCounter;
    }

    public void addStun() {
        if (stunCounter == 0) {
            Instances.turnManager.log(String.format("%s is stunned!\n", name));
        }
        stunCounter++;
    }

    public void removeStun() {
        if (stunCounter > 0) {
            stunCounter--;
            if (stunCounter == 0) {
                Instances.turnManager.log(String.format("%s is no longer stunned.\n", name));
            }
        }
    }

    public boolean isStunned() {
        return stunCounter > 0;
    }

    public ArrayList<ItemEffect> getItemEffects() {
        return itemEffects;
    }

    public void setItemEffects(ArrayList<ItemEffect> itemEffects) {
        this.itemEffects = itemEffects;
    }

    public ArrayList<PassiveAbility> getPassives() {
        return passives;
    }

    public void setPassives(ArrayList<PassiveAbility> passives) {
        this.passives = passives;
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
        if (firebaseKey == null || firebaseKey.equals("")) {
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

    public boolean hasEffect(Class<? extends Effect> effect) {
        for (Effect e : effects) {
            if (e.getClass().equals(effect)) return true;
        }
        for (ItemEffect e : itemEffects) {
            if (e.getClass().equals(effect)) return true;
        }
        return false;
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
        } else if (ability instanceof PassiveAbility) {
            ((PassiveAbility) ability).setActor(this);
            passives.add((PassiveAbility) ability);
        }
    }

    public void removeAbility(Ability ability) {
        if (ability instanceof ActiveAbility) {
            actions.remove(((ActiveAbility) ability).getAction());
            abilities.remove(ability);
        } else if (ability instanceof PassiveAbility) {
            passives.remove(ability);
        }
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

    public int getMaximumHealth() {
        return attributes.getMaxHealth().getEffectiveValue();
    }

    public boolean isDead() {
        return dead;
    }

    public void refresh() {
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).remove();
        }
        for (Action a : actions) {
            if (a instanceof Cooldown) {
                ((Cooldown) a).setRemainingCooldown(0);
            }
        }
        for (PassiveAbility p : passives) {
            if (p instanceof Cooldown) {
                ((Cooldown) p).setRemainingCooldown(0);
            }
            p.refresh();
        }
        for (ItemEffect e : itemEffects) {
            e.refresh();
        }
        currentHealth = getMaximumHealth();
        dead = false;
        dying = false;
        energy = 0;
        threat = 0;
    }

    @SuppressLint("DefaultLocale")
    public void takeDamage(int damage, Actor source) {
        if (damage <= 0) return;
        for (int i = passives.size() - 1; i >= 0; i--) {
            damage = passives.get(i).onTakeDamage(damage, source);
            if (damage <= 0) return;
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            damage = itemEffects.get(i).onTakeDamage(damage, source);
            if (damage <= 0) return;
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            damage = effects.get(i).onTakeDamage(damage, source);
            if (damage <= 0) return;
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
        if (dying) return;
        dying = true;
        for (int i = passives.size() - 1; i >= 0; i--) {
            dying = passives.get(i).onDying();
            if (!dying) return;
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            dying = itemEffects.get(i).onDying();
            if (!dying) return;
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            dying = effects.get(i).onDying();
            if (!dying) return;
        }
        if (dying) {
            die();
        }
    }

    public void die() {
        dead = true;
        dying = false;
        for (int i = passives.size() - 1; i >= 0; i--) {
            passives.get(i).onDeath();
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            itemEffects.get(i).onDeath();
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).onDeath();
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
        for (int i = passives.size() - 1; i >= 0; i--) {
            passives.get(i).onStartOfCombat();
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            itemEffects.get(i).onStartOfCombat();
        }
    }

    public void endCombat() {
        for (int i = passives.size() - 1; i >= 0; i--) {
            passives.get(i).onEndOfCombat();
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            itemEffects.get(i).onEndOfCombat();
        }
    }

    public void startTurn() {
        for (int i = passives.size() - 1; i >= 0; i--) {
            passives.get(i).onTurnStart();
            if (passives.get(i) instanceof Cooldown) {
                ((Cooldown) passives.get(i)).coolDown();
            }
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            itemEffects.get(i).onTurnStart();
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).onTurnStart();
        }
        for (Action a : actions) {
            if (a instanceof Cooldown) {
                ((Cooldown) a).coolDown();
            }
        }
    }

    public void endTurn() {
        for (int i = passives.size() - 1; i >= 0; i--) {
            passives.get(i).onTurnEnd();
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            itemEffects.get(i).onTurnEnd();
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).onTurnEnd();
        }
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @SuppressLint("DefaultLocale")
    public void heal(int healing, Actor source) {
        if (healing <= 0) return;
        for (int i = passives.size() - 1; i >= 0; i--) {
            healing = passives.get(i).onReceiveHealing(healing, source);
            if (healing <= 0) return;
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            healing = itemEffects.get(i).onReceiveHealing(healing, source);
            if (healing <= 0) return;
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            healing = effects.get(i).onReceiveHealing(healing, source);
            if (healing <= 0) return;
        }
        healing = Math.min(healing, getMaximumHealth() - getCurrentHealth());
        if (healing > 0) {
            currentHealth += healing;
            Instances.turnManager.log(String.format("%s recovers %d health. \n", getName(), healing));
        }
        if (currentHealth <= 0) {
            onDying();
        }
    }

    public void beforeAttacking(Actor target) {
        for (int i = passives.size() - 1; i >= 0; i--) {
            passives.get(i).beforeAttacking(target);
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            itemEffects.get(i).beforeAttacking(target);
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).beforeAttacking(target);
        }
    }

    public void afterAttacking(Actor target) {
        for (int i = passives.size() - 1; i >= 0; i--) {
            passives.get(i).afterAttacking(target);
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            itemEffects.get(i).afterAttacking(target);
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).afterAttacking(target);
        }
    }

    public boolean beforeAttacked(Actor attacker) {
        for (int i = passives.size() - 1; i >= 0; i--) {
            if (!passives.get(i).beforeAttacked(attacker)) return false;
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            if (!itemEffects.get(i).beforeAttacked(attacker)) return false;
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            if (!effects.get(i).beforeAttacked(attacker)) return false;
        }
        return true;
    }

    public void afterAttacked(Actor attacker) {
        for (int i = passives.size() - 1; i >= 0; i--) {
            passives.get(i).afterAttacked(attacker);
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            itemEffects.get(i).afterAttacked(attacker);
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).afterAttacked(attacker);
        }
    }

    public void beforeCasting(Actor target) {
        for (int i = passives.size() - 1; i >= 0; i--) {
            passives.get(i).beforeCasting(target);
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            itemEffects.get(i).beforeCasting(target);
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).beforeCasting(target);
        }
    }

    public void afterCasting(Actor target) {
        for (int i = passives.size() - 1; i >= 0; i--) {
            passives.get(i).afterCasting(target);
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            itemEffects.get(i).afterCasting(target);
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).afterCasting(target);
        }
    }

    public boolean beforeSpellHit(Actor attacker) {
        for (int i = passives.size() - 1; i >= 0; i--) {
            if (!passives.get(i).beforeSpellHit(attacker)) return false;
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            if (!itemEffects.get(i).beforeSpellHit(attacker)) return false;
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            if (!effects.get(i).beforeSpellHit(attacker)) return false;
        }
        return true;
    }

    public void afterSpellHit(Actor attacker) {
        for (int i = passives.size() - 1; i >= 0; i--) {
            passives.get(i).afterSpellHit(attacker);
        }
        for (int i = itemEffects.size() - 1; i >= 0; i--) {
            itemEffects.get(i).afterSpellHit(attacker);
        }
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).afterSpellHit(attacker);
        }
    }

    public enum Faction {PLAYER, ENEMY}
}
