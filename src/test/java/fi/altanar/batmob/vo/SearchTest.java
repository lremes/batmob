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
        this.engine = new SearchEngine(store, null);

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
        m.setZinium(true);
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
        MobFilter f = new MobFilter();
        f.name = "John ";

        ArrayList<Mob> results = this.engine.search(f);
        assertEquals(2, results.size());

        f.name = "John W";
        results = this.engine.search(f);
        assertEquals(1, results.size());
    }

    @Test
    void testSearchAlignment() {
        MobFilter f = new MobFilter();
        f.alignment = "good";

        ArrayList<Mob> results = this.engine.search(f);
        assertEquals(1, results.size());

        f.alignment = "evil";

        results = this.engine.search(f);
        assertEquals(1, results.size());
    }

    @Test
    void testSearchArea() {
        MobFilter f = new MobFilter();
        f.area = "city";

        ArrayList<Mob> results = this.engine.search(f);
        assertEquals(3, results.size());

        f.area = "B city";
        results = this.engine.search(f);
        assertEquals(2, results.size());

        f.area = "area";
        results = this.engine.search(f);
        assertEquals(2, results.size());
    }


    @Test
    void testSearcZinium() {
        MobFilter f = new MobFilter();
        f.isZinium = true;

        ArrayList<Mob> results = this.engine.search(f);
        assertEquals(1, results.size());
    }

    @Test
    void testSearchRace() {
        MobFilter f = new MobFilter();
        f.race = "elf";

        ArrayList<Mob> results = this.engine.search(f);
        assertEquals(3, results.size());

        f.race = "human";

        results = this.engine.search(f);
        assertEquals(1, results.size());
    }

    @Test
    void testSearchNameAndArea() {
        MobFilter f = new MobFilter();
        f.name = "John";
        f.area = "Area a2";
        ArrayList<Mob> results = this.engine.search(f);
        assertEquals(1, results.size());
    }

}
