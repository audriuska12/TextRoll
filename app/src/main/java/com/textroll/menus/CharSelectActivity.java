package com.textroll.menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.textroll.classes.Instances;
import com.textroll.classes.encounters.intro.IntroEncounterChain;
import com.textroll.mechanics.Player;
import com.textroll.textroll.R;

import java.util.ArrayList;

public class CharSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_select);
        populateCharacterSpinner();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelActivity(null);
        finish();
    }

    public void cancelActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goToChargen(View view) {
        Intent intent = new Intent(this, ChargenActivity.class);
        startActivity(intent);
    }

    public void playWithSelected(View view) {
        Spinner charSelectSpinner = findViewById(R.id.spinnerCharacterSelect);
        Instances.pc = (Player) charSelectSpinner.getSelectedItem();
        Instances.encounters = new IntroEncounterChain();
        Intent intent = new Intent(getApplicationContext(), TownMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void populateCharacterSpinner() {
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("characters").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Player> characters = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        characters.add(new Player(snapshot));
                    } catch (Exception e) {
                    }

                }
                Spinner charSelectSpinner = findViewById(R.id.spinnerCharacterSelect);
                ArrayAdapter<Player> arrayAdapter = new ArrayAdapter<Player>(CharSelectActivity.this, android.R.layout.simple_spinner_item, characters);
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                charSelectSpinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
