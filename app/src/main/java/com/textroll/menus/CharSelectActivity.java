package com.textroll.menus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.textroll.classes.Instances;
import com.textroll.mechanics.Ability;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.EncounterChain;
import com.textroll.mechanics.Player;
import com.textroll.textroll.R;

import java.util.ArrayList;
import java.util.List;

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
        if (Instances.pc.getCurrentQuestKey() != null) {
            Instances.mDatabase.child("encounterChains").child(Instances.pc.getCurrentQuestKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Instances.encounters = new EncounterChain(dataSnapshot);
                for (int i = 0; i < Instances.pc.getCurrentQuestEncounterId(); i++) {
                    Instances.encounters.next();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });
        }
        Intent intent = new Intent(getApplicationContext(), TownMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void deleteSelectedCharacter(View view) {
        new AlertDialog.Builder(this).setTitle("Are you sure?").setMessage("This can't be undone!").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Spinner charSelect = findViewById(R.id.spinnerCharacterSelect);
                ((Player) (charSelect).getSelectedItem()).deleteFromDatabase();
                updateCharacterDisplay(null);
                populateCharacterSpinner();
            }
        }).setNegativeButton(android.R.string.no, null).show();
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
                charSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Player character = (Player) adapterView.getSelectedItem();
                        updateCharacterDisplay(character);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        updateCharacterDisplay(null);
                    }
                });
                ArrayAdapter<Player> arrayAdapter = new ArrayAdapter<Player>(CharSelectActivity.this, android.R.layout.simple_spinner_item, characters);
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                charSelectSpinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateCharacterDisplay(Player character) {
        if (character == null) {
            findViewById(R.id.linearLayoutCharSelectOuter).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) findViewById(R.id.textViewCharSelectValStr)).setText(String.valueOf(character.getAttributes().getStrength().getBaseValue()));
            ((TextView) findViewById(R.id.textViewCharSelectValSpd)).setText(String.valueOf(character.getAttributes().getSpeed().getBaseValue()));
            ((TextView) findViewById(R.id.textViewCharSelectValEnd)).setText(String.valueOf(character.getAttributes().getEndurance().getBaseValue()));
            ((TextView) findViewById(R.id.textViewCharSelectValInt)).setText(String.valueOf(character.getAttributes().getIntelligence().getBaseValue()));
            ((TextView) findViewById(R.id.textViewCharSelectValMag)).setText(String.valueOf(character.getAttributes().getMagic().getBaseValue()));
            ((TextView) findViewById(R.id.textViewCharSelectCPVal)).setText(String.valueOf(character.getCharacterPoints()));
            ((TextView) findViewById(R.id.textViewCharSelectGoldVal)).setText(String.valueOf(character.getGold()));
            AbilityArrayAdapter<ActiveAbility> adapterActives = new AbilityArrayAdapter<ActiveAbility>(CharSelectActivity.this, android.R.layout.simple_list_item_1, character.getAbilities());
            ((ListView) (findViewById(R.id.listViewCharSelectActives))).setAdapter(adapterActives);
            findViewById(R.id.linearLayoutCharSelectOuter).setVisibility(View.VISIBLE);
        }
    }
}

