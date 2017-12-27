package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.textroll.classes.Instances;

import java.lang.reflect.Constructor;

class AbilityDao {
    static void getFromSnapshot(final Actor actor, DataSnapshot dataSnapshot) {

        for (DataSnapshot ability : dataSnapshot.child("abilities").getChildren()) {
            String name = (String) ability.child("class").getValue();
            int rank = Integer.valueOf((String) ability.child("rank").getValue());
            try {
                Class c = Class.forName("com.textroll.classes.abilities." + name);
                Constructor<?> con = c.getConstructor(Actor.class, Integer.TYPE, Integer.TYPE);
                int maxRank = Integer.valueOf((String) (Instances.abilitySnap.child(c.getSimpleName()).child("maxRank").getValue()));
                Ability abl = (Ability) con.newInstance(actor, maxRank, rank);
                actor.addAbility(abl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    static void recordToFirebase(Actor actor, DatabaseReference abilitiesRef) {
        abilitiesRef.setValue(null, new AbilityCleanListener(actor, abilitiesRef));
    }
}

class AbilityCleanListener implements DatabaseReference.CompletionListener {

    private Actor actor;
    private DatabaseReference abilitiesRef;

    AbilityCleanListener(Actor actor, DatabaseReference abilitiesRef) {
        this.actor = actor;
        this.abilitiesRef = abilitiesRef;
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        Object[] abilities = actor.getAbilities().toArray();
        for (Object ability1 : abilities) {
            ActiveAbility ability = (ActiveAbility) ability1;
            DatabaseReference abilityRoot = abilitiesRef.push();
            abilityRoot.child("class").setValue("active." + ability.getClass().getSimpleName());
            abilityRoot.child("rank").setValue(String.valueOf(ability.getCurrentRank()));
        }
        Object[] passives = actor.getPassives().toArray();
        for (Object passive : passives) {
            PassiveAbility ability = (PassiveAbility) passive;
            DatabaseReference abilityRoot = abilitiesRef.push();
            abilityRoot.child("class").setValue("passive." + ability.getClass().getSimpleName());
            abilityRoot.child("rank").setValue(String.valueOf(ability.getCurrentRank()));
        }
    }
}