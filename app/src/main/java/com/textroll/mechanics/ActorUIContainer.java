package com.textroll.mechanics;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ActorUIContainer {
    RelativeLayout layout;
    ProgressBar healthBar;
    Button button;
    int id;

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
