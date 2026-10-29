package fi.altanar.batmob.vo;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.mythicscape.batclient.interfaces.ParsedResult;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.controller.MobPlugin;
import fi.altanar.batmob.controller.RegexTrigger;
import fi.altanar.batmob.io.IMobListener;

import static org.mockito.Mockito.*;

public class MobTest {

  @Test
  void testPkills() {
    RegexTrigger tm = new RegexTrigger();

    int[] expValues = new int[4];

    try {
      URL testFileURL = ClassLoader.getSystemResource("pkills.txt");
      File myFile = new File(testFileURL.toURI());
      Scanner myReader = new Scanner(myFile);
      int i = 0;
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        Mob mob = (Mob) tm.process(data);
        assertNotNull(mob);
        expValues[i++] = mob.getExp();
      }
      myReader.close();

      testFileURL = ClassLoader.getSystemResource("pkills_update.txt");
      myFile = new File(testFileURL.toURI());
      myReader = new Scanner(myFile);
      i = 0;
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        Mob mob = (Mob) tm.process(data);
        assertNotNull(mob);
        assertNotEquals(expValues[i++], mob.getExp());
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testStore() {
    RegexTrigger tm = new RegexTrigger();
    MobStore store = new MobStore();

    try {
      URL testFileURL = ClassLoader.getSystemResource("pkills.txt");
      File myFile = new File(testFileURL.toURI());
      Scanner myReader = new Scanner(myFile);
      int i = 0;
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        Mob mob = (Mob) tm.process(data);
        store.store(mob);
        // System.out.println(mob);
      }
      myReader.close();

      testFileURL = ClassLoader.getSystemResource("pkills_update.txt");
      myFile = new File(testFileURL.toURI());
      myReader = new Scanner(myFile);
      i = 0;
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        Mob mob = (Mob) tm.process(data);

        mob = store.updateAutofilledFields(mob);
        // System.out.println(mob);
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testAreaUpdate() {
    Mob mob = new Mob(123, "test");
    MobStore store = new MobStore();

    assertFalse(store.store(mob));

    mob.setArea("test-area");

    store.updateAutofilledFields(mob);

    Mob expected = store.get(mob.getName());
    assertEquals(expected.getArea(), "test-area");
  }

  @Test
  void testEditableUpdate() {
    Mob mob = new Mob(123, "test");
    MobStore store = new MobStore();

    assertFalse(store.store(mob));

    mob.setAlignment("alignment");
    mob.setRace("race");
    mob.setNotes("notes");
    mob.setRep("rep");
    mob.setZinium(true);

    ArrayList<String> s = new ArrayList<String>();
    s.add("s1");
    s.add("s2");
    mob.setSpells(s);
    mob.setSkills(s);
    mob.setShortNames(s);

    Mob expected = store.updateAutofilledFields(mob);

    assertEquals(expected.getAlignment(), "alignment");
    assertEquals(expected.getRace(), "race");
    assertEquals(expected.getSkills(), s);
    assertEquals(expected.getSpells(), s);
    assertEquals(expected.getShortNames(), s);
    assertEquals(expected.getNotes(), "notes");
    assertEquals(expected.getRep(), "rep");
    assertTrue(expected.isZinium());
  }

  @Test
  void testTriggers() {
    try {
      URL testFileURL = ClassLoader.getSystemResource("mobs.txt");

      MobPlugin plugin = mock(MobPlugin.class);
      IMobListener l = mock(IMobListener.class);
      MobStore store = new MobStore();
      MobEngine engine = new MobEngine(plugin, store, null);
      engine.addMobListener(l);

      File myFile = new File(testFileURL.toURI());
      Scanner myReader = new Scanner(myFile);
      while (myReader.hasNextLine()) {
        String line = myReader.nextLine();
        ParsedResult input = new ParsedResult(line);
        String stripped = line.replaceAll("\u001b\\[\\d;\\d\\dm", "");
        input.setOriginalText(line);
        input.setStrippedText(stripped);
        engine.trigger(input);
      }
      myReader.close();

      assertEquals(13, engine.getMobStore().getCount());
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testIgnore() {
    try {
      URL testFileURL = ClassLoader.getSystemResource("ignored.txt");

      MobPlugin plugin = mock(MobPlugin.class);
      MobStore store = new MobStore();
      MobEngine engine = new MobEngine(plugin, store, null);

      File myFile = new File(testFileURL.toURI());
      Scanner myReader = new Scanner(myFile);
      while (myReader.hasNextLine()) {
        String line = myReader.nextLine();
        ParsedResult input = new ParsedResult(line);
        String stripped = line.replaceAll("\u001b\\[\\d;\\d\\dm|\u001b\\[0m", "");

        input.setOriginalText(line);
        input.setStrippedText(stripped);
        assertNull(engine.trigger(input));
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testContains() {
    try {
      URL testFileURL = ClassLoader.getSystemResource("mobs.txt");

      MobPlugin plugin = mock(MobPlugin.class);
      IMobListener l = mock(IMobListener.class);
      MobStore store = new MobStore();
      MobEngine engine = new MobEngine(plugin, store, null);
      engine.addMobListener(l);

      File myFile = new File(testFileURL.toURI());
      Scanner myReader = new Scanner(myFile);
      while (myReader.hasNextLine()) {
        String line = myReader.nextLine();
        ParsedResult input = new ParsedResult(line);
        String stripped = line.replaceAll("\u001b\\[\\d;\\d\\dm", "");
        input.setOriginalText(line);
        input.setStrippedText(stripped);
        if (!stripped.isEmpty()) {
          Mob m = engine.trigger(input);
          assertTrue(store.contains(m.getName()));
          assertTrue(store.contains(m));
        }
      }
      myReader.close();

    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  @Test
  void testNormalize() {
    Mob m = new Mob(0, " simple ");
    assertEquals(m.getName(), "simple");

    m = new Mob(0, "thisisaverylongnamethatshouldbecutshortbythenormalization1234567890");
    assertEquals(m.getName(), "thisisaverylongnamethatshouldbecutshortbythenormalizatio");

    m = new Mob(0, "mobname (undead)");
    assertEquals(m.getName(), "mobname");

    m = new Mob(0, "mobname (bleeding)");
    assertEquals(m.getName(), "mobname");

    m = new Mob(0, "mobname <wrapped>");
    assertEquals(m.getName(), "mobname");

    m = new Mob(0, "mobname <-=wrapped=->");
    assertEquals(m.getName(), "mobname");

    m = new Mob(0, "(invis mob)");
    assertEquals(m.getName(), "invis mob");

  }

  @Test
  void testNormalizeToEmpty() {
    assertEquals("", new Mob(0, "(undead)").getName());
    assertEquals("", new Mob(0, "<encircled>").getName());
    assertEquals("", Mob.normalizeName("()"));
    assertNull(new MobStore().get("<encircled>"));
  }

  @Test
  void testTriggerIgnoresNamesThatNormalizeToEmpty() {
    MobStore store = new MobStore();
    MobEngine engine = new MobEngine(mock(MobPlugin.class), store, null);

    String line = "\u001b[1;32m(undead)\u001b[0m";
    ParsedResult input = new ParsedResult(line);
    input.setOriginalText(line);
    input.setStrippedText("(undead)");

    assertNull(engine.trigger(input));
    assertEquals(0, store.getCount());
    assertTrue(engine.getRoomMobs().isEmpty());
  }

  @Test
  void testUpdateFromWiki() {
    Mob wiki = new Mob(0, "orc");
    wiki.setNotes("wiki notes");
    wiki.setRace("orc");

    // own notes are kept, missing gender doesn't break it
    Mob m = new Mob(0, "orc");
    m.setNotes("my notes");
    m.updateFromWiki(wiki);
    assertEquals("my notes", m.getNotes());
    assertEquals("orc", m.getRace());

    // empty notes are filled
    m = new Mob(0, "orc");
    m.setNotes("");
    m.setGender("male");
    m.updateFromWiki(wiki);
    assertEquals("wiki notes", m.getNotes());
    assertEquals("male", m.getGender());
  }

  @Test
  void testNameTags() {
    // real names from the session logs, kill list names are without the tags
    assertEquals("a mean ghost of sabretooth tiger", new Mob(0, "a mean ghost of sabretooth tiger (undead)").getName());
    assertEquals("a skinny ghost of norrfolk", new Mob(0, "a skinny ghost of norrfolk (undead) <-=wrapped=->").getName());
    assertEquals("an adept cleric", new Mob(0, "an adept cleric (in excellent shape, 100%)").getName());
    assertEquals("Yani, papa troll", new Mob(0, "Yani, papa troll (in excellent shape)").getName());
    assertEquals("an HUGE black panther", new Mob(0, "an HUGE black panther (in very bad shape)").getName());
    assertEquals("A priest in a white robe", new Mob(0, "A priest in a white robe [host]").getName());
    assertEquals("Seceh, the General of the Legion", new Mob(0, "Seceh, the General of the Legion (cursed)").getName());
    assertEquals("Dark Knight is grinning here", new Mob(0, "Dark Knight is grinning here <blue aura>").getName());

    // unknown tags are part of the name
    assertEquals("A fierce fire dragon <burning>", new Mob(0, "A fierce fire dragon <burning>").getName());

    assertEquals(Arrays.asList("(undead)", "<-=wrapped=->"), Mob.nameTags("a ghost (undead) <-=wrapped=->"));
    assertTrue(Mob.nameTags("a ghost").isEmpty());
  }

  @Test
  void testTagsSetFlags() {
    Mob m = new Mob(0, "The queen is pacing around, looking for something to do <cyan aura>");
    assertEquals("cyan aura", m.getAura());
    assertFalse(m.isUndead());

    m = new Mob(0, "a mean ghost (undead) <blue aura>");
    assertEquals("blue aura", m.getAura());
    assertTrue(m.isUndead());

    assertNull(new Mob(0, "a rat").getAura());
  }

  @Test
  void testAuraOfKnownMobIsUpdated() {
    MobStore store = new MobStore();
    MobEngine engine = new MobEngine(mock(MobPlugin.class), store, null);

    String[] seen = { "Dark Knight is grinning here", "Dark Knight is grinning here <blue aura>" };
    for (String name : seen) {
      String line = "\u001b[1;31m" + name + "\u001b[0m";
      ParsedResult input = new ParsedResult(line);
      input.setOriginalText(line);
      input.setStrippedText(name);
      engine.trigger(input);
    }

    assertEquals(1, store.getCount());
    assertEquals("blue aura", store.get("Dark Knight is grinning here").getAura());
  }

  @Test
  void testPkillsWithAnyCharacters() {
    RegexTrigger tm = new RegexTrigger();

    Mob m = (Mob) tm.process("| 23:43   41522: Apeth the sly Drow Mercenary, ready to kill you <Cohort of t |");
    assertEquals(41522, m.getExp());
    assertEquals("Apeth the sly Drow Mercenary, ready to kill you <Cohort", m.getName().substring(0, 55));

    m = (Mob) tm.process("| 08:15    6174: A crazed chicken looks BERSERK!                              |");
    assertEquals("A crazed chicken looks BERSERK!", m.getName());

    m = (Mob) tm.process("| 08:15    6174: Therp's guard!                                               |");
    assertEquals("Therp's guard!", m.getName());

    // party status lines are not kills
    assertNull(tm.process("| 1.1  Brurin         fol 1593(1593)  169( 169) 442(442) | 100 |        69933 |"));
  }
}
