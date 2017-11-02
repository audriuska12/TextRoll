package com.textroll.classes.encounters.intro;

import com.textroll.classes.enemies.TrainingDummy;
import com.textroll.mechanics.Encounter;
import com.textroll.mechanics.EncounterChain;
import com.textroll.mechanics.Enemy;

import java.util.ArrayList;

public class IntroEncounterChain extends EncounterChain {
    public IntroEncounterChain() {
        ArrayList<Enemy> enemiesEnc1 = new ArrayList<>();
        enemiesEnc1.add(new TrainingDummy());
        Encounter encounterOne = new Encounter(enemiesEnc1);
        addEncounter(encounterOne);
        ArrayList<Enemy> enemiesEnc2 = new ArrayList<>();
        enemiesEnc2.add(new TrainingDummy());
        enemiesEnc2.add(new TrainingDummy());
        Encounter encounterTwo = new Encounter(enemiesEnc2);
        addEncounter(encounterTwo);
    }
}
