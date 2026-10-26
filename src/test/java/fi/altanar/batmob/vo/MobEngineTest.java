package fi.altanar.batmob.vo;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.mythicscape.batclient.interfaces.ParsedResult;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.controller.MobPlugin;
import fi.altanar.batmob.io.IMobListener;
import fi.altanar.batmob.io.ISpellListener;

import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class MobEngineTest {

    private static Mob detect(MobEngine engine, String name) {
        String line = "\u001b[1;32m" + name + "\u001b[0m";
        ParsedResult input = new ParsedResult(line);
        input.setOriginalText(line);
        input.setStrippedText(name);
        return engine.trigger(input);
    }

    private static Mob studied(String shortName, String race) {
        Mob m = new Mob(0, shortName);
        ArrayList<String> names = new ArrayList<String>();
        names.add(shortName);
        m.setShortNames(names);
        m.setRace(race);
        return m;
    }

    @Test
    void testStudyGoesToMatchingMob() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob kiec = detect(engine, "Kiec the bartender");
        Mob guard = detect(engine, "A city guard");

        // the latest detected mob is the guard, but Kiec was studied
        Mob target = engine.applyStudy(studied("Kiec", "half-orc"));

        assertSame(kiec, target);
        assertEquals("half-orc", kiec.getRace());
        assertNull(guard.getRace());
    }

    @Test
    void testStudyWithoutMatchingMob() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob guard = detect(engine, "A city guard");

        assertNull(engine.applyStudy(studied("Kiec", "half-orc")));
        assertNull(guard.getRace());
    }

    @Test
    void testRemovedMobLeavesRoomList() {
        MobStore store = new MobStore();
        MobEngine engine = new MobEngine(mock(MobPlugin.class), store, null);
        store.addListener(engine);
        IMobListener l = mock(IMobListener.class);
        engine.addMobListener(l);

        Mob guard = detect(engine, "A city guard");
        detect(engine, "Kiec the bartender");
        reset(l);

        store.remove(guard);

        assertFalse(engine.getRoomMobs().contains(guard));
        assertEquals(1, engine.getRoomMobs().size());
        verify(l).mobsDetected(engine.getRoomMobs());
    }

    private static void line(MobEngine engine, String text) {
        ParsedResult input = new ParsedResult(text);
        input.setOriginalText(text);
        input.setStrippedText(text);
        engine.trigger(input);
    }

    @Test
    void testPartyMemberSpellsAreIgnored() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        ISpellListener l = mock(ISpellListener.class);
        engine.addSpellListener(l);

        String baroness = "Baroness gets an evil gleam in her eyes and chants 'I HATE MAGIC'";
        String bobby = "Bobby waves his index finger while uttering 'omm zur semen'";

        // no party known yet, both are shown
        line(engine, baroness);
        line(engine, bobby);
        verify(l, times(2)).spellDetected(any(Spell.class));

        line(engine, ",-----------------------------------------------------------------------------.");
        line(engine, "|*1.1   Bobby       ldr   812( 812)  320( 320)  410( 410) |");
        line(engine, "`-----------------------------------------------------------------------------'");
        reset(l);

        line(engine, baroness);
        line(engine, bobby);
        ArgumentCaptor<Spell> captor = ArgumentCaptor.forClass(Spell.class);
        verify(l, times(1)).spellDetected(captor.capture());
        assertEquals("Baroness", captor.getValue().getCaster());
    }
}
