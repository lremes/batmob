package fi.altanar.batmob.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MobPluginTest {

    @Test
    void testSearchText() {
        assertEquals("dragon", MobPlugin.searchText("search dragon"));
        assertEquals("big red dragon", MobPlugin.searchText("search big red dragon "));
        assertEquals("", MobPlugin.searchText("search"));
        assertEquals("", MobPlugin.searchText("search "));
    }
}
