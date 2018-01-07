package com.textroll.mechanics;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActorUIContainer {
    private RelativeLayout layout;
    private ProgressBar healthBar;
    private Button button;
    private int id;
    private TextView name;

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public RelativeLayout getLayout() {
        return layout;
    }

    public void setLayout(RelativeLayout layout) {
        this.layout = layout;
    }

    public ProgressBar getHealthBar() {
        return healthBar;
    }

    public void setHealthBar(ProgressBar healthBar) {
        this.healthBar = healthBar;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
