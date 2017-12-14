package com.textroll.menus;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.textroll.classes.Instances;
import com.textroll.classes.encounters.intro.IntroEncounterChain;
import com.textroll.mechanics.AttributeContainer;
import com.textroll.mechanics.Player;
import com.textroll.textroll.R;

public class ChargenActivity extends AppCompatActivity {

    int minStat = 3;
    int maxStat = 9;
    int charPoints = 5;
    int str = 5;
    int spd = 5;
    int end = 5;
    int intl = 5;
    int mag = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chargen);
        Instances.mDatabase.keepSynced(true);
        initiateListeners();
        updateValues();
    }

    private void initiateListeners() {
        findViewById(R.id.buttonChargenStrDec).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str > minStat) {
                    str--;
                    charPoints++;
                    updateValues();
                } else {
                    Toast.makeText(getApplicationContext(), "Attribute already at minimum value!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.buttonChargenStrInc).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str < maxStat && charPoints > 0) {
                    str++;
                    charPoints--;
                    updateValues();
                } else {
                    if (str == maxStat) {
                        Toast.makeText(getApplicationContext(), "Attribute already at maximum value!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Out of character points!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        findViewById(R.id.buttonChargenSpdDec).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spd > minStat) {
                    spd--;
                    charPoints++;
                    updateValues();
                } else {
                    Toast.makeText(getApplicationContext(), "Attribute already at minimum value!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.buttonChargenSpdInc).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spd < maxStat && charPoints > 0) {
                    spd++;
                    charPoints--;
                    updateValues();
                } else {
                    if (spd == maxStat) {
                        Toast.makeText(getApplicationContext(), "Attribute already at maximum value!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Out of character points!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        findViewById(R.id.buttonChargenEndDec).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (end > minStat) {
                    end--;
                    charPoints++;
                    updateValues();
                } else {
                    Toast.makeText(getApplicationContext(), "Attribute already at minimum value!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.buttonChargenEndInc).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (end < maxStat && charPoints > 0) {
                    end++;
                    charPoints--;
                    updateValues();
                } else {
                    if (end == maxStat) {
                        Toast.makeText(getApplicationContext(), "Attribute already at maximum value!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Out of character points!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        findViewById(R.id.buttonChargenIntDec).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intl > minStat) {
                    intl--;
                    charPoints++;
                    updateValues();
                } else {
                    Toast.makeText(getApplicationContext(), "Attribute already at minimum value!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.buttonChargenIntInc).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intl < maxStat && charPoints > 0) {
                    intl++;
                    charPoints--;
                    updateValues();
                } else {
                    if (intl == maxStat) {
                        Toast.makeText(getApplicationContext(), "Attribute already at maximum value!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Out of character points!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        findViewById(R.id.buttonChargenMagDec).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mag > minStat) {
                    mag--;
                    charPoints++;
                    updateValues();
                } else {
                    Toast.makeText(getApplicationContext(), "Attribute already at minimum value!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.buttonChargenMagInc).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mag < maxStat && charPoints > 0) {
                    mag++;
                    charPoints--;
                    updateValues();
                } else {
                    if (mag == maxStat) {
                        Toast.makeText(getApplicationContext(), "Attribute already at maximum value!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Out of character points!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void updateValues() {
        ((TextView) findViewById(R.id.textViewChargenAvailablePointsCount)).setText(String.valueOf(charPoints));
        ((TextView) findViewById(R.id.textViewChargenStrVal)).setText(String.valueOf(str));
        ((TextView) findViewById(R.id.textViewChargenSpdVal)).setText(String.valueOf(spd));
        ((TextView) findViewById(R.id.textViewChargenEndVal)).setText(String.valueOf(end));
        ((TextView) findViewById(R.id.textViewChargenIntVal)).setText(String.valueOf(intl));
        ((TextView) findViewById(R.id.textViewChargenMagVal)).setText(String.valueOf(mag));

        Button btn = findViewById(R.id.buttonStart);
        if (charPoints == 0) {
            btn.setAlpha(1);
            btn.setClickable(true);
        } else {
            btn.setAlpha(0.5f);
            btn.setClickable(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelActivity(null);
        finish();
    }

    public void cancelActivity(View view) {
        Intent intent = new Intent(this, CharSelectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void startGameActivity(View view) {
        String name = ((TextView) findViewById(R.id.editTextCharName)).getText().toString();
        if (name.matches("^[A-Za-z ]{3,15}$")) {
            Instances.pc = new Player(name);
            AttributeContainer attributes = Instances.pc.getAttributes();
            attributes.getStrength().setBase(str);
            attributes.getSpeed().setBase(spd);
            attributes.getEndurance().setBase(end);
            attributes.getIntelligence().setBase(intl);
            attributes.getMagic().setBase(mag);
            Instances.encounters = new IntroEncounterChain();
            Intent intent = new Intent(getApplicationContext(), TownMenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Name must be letters and spaces, 3-15 letters long", Toast.LENGTH_SHORT).show();
        }
    }
}
