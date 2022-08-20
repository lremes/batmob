package fi.altanar.batmob.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import fi.altanar.batmob.io.MobDataPersister;

public class MobSaveTest {

    @Test
    void testSaveAndLoad() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        Mob mob = new Mob(100, "test");
        Mob mob2 = new Mob(101, "test2");

        MobStore store = new MobStore();
        store.store(mob);
        store.store(mob2);

        MobDataPersister.save(tmpDir, store.getSaveObject());
        MobSaveObject saved = MobDataPersister.load(tmpDir);
        store.restoreFromSaveObject(saved);

        assertEquals(2, store.getCount());
        assertNotNull(store.get(mob.getName()));
        assertNotNull(store.get(mob2.getName()));
    }
}
