package com.textroll.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.textroll.classes.Instances;
import com.textroll.classes.encounters.intro.IntroEncounterChain;
import com.textroll.mechanics.Player;
import com.textroll.textroll.R;

public class TownMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town_menu);
        Instances.pc.refresh();
        if (!Instances.encounters.hasCurrentEncounter()) {
            Button btn = findViewById(R.id.buttonFight);
            btn.setClickable(false);
            btn.setAlpha(.5f);
            btn.setText(R.string.lblNoFight);
        } else {
            Button btn = findViewById(R.id.buttonFight);
            btn.setClickable(true);
            btn.setAlpha(1f);
            btn.setText(R.string.lblFight);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        mainMenuActivity(null);
        finish();
    }

    public void saveCharacter(View view) {
        Instances.pc.saveToFirebase();
        Toast.makeText(this, "Saving character...", Toast.LENGTH_SHORT).show();
    }

    public void combatActivity(View view){
        Intent intent = new Intent(this, CombatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void mainMenuActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
