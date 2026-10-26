package fi.altanar.batmob.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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

        try {
          MobDataPersister.save(tmpDir, store.getSaveObject());
          MobSaveObject saved = MobDataPersister.load(tmpDir);
          store.restoreFromSaveObject(saved);
        } catch (IOException ioe) {
          assertNotNull(ioe);
        } catch (ClassNotFoundException e) {
          assertNotNull(e);
        }

        assertEquals(2, store.getCount());
        assertNotNull(store.get(mob.getName()));
        assertNotNull(store.get(mob2.getName()));
    }

    @Test
    void testRestoreUsesNormalizedNames() {
        HashMap<String, Mob> saved = new HashMap<String, Mob>();
        saved.put("(a guard)", new Mob(10, "(a guard)"));
        saved.put("|broken", new Mob(10, "broken"));

        MobStore store = new MobStore();
        store.restoreFromSaveObject(new MobSaveObject(saved));

        assertEquals(1, store.getCount());
        Mob guard = store.get("a guard");
        assertNotNull(guard);
        assertTrue(store.contains(guard));
    }

    @Test
    void testSaveReplacesOldData(@TempDir File dir) throws Exception {
        MobStore store = new MobStore();
        store.store(new Mob(100, "first"));
        MobDataPersister.save(dir.getPath(), store.getSaveObject());

        store.store(new Mob(100, "second"));
        MobDataPersister.save(dir.getPath(), store.getSaveObject());

        MobStore loaded = new MobStore();
        loaded.restoreFromSaveObject(MobDataPersister.load(dir.getPath()));
        assertEquals(2, loaded.getCount());

        // only the data file is left behind
        String[] files = new File(dir, "conf").list();
        assertArrayEquals(new String[] { "mobData.conf" }, files);
    }

    @Test
    void testSaveObjectIsACopy() {
        MobStore store = new MobStore();
        store.store(new Mob(100, "first"));
        MobSaveObject saved = store.getSaveObject();

        store.store(new Mob(100, "second"));
        assertEquals(1, saved.getData().size());
    }
}
