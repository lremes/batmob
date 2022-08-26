package fi.altanar.batmob.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import fi.altanar.batmob.controller.SearchEngine;
import fi.altanar.batmob.controller.SearchEngine.SearchCriteria;


public class SearchTest {

    SearchEngine engine;
    MobStore store;

    @BeforeEach
    void setUp() {
        this.store = new MobStore();
        this.engine = new SearchEngine(store);

        Mob m = new Mob(10, "nameTest");
        m.setArea("Area a");
        m.setRace("elf");
        m.setAlignment("good");
        m.setRep("good");
        this.store.store(m);

        m = new Mob(10, "John doe");
        m.setArea("Area a2");
        m.setExp(10);
        m.setRace("elf");
        m.setAlignment("evil");
        m.setRep("wicked");
        m.setRixx(true);
        this.store.store(m);


        m = new Mob(10, "John Winston");
        m.setExp(22);
        m.setRace("elf");
        m.setAlignment("A creature");
        this.store.store(m);


        m = new Mob(10, "Dark monster");
        m.setArea("B city");
        m.setExp(33);
        this.store.store(m);


        m = new Mob(10, "Sandman");
        m.setExp(44);
        m.setArea("B city");
        m.setRace("human");
        this.store.store(m);

    }

    @Test
    void testSearchName() {
        ArrayList<Mob> results = this.engine.search("John ", SearchEngine.SearchCriteria.NAME);
        assertEquals(2, results.size());

        results = this.engine.search("John W", SearchEngine.SearchCriteria.NAME);
        assertEquals(1, results.size());
    }

}
