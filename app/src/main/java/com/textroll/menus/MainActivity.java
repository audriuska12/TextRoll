package com.textroll.menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.textroll.classes.Instances;
import com.textroll.mechanics.AbilityMap;
import com.textroll.mechanics.QuestLog;
import com.textroll.textroll.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("Exit", false)) {
            finish();
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Instances.mDatabase.keepSynced(true);
        Instances.mAuth = FirebaseAuth.getInstance();
        Instances.user = Instances.mAuth.getCurrentUser();
        verifyAuth();
    }

    private void verifyAuth() {
        if (Instances.user == null) {
            goToLogin(null);
            ((ViewSwitcher) (findViewById(R.id.viewSwitcherMainMenuPlay))).setDisplayedChild(2);
            findViewById(R.id.buttonLogout).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.buttonLogout).setVisibility(View.VISIBLE);
            updateNameTag();
        }
    }


    public void goToLogin(View view) {
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
        if (Instances.itemSnap == null) {
            Instances.mDatabase.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Instances.itemSnap = dataSnapshot;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (Instances.shopSnap == null) {
            Instances.mDatabase.child("shop").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Instances.shopSnap = dataSnapshot;
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
        if (Instances.summonSnap == null) {
            Instances.mDatabase.child("summons").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Instances.summonSnap = dataSnapshot;
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
        Instances.mAuth.signOut();
        Instances.user = null;
        Instances.displayName = null;
        Instances.encounters = null;
        Instances.abilityMap = null;
        Instances.pc = null;
        Instances.questLog = null;
        Instances.abilitySnap = null;
        Instances.enemySnap = null;
        Instances.itemSnap = null;
        Instances.shopSnap = null;
        Instances.summonSnap = null;
        findViewById(R.id.buttonLogout).setVisibility(View.INVISIBLE);
        goToLogin(view);
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
                                    ((ViewSwitcher) (findViewById(R.id.viewSwitcherMainMenuPlay))).setDisplayedChild(1);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Reading display name failed", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lia.setText(String.format("%s %s", getString(R.string.lblLia), Instances.displayName));
                    lia.setVisibility(View.VISIBLE);
                    ((ViewSwitcher) (findViewById(R.id.viewSwitcherMainMenuPlay))).setDisplayedChild(1);
                }
            });
        }
    }
}
