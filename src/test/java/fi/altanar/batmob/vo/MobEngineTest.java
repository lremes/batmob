package fi.altanar.batmob.vo;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.mythicscape.batclient.interfaces.ParsedResult;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void testControlCodeLinesAreLogged(@TempDir File dir) throws Exception {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        engine.setBaseDir(dir.getPath());
        engine.setLogControlCodes(true);

        String battle = "\u001b<10spec_battle\u001b|You hit the orc.\u001b>10";
        String exits = "Obvious exits are: \u001b[1m\u001b<31n\u001b|n\u001b>31\u001b[0m.";
        line(engine, battle);
        line(engine, "\u001b[1;32mA plain green line\u001b[0m");
        line(engine, exits);

        File[] logs = new File(dir, "logs").listFiles((d, name) -> name.endsWith("-batmob-controlcodes.txt"));
        assertEquals(1, logs.length);

        List<String> logged = Files.readAllLines(logs[0].toPath(), StandardCharsets.UTF_8);
        assertEquals(2, logged.size());
        assertTrue(logged.get(0).endsWith(" line " + battle));
        assertTrue(logged.get(1).endsWith(" line " + exits));
    }

    @Test
    void testControlCodeLoggingIsOffByDefault(@TempDir File dir) {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        engine.setBaseDir(dir.getPath());

        line(engine, "\u001b<10spec_battle\u001b|You hit the orc.\u001b>10");
        engine.logControlCode("proto99 BAT_MAPPER;;arelium");

        assertFalse(new File(dir, "logs").exists());
    }

    @Test
    void testCombatLinksShortName() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob fox = detect(engine, "a skinned fox limps here");
        Mob rat = detect(engine, "A rat");

        line(engine, "Skinned fox is in very bad shape (20%).");
        assertEquals(Arrays.asList("Skinned fox"), fox.getShortNames());
        assertNull(rat.getShortNames());

        // the same name again doesn't duplicate it
        line(engine, "Skinned fox is DEAD, R.I.P.");
        assertEquals(1, fox.getShortNames().size());

        // a name in the middle of a word doesn't match
        line(engine, "Ra is slightly hurt (75%).");
        assertNull(rat.getShortNames());
    }

    @Test
    void testCombatShortNameNotLinkedWhenAmbiguous() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob guard1 = detect(engine, "A city guard");
        Mob guard2 = detect(engine, "A sleepy guard");

        line(engine, "Guard is in a good shape (90%).");

        assertNull(guard1.getShortNames());
        assertNull(guard2.getShortNames());
    }

    @Test
    void testPartyMemberHealthIsNotLinked() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob mob = detect(engine, "Brurin the giant spider");

        line(engine, ",-----------------------------------------------------------------------------.");
        line(engine, "| 1.1  Brurin         fol 1593(1593)  169( 169) 442(442) | 100 |        69933 |");
        line(engine, "`-----------------------------------------------------------------------------'");
        line(engine, "Brurin is slightly hurt (75%).");

        assertNull(mob.getShortNames());
    }

    @Test
    void testMonsterSpellsAreRecorded() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob mayor = detect(engine, "the Mayor of Arelium");
        Mob rat = detect(engine, "A rat");

        // from combatmessages.txt, caster "Mayor"
        String cast = new ArrayList<String>(readLines("combatmessages.txt")).stream()
                .filter(l -> l.startsWith("Mayor ")).findFirst().get();
        line(engine, cast);
        line(engine, cast);

        assertEquals(1, mayor.getSpells().size());
        assertTrue(rat.getSpells().isEmpty());
    }

    @Test
    void testSpellsOfUnknownCastersAreNotRecorded() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob rat = detect(engine, "A rat");

        line(engine, "Bobby waves his index finger while uttering 'omm zur semen'");

        assertTrue(rat.getSpells().isEmpty());
    }

    private static List<String> readLines(String resource) {
        try {
            return Files.readAllLines(new File(ClassLoader.getSystemResource(resource).toURI()).toPath(),
                    StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testNamedMonsterSpellsAreRecorded() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob guard = detect(engine, "A guard of the temple patrolling in the garden");
        Mob rapunzel = detect(engine, "Rapunzel is combing her hair");

        line(engine, "Guard waves his index finger while uttering (firebolt)");
        line(engine, "Rapunzel claps her hands and says (acid blast)");
        line(engine, "Rapunzel claps her hands and says (acid blast)");

        assertEquals(Arrays.asList("firebolt"), guard.getSpells());
        assertEquals(Arrays.asList("acid blast"), rapunzel.getSpells());
    }

    @Test
    void testStunnedHealthLinksShortName() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob maggot = detect(engine, "A huge maggot crawls here");

        line(engine, "Huge Maggot is not in a good shape (45%) and STUNNED.");

        assertEquals(Arrays.asList("Huge Maggot"), maggot.getShortNames());
    }

    @Test
    void testMonsterSkillsAreRecorded() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob miner = detect(engine, "A smoking miner is resting here");
        Mob rat = detect(engine, "A rat");

        line(engine, "Smoking miner performs a fast kick at Arasaka's stomach, leaving foe gasping for air.");
        line(engine, "Smoking miner kicks you in the groin very hard. You gasp with pain and double up.");
        line(engine, "Smoking miner pummels Arasaka in the jaw!");
        line(engine, "Smoking miner paralyzes Arasaka with his mind.");

        assertEquals(Arrays.asList("kick", "pummel"), miner.getSkills());
        assertEquals(Arrays.asList("paralyze"), miner.getSpells());
        assertTrue(rat.getSkills().isEmpty());
    }

    @Test
    void testPartyMemberSkillsAreNotRecorded() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob brurin = detect(engine, "Brurin the giant spider");

        line(engine, ",-----------------------------------------------------------------------------.");
        line(engine, "| 1.1  Brurin         fol 1593(1593)  169( 169) 442(442) | 100 |        69933 |");
        line(engine, "`-----------------------------------------------------------------------------'");
        line(engine, "Brurin bashes his foe in berserk rage!!");

        assertTrue(brurin.getSkills().isEmpty());
    }

    @Test
    void testGrappleAndSpecialsAreRecorded() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob khalun = detect(engine, "Khalun the brawler");

        line(engine, "Khalun starts grappling Arasaka.");
        line(engine, "Then, Khalun grasps Arasaka by the hair and pummels Arasaka's head with maniacal rage.");

        assertEquals(Arrays.asList("grapple", "special: hair grab"), khalun.getSkills());
    }

    @Test
    void testAreaSpellsAreRecorded() {
        MobEngine engine = new MobEngine(mock(MobPlugin.class), new MobStore(), null);
        Mob devil = detect(engine, "A snow devil whirls around");

        line(engine, "Snow devil hits you, Croesus and Online with its hailstorm.");

        assertEquals(Arrays.asList("hailstorm"), devil.getSpells());
    }
}
