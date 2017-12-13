package com.textroll.mechanics;

import com.google.firebase.database.DatabaseReference;

public interface Ability {

    void setMaxRank(int rank);

    void changeRank(int change);

    int getCurrentRank();

    void setCurrentRank(int rank);

    int getMaximumRank();

    String getFirebaseName();
}
