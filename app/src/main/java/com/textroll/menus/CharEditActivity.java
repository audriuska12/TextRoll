package com.textroll.menus;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.textroll.classes.Instances;
import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Attribute;
import com.textroll.mechanics.Player;
import com.textroll.textroll.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class CharEditActivity extends AppCompatActivity {
    private Player modified;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_edit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(Instances.pc);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            modified = (Player) ois.readObject();
            setListeners();
            updateViews();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToTown(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    public void goToTown(View view) {
        Intent intent = new Intent(this, TownMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void saveCharacter(View view) {
        Instances.pc = modified;
        Instances.pc.saveToFirebase();
        Toast.makeText(this, "Saving character...", Toast.LENGTH_SHORT).show();
    }

    private void updateViews() {
        ((TextView) findViewById(R.id.textViewCharEditCPVal)).setText(String.valueOf(modified.getCharacterPoints()));
        ((TextView) findViewById(R.id.textViewCharEditStrVal)).setText(String.valueOf(modified.getAttributes().getStrength().getBaseValue()));
        ((TextView) findViewById(R.id.textViewCharEditSpdVal)).setText(String.valueOf(modified.getAttributes().getSpeed().getBaseValue()));
        ((TextView) findViewById(R.id.textViewCharEditEndVal)).setText(String.valueOf(modified.getAttributes().getEndurance().getBaseValue()));
        ((TextView) findViewById(R.id.textViewCharEditIntVal)).setText(String.valueOf(modified.getAttributes().getIntelligence().getBaseValue()));
        ((TextView) findViewById(R.id.textViewCharEditMagVal)).setText(String.valueOf(modified.getAttributes().getMagic().getBaseValue()));
        AbilityArrayAdapter abilitiesAv = new AbilityArrayAdapter(CharEditActivity.this, android.R.layout.simple_list_item_1, modified.getAbilities());
        ((ListView) findViewById(R.id.listViewCharEditAbilitiesAvailable)).setAdapter(abilitiesAv);
    }

    private void setListeners() {
        (findViewById(R.id.buttonCharEditStrInc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attribute str = modified.getAttributes().getStrength();
                if (str.getBaseValue() > modified.getCharacterPoints()) {
                    Toast.makeText(getApplicationContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                } else {
                    modified.setCharacterPoints(modified.getCharacterPoints() - str.getBaseValue());
                    str.modifyBase(1);
                    updateViews();
                }
            }
        });
        (findViewById(R.id.buttonCharEditSpdInc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attribute spd = modified.getAttributes().getSpeed();
                if (spd.getBaseValue() > modified.getCharacterPoints()) {
                    Toast.makeText(getApplicationContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                } else {
                    modified.setCharacterPoints(modified.getCharacterPoints() - spd.getBaseValue());
                    spd.modifyBase(1);
                    updateViews();
                }
            }
        });
        (findViewById(R.id.buttonCharEditEndInc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attribute end = modified.getAttributes().getEndurance();
                if (end.getBaseValue() > modified.getCharacterPoints()) {
                    Toast.makeText(getApplicationContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                } else {
                    modified.setCharacterPoints(modified.getCharacterPoints() - end.getBaseValue());
                    end.modifyBase(1);
                    updateViews();
                }
            }
        });
        (findViewById(R.id.buttonCharEditIntInc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attribute intl = modified.getAttributes().getIntelligence();
                if (intl.getBaseValue() > modified.getCharacterPoints()) {
                    Toast.makeText(getApplicationContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                } else {
                    modified.setCharacterPoints(modified.getCharacterPoints() - intl.getBaseValue());
                    intl.modifyBase(1);
                    updateViews();
                }
            }
        });
        (findViewById(R.id.buttonCharEditMagInc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Attribute mag = modified.getAttributes().getMagic();
                if (mag.getBaseValue() > modified.getCharacterPoints()) {
                    Toast.makeText(getApplicationContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                } else {
                    modified.setCharacterPoints(modified.getCharacterPoints() - mag.getBaseValue());
                    mag.modifyBase(1);
                    updateViews();
                }
            }
        });
    }
}