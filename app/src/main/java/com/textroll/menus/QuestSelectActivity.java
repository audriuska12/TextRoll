package com.textroll.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.textroll.classes.Instances;
import com.textroll.mechanics.EncounterChain;
import com.textroll.mechanics.QuestEntry;
import com.textroll.mechanics.QuestNode;
import com.textroll.textroll.R;

import java.util.ArrayList;

public class QuestSelectActivity extends AppCompatActivity {
    ArrayList<QuestNode> quests;
    QuestNode selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_select);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> availableQuestKeys = Instances.questLog.getAvailableQuests(Instances.pc);
        quests = new ArrayList<>();
        for (String key : availableQuestKeys) {
            quests.add(Instances.questLog.getQuests().get(key));
        }
        QuestListAdapter adapter = new QuestListAdapter(QuestSelectActivity.this, android.R.layout.simple_spinner_item, quests);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner questSpinner = findViewById(R.id.spinnerQuestSelectAvailableQuests);
        questSpinner.setAdapter(adapter);
        questSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (QuestNode) adapterView.getSelectedItem();
                ((TextView) findViewById(R.id.textViewQuestSelectDescription)).setText(selected.getDescription());
                findViewById(R.id.buttonQuestSelectConfirm).setClickable(true);
                findViewById(R.id.buttonQuestSelectConfirm).setAlpha(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ((TextView) findViewById((R.id.textViewQuestSelectDescription))).setText("");
                findViewById(R.id.buttonQuestSelectConfirm).setClickable(false);
                findViewById(R.id.buttonQuestSelectConfirm).setAlpha(0.5f);
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        goToTown(null);
    }

    public void goToTown(View view) {
        Intent intent = new Intent(this, TownMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void selectQuest(View view) {
        if (selected != null) {
            Instances.pc.setCurrentQuestKey(selected.getKey());
            Instances.pc.setCurrentQuestEncounterId(0);
            Toast.makeText(this, "Loading quest from database...", Toast.LENGTH_SHORT).show();
            Instances.mDatabase.child("encounterChains").child(selected.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Instances.encounters = new EncounterChain(dataSnapshot);
                    Toast.makeText(getApplicationContext(), "Quest loaded!", Toast.LENGTH_SHORT).show();
                    goToTown(null);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
