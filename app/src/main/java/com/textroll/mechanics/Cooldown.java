package com.textroll.mechanics;


public interface Cooldown {
    int getRemainingCooldown();
    void setRemainingCooldown(int cooldown);
    void coolDown();
}
