package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.textroll.classes.Instances;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by audri on 2017-12-20.
 */

class InventoryDao {
    public static void getFromSnapshot(Actor actor, DataSnapshot snapshot) {
        for (DataSnapshot equippedItem : snapshot.child("equipped").getChildren()) {
            actor.equipItem(new Item(Instances.itemSnap.child((String) equippedItem.getValue())));
        }
        for (DataSnapshot inventoryItem : snapshot.child("backpack").getChildren()) {
            actor.getInventory().add(new Item(Instances.itemSnap.child((String) inventoryItem.getValue())));
        }
    }

    public static void recordToFirebase(Actor actor, DatabaseReference inventory) {
        inventory.setValue(null);
        for (Map.Entry<Item.itemType, Item> entry : actor.getEquippedItems().entrySet()) {
            DatabaseReference ref = inventory.child("equipped").child(String.valueOf(entry.getKey()));
            Item item = entry.getValue();
            ref.setValue(item.getFirebaseName());
        }
        ArrayList<Item> backpack = actor.getInventory();
        for (Item item : backpack) {
            DatabaseReference ref = inventory.child("backpack").push();
            ref.setValue(item.getFirebaseName());
        }
    }
}
