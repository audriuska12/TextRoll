package com.textroll.menus;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.textroll.classes.Instances;
import com.textroll.classes.encounters.intro.IntroEncounterChain;
import com.textroll.mechanics.Player;
import com.textroll.textroll.R;

public class ChargenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargen);
        Instances.mDatabase.keepSynced(true);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        cancelActivity(null);
        finish();
    }

    public void cancelActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void startGameActivity(View view){
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").child("1").child("name").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Instances.pc = new Player(dataSnapshot.getValue().toString());
                        Instances.encounters = new IntroEncounterChain();
                        Intent intent = new Intent(getApplicationContext(), TownMenuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
