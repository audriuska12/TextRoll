package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by audri on 2017-12-16.
 */

public class AbilityMap {
    HashMap<String, AbilityNode> abilities;

    public AbilityMap(DataSnapshot snapshot, String startPoint) {
        abilities = new HashMap<>();
        recursiveNodeAdd(snapshot, startPoint);
    }

    public HashMap<String, AbilityNode> getAbilities() {
        return abilities;
    }

    public void setAbilities(HashMap<String, AbilityNode> abilities) {
        this.abilities = abilities;
    }

    private void recursiveNodeAdd(DataSnapshot snapshot, String key) {
        boolean forPlayers = (Integer.valueOf((String) (snapshot.child(key).child("availableForPlayers")).getValue()) == 1);
        int maxRank = Integer.valueOf((String) (snapshot.child(key).child("maxRank")).getValue());
        int baseCost = Integer.valueOf((String) (snapshot.child(key).child("baseCost")).getValue());
        int costPerRank = Integer.valueOf((String) (snapshot.child(key).child("costPerRank")).getValue());
        String description = (String) (snapshot.child(key).child("description")).getValue();
        abilities.put(key, new AbilityNode(this, key, forPlayers, maxRank, baseCost, costPerRank, description));
        for (DataSnapshot snap : snapshot.child(key).child("unlockedAbilities").getChildren()) {
            String next = (String) snap.getValue();
            if (!abilities.containsKey(next)) {
                recursiveNodeAdd(snapshot, next);
                abilities.get(next).previous.add(abilities.get(key));
                abilities.get(key).next.add(abilities.get(next));
            }
        }
    }

    public ArrayList<Ability> getAvailableAbilities(Player player) {
        ArrayList<String> classNames = new ArrayList<>();
        for (ActiveAbility active : player.getAbilities()) {
            String className = active.getClass().getSimpleName();
            recursiveAbilityCheck(classNames, className);
        }
        for (ActiveAbility active : player.getAbilities()) {
            String className = active.getClass().getSimpleName();
            classNames.remove(className);
        }
        ArrayList<Ability> abl = new ArrayList<>();
        for (String name : classNames) {
            try {
                Class cl = Class.forName("com.textroll.classes.abilities.active." + name);
                Constructor c = cl.getConstructor(Actor.class, int.class, int.class);
                abl.add((Ability) c.newInstance(player, abilities.get(name).getMaxRank(), 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return abl;
    }

    private void recursiveAbilityCheck(ArrayList<String> strings, String key) {
        if (strings.contains(key)) {
            strings.remove(key);
        } else {
            strings.add(key);
            for (AbilityNode next : abilities.get(key).getNext()) {
                recursiveAbilityCheck(strings, next.key);
            }
        }
    }
}
