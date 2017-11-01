package com.textroll.classes;

/**
 * Created by audri on 2017-10-30.
 */

public interface Cooldown {
    int getRemainingCooldown();
    void setRemainingCooldown(int cooldown);
    void coolDown();
}
