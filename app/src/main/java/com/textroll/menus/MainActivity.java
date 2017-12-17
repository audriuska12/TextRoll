package com.textroll.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.textroll.classes.Instances;
import com.textroll.mechanics.AbilityMap;
import com.textroll.mechanics.QuestLog;
import com.textroll.textroll.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("Exit", false)) {
            finish();
        } else {
            Instances.mDatabase.keepSynced(true);
            mAuth = FirebaseAuth.getInstance();
            Instances.user = mAuth.getCurrentUser();
            setContentView(R.layout.activity_main);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                verifyAuth();
            }
        }, 100);
    }

    private void verifyAuth() {
        if (Instances.user == null) {
            goToLogin();
        }
        updateNameTag();
    }


    public void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void goToDisplayNameSelect() {
        Intent intent = new Intent(this, DisplayNameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void goToCharSelect(View view) {
        if (Instances.abilitySnap == null) {
            Instances.mDatabase.child("abilities").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Instances.abilitySnap = dataSnapshot;
                    Instances.abilityMap = new AbilityMap(Instances.abilitySnap, "Idle");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (Instances.enemySnap == null) {
            Instances.mDatabase.child("enemies").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Instances.enemySnap = dataSnapshot;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (Instances.questLog == null) {
            Instances.mDatabase.child("encounterChains").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Instances.questLog = new QuestLog(dataSnapshot, "Intro");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        Intent intent = new Intent(this, CharSelectActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        mAuth.signOut();
        Instances.user = null;
        Instances.displayName = null;
        Instances.encounters = null;
        Instances.abilityMap = null;
        Instances.pc = null;
        Instances.questLog = null;
        Instances.abilitySnap = null;
        Instances.enemySnap = null;
        goToLogin();
    }

    private void updateNameTag() {

        final TextView lia = (findViewById(R.id.textViewLia));
        if (Instances.displayName == null) {
            Instances.mDatabase.child("users").child(Instances.user.getUid()).child("displayName").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                goToDisplayNameSelect();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lia.setText(String.format("%s %s", getString(R.string.lblLia), dataSnapshot.getValue()));
                                    lia.setVisibility(View.VISIBLE);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lia.setText(String.format("%s %s", getString(R.string.lblLia), Instances.displayName));
                    lia.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
