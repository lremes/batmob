package fi.altanar.batmob.gui;

import java.util.Arrays;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fi.altanar.batmob.controller.SearchEngine;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobStore;

public class SearchPanelTest {

    @Test
    void testMatchingAreas() {
        TreeSet<String> areas = new TreeSet<String>(Arrays.asList("Shadowkeep", "Catfolk tree", "Iron mine", "Arelium"));

        assertEquals(Arrays.asList("Catfolk tree", "Shadowkeep"), SearchPanel.matchingAreas(areas, "ee"));
        assertEquals(Arrays.asList("Iron mine"), SearchPanel.matchingAreas(areas, "MINE "));
        assertEquals(4, SearchPanel.matchingAreas(areas, "").size());
        assertTrue(SearchPanel.matchingAreas(areas, "xyz").isEmpty());
    }

    @Test
    void testFilterKeepsTypedText() {
        MobStore store = new MobStore();
        for (String area : new String[] { "Shadowkeep", "Catfolk tree", "Iron mine" }) {
            Mob m = new Mob(0, "mob in " + area);
            m.setArea(area);
            store.store(m);
        }
        SearchPanel panel = new SearchPanel(new SearchEngine(store, null));
        panel.populateSelect();
        assertEquals(Arrays.asList("Catfolk tree", "Iron mine", "Shadowkeep"), panel.getAreaItems());

        panel.setAreaText("ee");
        panel.filterAreas();
        assertEquals(Arrays.asList("Catfolk tree", "Shadowkeep"), panel.getAreaItems());
        assertEquals("ee", panel.getAreaText());

        // refreshing the areas keeps what was typed
        panel.populateSelect();
        assertEquals("ee", panel.getAreaText());

        panel.setAreaText("");
        panel.filterAreas();
        assertEquals(3, panel.getAreaItems().size());
    }
}
