package fi.altanar.batmob.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import fi.altanar.batmob.controller.SearchEngine;


public class SearchTest {

    SearchEngine engine;
    MobStore store;

    @BeforeEach
    void setUp() {
        this.store = new MobStore();
        this.engine = new SearchEngine(store);

        Mob m = new Mob(10, "nameTest");
        m.setRace("elf");
        m.setAlignment("good");
        m.setRep("good");
        m.setArea("Area a");
        this.store.store(m);

        m = new Mob(10, "John doe");
        m.setArea("Area a2");
        m.updateExp(10);
        m.setRace("elf");
        m.setAlignment("evil");
        m.setRep("wicked");
        m.setRixx(true);
        m.setArea("Area a2");
        this.store.store(m);


        m = new Mob(10, "John Winston");
        m.updateExp(22);
        m.setRace("elf");
        m.setAlignment("A creature");
        m.setArea("city");
        this.store.store(m);


        m = new Mob(10, "Dark monster");
        m.updateExp(33);
        m.setArea("B city");
        this.store.store(m);


        m = new Mob(10, "Sandman");
        m.updateExp(44);
        m.setRace("human");
        m.setArea("B city");
        this.store.store(m);

    }

    @Test
    void testSearchName() {
        ArrayList<Mob> results = this.engine.search("John ", SearchEngine.SearchCriteria.NAME);
        assertEquals(2, results.size());

        results = this.engine.search("John W", SearchEngine.SearchCriteria.NAME);
        assertEquals(1, results.size());
    }

    @Test
    void testSearchAlignment() {
        ArrayList<Mob> results = this.engine.search("good", SearchEngine.SearchCriteria.ALIGNMENT);
        assertEquals(1, results.size());

        results = this.engine.search("evil", SearchEngine.SearchCriteria.ALIGNMENT);
        assertEquals(1, results.size());
    }

    @Test
    void testSearchArea() {
        ArrayList<Mob> results = this.engine.search("Area a", SearchEngine.SearchCriteria.AREA);
        assertEquals(2, results.size());

        results = this.engine.search("city", SearchEngine.SearchCriteria.AREA);
        assertEquals(3, results.size());

        results = this.engine.search("B cit", SearchEngine.SearchCriteria.AREA);
        assertEquals(2, results.size());
    }

    @Test
    void testSearchRep() {
        ArrayList<Mob> results = this.engine.search("wicked", SearchEngine.SearchCriteria.REP);
        assertEquals(1, results.size());
    }

    @Test
    void testSearchRixx() {
        ArrayList<Mob> results = this.engine.search("true", SearchEngine.SearchCriteria.RIXX);
        assertEquals(1, results.size());
    }

    @Test
    void testSearchRace() {
        ArrayList<Mob> results = this.engine.search("elf", SearchEngine.SearchCriteria.RACE);
        assertEquals(3, results.size());

        results = this.engine.search("human", SearchEngine.SearchCriteria.RACE);
        assertEquals(1, results.size());
    }

}
