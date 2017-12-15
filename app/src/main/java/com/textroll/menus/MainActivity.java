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
import com.textroll.mechanics.QuestLog;
import com.textroll.textroll.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Timer timerAuthVerifier;

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
        timerAuthVerifier = new Timer();
        timerAuthVerifier.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                verifyAuth();
            }
        }, 0, 25);
    }

    private void verifyAuth() {
        if (Instances.user == null) {
            goToLogin();
        } else {
            timerAuthVerifier.cancel();
            updateNameTag();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int returnCode, Intent data) {
        if (requestCode == 1) {
            if (returnCode == RESULT_CANCELED) {
                goToLogin();
            } else {
                updateNameTag();
            }
        }
        if (requestCode == 2) {
            if (returnCode == RESULT_CANCELED) {
                goToDisplayNameSelect();
            } else {
                updateNameTag();
            }
        }
    }

    public void goToLogin() {
        timerAuthVerifier.cancel();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    public void goToDisplayNameSelect() {
        timerAuthVerifier.cancel();
        Intent intent = new Intent(this, DisplayNameActivity.class);
        startActivityForResult(intent, 2);
    }

    public void goToCharSelect(View view) {
        timerAuthVerifier.cancel();
        if (Instances.abilitySnap == null) {
            Instances.mDatabase.child("abilities").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Instances.abilitySnap = dataSnapshot;
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
        goToLogin();
    }

    private void updateNameTag() {
        final TextView lia = (findViewById(R.id.textViewLia));
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("displayName").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            goToDisplayNameSelect();
                        }
                        lia.setText(String.format("%s %s", getString(R.string.lblLia), dataSnapshot.getValue()));
                        lia.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }
}
