package com.textroll.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.textroll.classes.Instances;
import com.textroll.textroll.R;

import java.util.ArrayList;
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
        }, 0, 100);
    }

    private void updateData() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        ViewFlipper flipper = findViewById(R.id.viewFlipperTown);
                        if (Instances.encounters == null) {
                            if (Instances.pc.getCurrentQuestKey() != null) {
                                flipper.setDisplayedChild(0);
                            } else {
                                flipper.setDisplayedChild(2);
                            }
                        } else {
                            flipper.setDisplayedChild(1);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        timerUpdate.cancel();
        super.onBackPressed();
        mainMenuActivity(null);
        finish();
    }

    public void saveCharacter(View view) {
        Instances.pc.saveToFirebase();
        Toast.makeText(this, "Saving character...", Toast.LENGTH_SHORT).show();
    }

    public void combatActivity(View view) {
        timerUpdate.cancel();
        Intent intent = new Intent(this, CombatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void mainMenuActivity(View view) {
        timerUpdate.cancel();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void charEditActivity(View view) {
        timerUpdate.cancel();
        Intent intent = new Intent(this, CharEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void questSelectActivity(View view) {
        timerUpdate.cancel();
        Intent intent = new Intent(this, QuestSelectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void goToInventory(View view) {
        timerUpdate.cancel();
        Intent intent = new Intent(this, InventoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void goToShop(View view) {
        timerUpdate.cancel();
        Intent intent = new Intent(this, ShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
