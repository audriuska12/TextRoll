package com.textroll.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.textroll.classes.Instances;
import com.textroll.classes.encounters.intro.IntroEncounterChain;
import com.textroll.mechanics.Player;
import com.textroll.textroll.R;

public class ChargenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargen);
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
        Instances.pc = new Player("Player");
        Instances.encounters = new IntroEncounterChain();
        Intent intent = new Intent(this, TownMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
