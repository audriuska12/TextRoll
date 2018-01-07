package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.Constructor;


class EffectDao {
    static ItemEffect getFromSnapshot(DataSnapshot snap) {
        try {
            Class<?> c = Class.forName("com.textroll.classes.effects." + snap.getKey());
            Constructor con = c.getConstructor(DataSnapshot.class);
            return (ItemEffect) con.newInstance(snap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
