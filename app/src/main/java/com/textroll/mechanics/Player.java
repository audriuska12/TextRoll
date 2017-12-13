package com.textroll.mechanics;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.Instances;
import com.textroll.classes.abilities.active.generic.BasicAttack;
import com.textroll.classes.abilities.active.player.Berserk;

public class Player extends Actor {

    private Action nextAction;
    private int characterPoints;

    public Player(String name){
        super(name);
        addAbility(new BasicAttack(this, 1, 1));
        addAbility(new Berserk(this, 5, 1));
        refresh();
    }

    public Player(DataSnapshot snapshot) {
        super(snapshot);
        this.characterPoints = Integer.valueOf((String) (snapshot.child("CP").getValue()));
    }

    public void setNextAction(Action action){
        this.nextAction = action;
    }

    @Override
    public void saveToFirebase() {
        super.saveToFirebase();
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child(firebaseKey).child("CP").setValue(String.valueOf(this.characterPoints));
    }

    @Override
    public Action takeAction() {
        Action action = nextAction;
        nextAction = null;
        return action;
    }
}
