package fi.altanar.batmob.io;

import org.junit.jupiter.api.Test;

import fi.altanar.batmob.vo.Mob;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MediaWikiTest {
    
    @Test
    void testFetchMobInfo() {
        MediaWikiApi api = new MediaWikiApi("https://taikajuoma.ovh/");

        String name = "Darrol Half-plow the master miner";
        Mob result = api.fetchMobInfo(name);

        assertEquals(name, result.getName());
        assertEquals("Be ware of his dangerous special: Darrol Half-plow's antics somehow seem unusually amusing ... hp -298", result.getNotes());

        name = "A colossal guardian mutant";
        result = api.fetchMobInfo(name);

        assertEquals(1, result.getSkills().size());
        assertEquals(5, result.getSpells().size());

        name = "A cougar-furred catfolk warrior patrols through";
        result = api.fetchMobInfo(name);

        assertEquals("good", result.getAlignment());
        assertEquals("catfolk", result.getRace());
        assertEquals("male", result.getGender());

        name = "a young student of Kilrathi is browsing through the books";
        result = api.fetchMobInfo(name);

        assertEquals("evil", result.getAlignment());
        assertEquals("human", result.getRace());
    }

}
