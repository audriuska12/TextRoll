package com.textroll.classes;

/**
 * Created by audri on 2017-10-22.
 */

public interface Ability {
    void setCurrentRank(int rank);
    void setMaxRank(int rank);
    void changeRank(int change);
    int getCurrentRank();
    int getMaximumRank();
}
