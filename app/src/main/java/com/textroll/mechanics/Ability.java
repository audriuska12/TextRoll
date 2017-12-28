package com.textroll.mechanics;


public interface Ability {

    void setMaxRank(int rank);

    void changeRank(int change);

    int getCurrentRank();

    void setCurrentRank(int rank);

    int getMaximumRank();

    String getFirebaseName();

    String getStatName();

    String getDescription();
}
