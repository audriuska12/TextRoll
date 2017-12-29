package com.textroll.menus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.textroll.classes.Instances;
import com.textroll.classes.TurnManager;
import com.textroll.mechanics.Action;
import com.textroll.mechanics.Actor;
import com.textroll.mechanics.ActorUIContainer;
import com.textroll.mechanics.Enemy;
import com.textroll.mechanics.Item;
import com.textroll.mechanics.QuestEntry;
import com.textroll.textroll.R;

import java.util.ArrayList;

import static com.textroll.classes.Instances.enemies;
import static com.textroll.classes.Instances.pc;
import static com.textroll.classes.Instances.turnManager;


public class CombatActivity extends AppCompatActivity {

    TextView combatLog;
    ArrayList<Actor> characters;
    ArrayList<Button> buttons;
    Actor target;
    Spinner actionSelect;
    int idCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        buttons = new ArrayList<>();
        addCharacterDisplay((LinearLayout) findViewById(R.id.linearLayoutParty), pc);
        enemies = new ArrayList<>();
        enemies.addAll(Instances.encounters.getCurrentEncounter().getEnemies());
        for (Enemy e : enemies) {
            addCharacterDisplay((LinearLayout) findViewById(R.id.linearLayoutEnemies), e);
        }
        combatLog = findViewById(R.id.combatLog);
        combatLog.setMovementMethod(new ScrollingMovementMethod());
        characters = new ArrayList();
        characters.add(pc);
        characters.addAll(enemies);
        actionSelect = findViewById(R.id.spinnerActionSelect);
        actionSelect.setOnItemSelectedListener(new ActionSelectListener());
        refreshViews();
        deselectTarget();
        turnManager = new TurnManager(characters, this);
        new Thread(turnManager).start();
    }

    public void PlayerTurnStart() {
        populateActionSpinner();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.buttonGo).setClickable(false);
                findViewById(R.id.buttonGo).setAlpha(0.5f);
            }
        });
    }

    public void populateActionSpinner() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Instances.pc.updateAvailableActions();
                ArrayList<Action> actions = new ArrayList<>();
                for (Action a : Instances.pc.getActions()) {
                    try {
                        if (a.validForTarget(Instances.pc, target)) {
                            actions.add(a);
                        }
                    } catch (NullPointerException e) {

                    }
                }
                ArrayAdapter<Action> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, actions);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                actionSelect.setAdapter(adapter);
            }
        });
    }

    public void goClick(View view) {
        pc.setNextAction((Action) actionSelect.getSelectedItem());
        synchronized (Instances.turnManager) {
            Instances.turnManager.notify();
        }
        findViewById(R.id.buttonGo).setClickable(false);
        findViewById(R.id.buttonGo).setAlpha(0.5f);
    }

    public void selectTarget(Actor newTarget) {
        target = newTarget;
        Button btn = findViewById(R.id.buttonGo);
        btn.setClickable(true);
        btn.setAlpha(1f);
    }

    public void deselectTarget() {
        target = null;
        Button btn = findViewById(R.id.buttonGo);
        btn.setClickable(false);
        btn.setAlpha(0.5f);
    }

    public void goToTown(View view) {
        turnManager.kill();
        pc.refresh();
        pc.setUi(null);
        if (Instances.encounters.hasCurrentEncounter()) {
            Instances.encounters.getCurrentEncounter().reset();
        } else {
            Instances.encounters = null;
            Instances.pc.setCurrentQuestEncounterId(0);
            Instances.pc.setCurrentQuestKey(null);
        }
        Intent intent = new Intent(this, TownMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        goToTown(null);
    }

    public void refreshViews() {
        for (Actor c : characters) {
            try {
                c.getUi().getHealthBar().setMax(c.getMaximumHealth());
                c.getUi().getHealthBar().setProgress(c.getCurrentHealth());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void log(String text) {
        combatLog.append(text);
    }

    public Actor getCurrentTarget() {
        return target;
    }

    public void processDeath(final Actor c) {
        if (c == target) deselectTarget();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    LinearLayout layout = (LinearLayout) (findViewById(c.getUi().getId()).getParent());
                    layout.removeView(findViewById(c.getUi().getId()));
                    buttons.remove(c.getUi().getButton());
                    characters.remove(c);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        })
        ;
    }

    private void addCharacterDisplay(LinearLayout layout, Actor actor) {
        actor.setUi(new ActorUIContainer());
        RelativeLayout group = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        group.setLayoutParams(params);
        Button targetButton = new Button(this);
        int buttonId = ++idCounter;
        targetButton.setId(buttonId);
        targetButton.setOnClickListener(new TargetButtonListener(actor));
        targetButton.setText("X");
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(120, 120);
        params1.addRule(RelativeLayout.ALIGN_LEFT);
        targetButton.setLayoutParams(params1);
        buttons.add(targetButton);
        actor.getUi().setButton(targetButton);
        group.addView(targetButton, params1);
        ProgressBar healthBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        healthBar.setId(++idCounter);
        healthBar.setMax(actor.getMaximumHealth());
        healthBar.setProgress(actor.getCurrentHealth());
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 120);
        params2.addRule(RelativeLayout.RIGHT_OF, buttonId);
        healthBar.setLayoutParams(params2);
        actor.getUi().setHealthBar(healthBar);
        group.addView(healthBar, params2);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        TextView nameView = new TextView(this, null);
        nameView.setId(++idCounter);
        params3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        nameView.setLayoutParams(params3);
        nameView.setText(actor.getName());
        actor.getUi().setName(nameView);
        group.addView(nameView);
        actor.getUi().setLayout(group);
        group.setId(++idCounter);
        actor.getUi().setId(group.getId());
        layout.addView(group, params);
    }

    public void win() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                log("You win! \n");
                log(String.format("Gained %d character points and %d gold!\n", Instances.encounters.getCurrentEncounter().getRewardCP(), Instances.encounters.getCurrentEncounter().getRewardG()));
                Instances.pc.addCharacterPoints(Instances.encounters.getCurrentEncounter().getRewardCP());
                Instances.pc.addGold(Instances.encounters.getCurrentEncounter().getRewardG());
                ArrayList<Item> items = Instances.encounters.getCurrentEncounter().getRewardItems();
                if (items.size() > 0) {
                    log("Loot:\n");
                    for (Item item : items) {
                        log(String.format("%s\n", item.getName()));
                        Instances.pc.getInventory().add(item);
                    }
                }
                Instances.encounters.next();
                Instances.pc.getQuests().put(Instances.encounters.getKey(), new QuestEntry(Instances.encounters.getKey(), (Instances.encounters.hasCurrentEncounter() ? Instances.encounters.getCurrentEncounterId() : 0), (Instances.pc.getQuests().get(Instances.encounters.getKey()) != null && Instances.pc.getQuests().get(Instances.encounters.getKey()).completed) || !Instances.encounters.hasCurrentEncounter()));
                Instances.pc.setCurrentQuestEncounterId(Instances.pc.getCurrentQuestEncounterId() + 1);
                ((Button) findViewById(R.id.buttonEndCombat)).setText(R.string.lblVictory);
            }
        });
    }

    public void lose() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                log("You lose... \n");
                ((Button) findViewById(R.id.buttonEndCombat)).setText(R.string.lblDefeat);
            }
        });
    }

    private class TargetButtonListener implements View.OnClickListener {

        Actor actor;

        public TargetButtonListener(Actor actor) {
            this.actor = actor;
        }

        @Override
        public void onClick(View view) {
            view.setClickable(false);
            view.setAlpha(.5f);
            for (View btn : buttons) {
                if (btn != view) {
                    btn.setClickable(true);
                    btn.setAlpha(1f);
                }
            }
            selectTarget(actor);
            populateActionSpinner();
        }
    }

    private class ActionSelectListener implements Spinner.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Button btn = findViewById(R.id.buttonGo);
            Action action = ((Action) (adapterView.getSelectedItem()));
            if (action.isAvailable() && action.validForTarget(Instances.pc, target)) {
                action.setTarget(target);
                btn.setClickable(true);
                btn.setAlpha(1f);
            } else {
                btn.setClickable(false);
                btn.setAlpha(.5f);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Button btn = findViewById(R.id.buttonGo);
            btn.setClickable(false);
            btn.setAlpha(.5f);
        }
    }
}

