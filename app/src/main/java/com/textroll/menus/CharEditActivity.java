package com.textroll.menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Ability;
import com.textroll.mechanics.AbilityNode;
import com.textroll.mechanics.Attribute;
import com.textroll.mechanics.Player;
import com.textroll.textroll.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CharEditActivity extends AppCompatActivity {
    private Player modified;
    private Ability selectedAbility;

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
        ArrayList<Ability> jointAbilities = new ArrayList<>();
        jointAbilities.addAll(modified.getAbilities());
        jointAbilities.addAll(modified.getPassives());
        AbilityArrayAdapter abilitiesPc = new AbilityArrayAdapter(CharEditActivity.this, android.R.layout.simple_list_item_1, jointAbilities);
        ((ListView) findViewById(R.id.listViewCharEditAbilitiesPc)).setAdapter(abilitiesPc);
        AbilityArrayAdapter abilitiesAv = new AbilityArrayAdapter(CharEditActivity.this, android.R.layout.simple_list_item_1, Instances.abilityMap.getAvailableAbilities(modified));
        ((ListView) findViewById(R.id.listViewCharEditAbilitiesAvailable)).setAdapter(abilitiesAv);
        if (selectedAbility == null) {
            findViewById(R.id.constraintLayoutCharEditAbilityInfo).setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.textViewCharEditCostVal)).setText("");
            ((TextView) findViewById(R.id.textViewCharEditDescVal)).setText("");
            findViewById(R.id.viewSwitcherCharEditPU).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) (findViewById(R.id.textViewCharEditDescVal))).setText(selectedAbility.getDescription());
        }
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

        ((ListView) (findViewById(R.id.listViewCharEditAbilitiesPc))).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((ViewSwitcher) (findViewById(R.id.viewSwitcherCharEditPU))).setDisplayedChild(1);
                selectedAbility = (Ability) adapterView.getItemAtPosition(i);
                ((TextView) (findViewById(R.id.textViewCharEditDescVal))).setText(selectedAbility.getDescription());
                AbilityNode node = Instances.abilityMap.getAbilities().get(selectedAbility.getClass().getSimpleName());
                ((TextView) (findViewById((R.id.textViewCharEditCostVal)))).setText(String.valueOf(node.getBaseCost() + selectedAbility.getCurrentRank() * node.getCostPerRank()));
                Button upgradeButton = findViewById(R.id.buttonCharEditUpgrade);
                if (selectedAbility.getCurrentRank() < selectedAbility.getMaximumRank()) {
                    upgradeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AbilityNode node = Instances.abilityMap.getAbilities().get(selectedAbility.getClass().getSimpleName());
                            if (modified.getCharacterPoints() >= node.getBaseCost() + selectedAbility.getCurrentRank() * node.getCostPerRank()) {
                                modified.setCharacterPoints(modified.getCharacterPoints() - (node.getBaseCost() + selectedAbility.getCurrentRank() * node.getCostPerRank()));
                                selectedAbility.setCurrentRank(selectedAbility.getCurrentRank() + 1);
                                ((TextView) (findViewById((R.id.textViewCharEditCostVal)))).setText(String.valueOf(node.getBaseCost() + selectedAbility.getCurrentRank() * node.getCostPerRank()));
                                updateViews();
                            } else {
                                Toast.makeText(getApplicationContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    upgradeButton.setClickable(true);
                    upgradeButton.setAlpha(1);
                } else {
                    upgradeButton.setClickable(false);
                    upgradeButton.setAlpha(0.5f);
                }
                findViewById(R.id.viewSwitcherCharEditPU).setVisibility(View.VISIBLE);
                findViewById(R.id.constraintLayoutCharEditAbilityInfo).setVisibility(View.VISIBLE);
            }
        });

        ((ListView) (findViewById(R.id.listViewCharEditAbilitiesAvailable))).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((ViewSwitcher) (findViewById(R.id.viewSwitcherCharEditPU))).setDisplayedChild(0);
                selectedAbility = (Ability) adapterView.getItemAtPosition(i);
                ((TextView) (findViewById(R.id.textViewCharEditDescVal))).setText(selectedAbility.getDescription());
                AbilityNode node = Instances.abilityMap.getAbilities().get(selectedAbility.getClass().getSimpleName());
                ((TextView) (findViewById((R.id.textViewCharEditCostVal)))).setText(String.valueOf(node.getBaseCost()));
                Button purchaseButton = findViewById(R.id.buttonCharEditPurchase);
                purchaseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AbilityNode node = Instances.abilityMap.getAbilities().get(selectedAbility.getClass().getSimpleName());
                        if (modified.getCharacterPoints() >= node.getBaseCost()) {
                            modified.addAbility(selectedAbility);
                            modified.setCharacterPoints(modified.getCharacterPoints() - node.getBaseCost());
                            ((ViewSwitcher) (findViewById(R.id.viewSwitcherCharEditPU))).setDisplayedChild(1);
                            selectedAbility = null;
                            findViewById(R.id.constraintLayoutCharEditAbilityInfo).setVisibility(View.VISIBLE);
                            updateViews();
                        } else {
                            Toast.makeText(getApplicationContext(), "Not enough points!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                findViewById(R.id.viewSwitcherCharEditPU).setVisibility(View.VISIBLE);
                findViewById(R.id.constraintLayoutCharEditAbilityInfo).setVisibility(View.VISIBLE);
            }
        });
    }
}