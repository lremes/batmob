package fi.altanar.batmob.vo;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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
import fi.altanar.batmob.controller.SpellTriggers;
import fi.altanar.batmob.controller.StudyTriggers;
import fi.altanar.batmob.io.IMobListener;

import static org.mockito.Mockito.*;

public class StudyTriggerTest {

    private ArrayList<Mob> mobs = new ArrayList<Mob>();

    @Test
    void testStudy() {
        StudyTriggers tm = new StudyTriggers();
        try {
            URL testFileURL = ClassLoader.getSystemResource("studymessages.txt");
            File myFile = new File(testFileURL.toURI());
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Mob m = (Mob) tm.process(data);
                if (m != null) {
                    this.mobs.add(m);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        assertEquals(9, this.mobs.size());

        Mob m = this.mobs.get(0);
        assertEquals("Shezot", m.getShortNames().toArray()[0]);
        assertEquals("dwarf", m.getRace());
        assertEquals("good", m.getAlignment());
        assertEquals("female", m.getGender());
        assertEquals("average dangerousness", m.getFolkloristRating());

        m = this.mobs.get(1);
        assertEquals("Kiec", m.getShortNames().toArray()[0]);
        assertEquals("half-orc", m.getRace());
        assertEquals("evil", m.getAlignment());
        assertEquals("male", m.getGender());
        assertEquals("average dangerousness", m.getFolkloristRating());

        m = this.mobs.get(2);
        assertEquals("Petrus", m.getShortNames().toArray()[0]);
        assertEquals("human", m.getRace());
        assertEquals("very weak", m.getFolkloristRating());

        m = this.mobs.get(3);
        assertEquals("Llakrawour", m.getShortNames().toArray()[0]);
        assertEquals("elf", m.getRace());
        assertEquals("average dangerousness", m.getFolkloristRating());

        m = this.mobs.get(4);
        assertEquals("Barsoomian", m.getShortNames().toArray()[0]);
        assertEquals("barsoomian", m.getRace());
        assertEquals("weak", m.getFolkloristRating());
        assertEquals("extremely evil", m.getAlignment());

        m = this.mobs.get(5);
        assertEquals("Ugly magpie", m.getShortNames().toArray()[0]);
        assertEquals("bird", m.getRace());
        assertEquals("extremely weak", m.getFolkloristRating());

        m = this.mobs.get(6);
        assertEquals("Sing Wu", m.getShortNames().toArray()[0]);
        assertEquals("human", m.getRace());

        m = this.mobs.get(7);
        assertEquals("Laudia Zemovrez", m.getShortNames().toArray()[0]);
        assertEquals("elf", m.getRace());
        assertEquals("physical", m.getDamageType());

        m = this.mobs.get(8);
        assertEquals("Elemental", m.getShortNames().toArray()[0]);
        assertEquals("elemental", m.getRace());
        assertEquals("physical, poison", m.getDamageType());
        assertEquals("a bit good", m.getAlignment());

    }

    @Test
    void testLinesOutsideStudyAreIgnored() {
        StudyTriggers tm = new StudyTriggers();

        // these match the race/alignment/damage patterns but no study is running
        assertNull(tm.process("The door is closed."));
        assertNull(tm.process("He is a bit evil."));
        assertNull(tm.process("She inflicts fire damage."));
        assertNull(tm.process("... You are done studying!"));

        // a study after that still works
        assertNull(tm.process("You start studying Kiec..."));
        assertNull(tm.process("... You are done studying!"));
        assertNull(tm.process("Kiec is a half-orc."));
        Mob m = (Mob) tm.process(
                "He appears to be of average dangerousness on the official folklorist danger level rating system.");
        assertNotNull(m);
        assertEquals("half-orc", m.getRace());

        // lines after the rating no longer belong to a study
        assertNull(tm.process("He inflicts physical damage."));
        assertNull(tm.process("Bob is a dwarf."));
    }
}
