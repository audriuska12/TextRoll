package com.textroll.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.textroll.classes.*;
import com.textroll.classes.Actor;
import com.textroll.textroll.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.textroll.classes.Instances.pc;
import static com.textroll.classes.Instances.turnManager;
import static com.textroll.classes.Instances.enemies;


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
        enemies.add(new Enemy("Enemy 1"));
        enemies.add(new Enemy("Enemy 2"));
        for (Enemy e : enemies) {
            addCharacterDisplay((LinearLayout) findViewById(R.id.linearLayoutEnemies), e);
        }
        combatLog = (TextView) findViewById(R.id.combatLog);
        combatLog.setMovementMethod(new ScrollingMovementMethod());
        characters = new ArrayList();
        characters.add(pc);
        characters.addAll(enemies);
        actionSelect = (Spinner) findViewById(R.id.spinnerActionSelect);
        actionSelect.setOnItemSelectedListener(new ActionSelectListener());
        refreshHealthBars();
        deselectTarget();
        turnManager = new TurnManager(characters, this);
        new Thread(turnManager).start();
    }

    public void PlayerTurnStart() {
        populateActionSpinner();
    }

    public void populateActionSpinner() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Instances.pc.updateAvailableActions(target);
                ArrayList<Action> actions = new ArrayList<>();
                for (Action a : Instances.pc.getActions()) {
                    if(a.validForTarget(Instances.pc, target))actions.add(a);
                }
                ArrayAdapter<Action> adapter = new ArrayAdapter<Action>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, actions);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                actionSelect.setAdapter(adapter);
            }
        });
    }

    public void goClick(View view) {
        pc.setNextAction((Action) actionSelect.getSelectedItem());
        synchronized(Instances.turnManager){
            Instances.turnManager.notify();
        }
    }

    public void selectTarget(Actor newTarget) {
        target = newTarget;
        Button btn = (Button) findViewById(R.id.buttonGo);
        btn.setClickable(true);
        btn.setAlpha(1f);
    }

    public void deselectTarget() {
        target = null;
        Button btn = (Button) findViewById(R.id.buttonGo);
        btn.setClickable(false);
        btn.setAlpha(0.5f);
    }

    public void goToTown(View view) {
        turnManager.kill();
        pc.refresh();
        Intent intent = new Intent(this, TownMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        goToTown(null);
    }

    public void startExitTimer() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                goToTown(null);
            }
        }, 1000);
    }

    public void refreshHealthBars() {
        for (Actor c : characters) {
            c.getUi().getHealthBar().setMax(c.getMaximumHealth());
            c.getUi().getHealthBar().setProgress(c.getCurrentHealth());
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
                LinearLayout layout = (LinearLayout) (findViewById(c.getUi().getId()).getParent());
                layout.removeView(findViewById(c.getUi().getId()));
                buttons.remove(c.getUi().getButton());
                characters.remove(c);
            }
        })
        ;
    }

    private void addCharacterDisplay(LinearLayout layout, Actor actor) {
        RelativeLayout group = new RelativeLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
        actor.getUi().setLayout(group);
        group.setId(++idCounter);
        actor.getUi().setId(group.getId());
        layout.addView(group, params);
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

    private class ActionSelectListener implements Spinner.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Button btn = (Button)findViewById(R.id.buttonGo);
            if (((Action)(adapterView.getSelectedItem())).isAvailable(Instances.pc, target)){
                btn.setClickable(true);
                btn.setAlpha(1f);
            } else {
                btn.setClickable(false);
                btn.setAlpha(.5f);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Button btn = (Button)findViewById(R.id.buttonGo);
            btn.setClickable(false);
            btn.setAlpha(.5f);
        }
    }
}

