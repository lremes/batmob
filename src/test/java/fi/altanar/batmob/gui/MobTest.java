package fi.altanar.batmob.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import com.mythicscape.batclient.interfaces.ParsedResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.controller.MobPlugin;
import fi.altanar.batmob.controller.RegexTrigger;
import fi.altanar.batmob.io.IMobListener;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobStore;

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
              Mob mob = (Mob)tm.process(data);
              assertNotNull(mob);
              assertNotEquals(expValues[i++], mob.getExp());
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
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
              System.out.println(mob);
            }
            myReader.close();

            testFileURL = ClassLoader.getSystemResource("pkills_update.txt");
            myFile = new File(testFileURL.toURI());
            myReader = new Scanner(myFile);
            i = 0;
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              Mob mob = (Mob)tm.process(data);

              mob = store.updateAutofilledFields(mob);
              System.out.println(mob);
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
      mob.setRixx(true);
      String[] sArray = new String[]{ "s1", "s2" };

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
      assertTrue(expected.isRixx());
    }

    @Test
    void testIgnore() {
      try {
        URL testFileURL = ClassLoader.getSystemResource("ignored.txt");

        File myFile = new File(testFileURL.toURI());
        Scanner myReader = new Scanner(myFile);
        while (myReader.hasNextLine()) {
          String line = myReader.nextLine();
          boolean matches = false;
          for (String s: MobEngine.IGNORED) {
            matches = line.startsWith(s);
            if (matches) {
              break;
            }
          }
          assertTrue(matches);
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
    @Disabled
    void testIgnoreMap() {
      try {
        URL testFileURL = ClassLoader.getSystemResource("ignored_maps.txt");

        File myFile = new File(testFileURL.toURI());
        Scanner myReader = new Scanner(myFile);
        while (myReader.hasNextLine()) {
          String line = myReader.nextLine().trim();
          System.out.println(line);
          Matcher m = MobEngine.IGNORE_MAPS.matcher(line);
          assertTrue(m.matches() == true);
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
}

