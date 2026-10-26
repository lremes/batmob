package fi.altanar.batmob.gui;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.controller.MobPlugin;
import fi.altanar.batmob.controller.SearchEngine;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobStore;

import static org.mockito.Mockito.*;

public class DetailsPanelTest {

    @Test
    void testParseList() {
        assertEquals(Arrays.asList("a", "b c", "d"), DetailsPanel.parseList(" a, b c ,,d,"));
        assertTrue(DetailsPanel.parseList("").isEmpty());
        assertTrue(DetailsPanel.parseList(" , ").isEmpty());
    }

    @Test
    void testSaveKeepsFields() {
        MobStore store = new MobStore();
        MobEngine engine = new MobEngine(mock(MobPlugin.class), store, null);
        DetailsPanel panel = new DetailsPanel(engine, new SearchEngine(store, null));

        Mob mob = new Mob(100, "Kiec the bartender");
        mob.setGender("male");
        mob.setShortNames(new ArrayList<String>(Arrays.asList("kiec", "bartender")));
        mob.setSpells(new ArrayList<String>(Arrays.asList("magic missile", "shocking grasp")));
        store.store(mob);

        // saving without edits must not change anything
        panel.mobSelected(mob);
        panel.saveMob();

        assertEquals("male", mob.getGender());
        assertEquals(Arrays.asList("kiec", "bartender"), mob.getShortNames());
        assertEquals(Arrays.asList("magic missile", "shocking grasp"), mob.getSpells());
    }
}
