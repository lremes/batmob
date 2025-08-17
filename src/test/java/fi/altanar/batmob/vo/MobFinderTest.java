package fi.altanar.batmob.vo;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class MobFinderTest {

    @Test
    void testFindByShortName() {
        ArrayList<Mob> mobs = new ArrayList<Mob>();

        Mob m1 = new Mob(100, "test");
        m1.addShortName("test");
        mobs.add(m1);

        Mob m2 = new Mob(100, "Astrax the magnificent");
        m2.addShortName("astrax");
        mobs.add(m2);

        Mob m3 = new Mob(100, "Meno Mahtava");
        m3.addShortName("meno");
        mobs.add(m3);

        Mob found = new MobFinder(mobs).findByShortName(m3);
        assertNotNull(found);
        assertEquals("Meno Mahtava", found.getName());

        found = new MobFinder(mobs).findByShortName(m1);
        assertNotNull(found);
        assertEquals("test", found.getName());

        found = new MobFinder(mobs).findByShortName(m2);
        assertNotNull(found);
        assertEquals("Astrax the magnificent", found.getName());

    }

    @Test
    void testFindByName() {
        ArrayList<Mob> mobs = new ArrayList<Mob>();

        Mob m1 = new Mob(100, "test");
        mobs.add(m1);

        Mob m2 = new Mob(100, "Astrax the magnificent");
        mobs.add(m2);

        Mob m3 = new Mob(100, "Meno Mahtava");
        mobs.add(m3);

        Mob toFind = new Mob(100, "Max Mahtava");
        toFind.addShortName("Meno");

        Mob found = new MobFinder(mobs).findByShortName(toFind);
        assertNotNull(found);
        assertEquals("Meno Mahtava", found.getName());
    }
}
