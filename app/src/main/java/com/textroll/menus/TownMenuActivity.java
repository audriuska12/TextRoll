package com.textroll.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.textroll.classes.Instances;
import com.textroll.textroll.R;

import java.util.Timer;
import java.util.TimerTask;

public class TownMenuActivity extends AppCompatActivity {

    Timer timerUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town_menu);
        Instances.pc.refresh();
    }

    protected void onResume() {
        super.onResume();
        timerUpdate = new Timer();
        timerUpdate.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateData();
            }
        }, 0, 25);
    }

    private void updateData() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Button btn = findViewById(R.id.buttonFight);
                        if (Instances.encounters == null) {
                            btn.setClickable(false);
                            btn.setAlpha(.5f);
                            btn.setText(R.string.lblLoadEncChain);
                        } else if (!Instances.encounters.hasCurrentEncounter()) {
                            btn.setClickable(false);
                            btn.setAlpha(.5f);
                            btn.setText(R.string.lblNoFight);
                            timerUpdate.cancel();
                        } else {
                            btn.setClickable(true);
                            btn.setAlpha(1f);
                            btn.setText(R.string.lblFight);
                            timerUpdate.cancel();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed(){
        timerUpdate.cancel();
        super.onBackPressed();
        mainMenuActivity(null);
        finish();
    }

    public void saveCharacter(View view) {
        Instances.pc.saveToFirebase();
        Toast.makeText(this, "Saving character...", Toast.LENGTH_SHORT).show();
    }

    public void combatActivity(View view){
        timerUpdate.cancel();
        Intent intent = new Intent(this, CombatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void mainMenuActivity(View view){
        timerUpdate.cancel();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
