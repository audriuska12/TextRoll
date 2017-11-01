package com.textroll.classes;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by audri on 2017-10-06.
 */

public class ActorUIContainer {
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

    RelativeLayout layout;
    ProgressBar healthBar;
    Button button;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
}
