package fi.altanar.batmob.vo;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import fi.altanar.batmob.io.IMobListener;

import static org.mockito.Mockito.*;

public class SpellTriggerTest {
    @Test
    void testSpells() {
        SpellTriggers tm = new SpellTriggers();

        List<String> testStrings = Arrays.asList(
                "Baroness",
                "Snake",
                "Oize tyl Hesp",
                "Maltrak",
                "Bobby",
                "Trumpet archon",
                "Mayor",
                "Derfel");

        try {
            URL testFileURL = ClassLoader.getSystemResource("combatmessages.txt");
            File myFile = new File(testFileURL.toURI());
            Scanner myReader = new Scanner(myFile);
            int i = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Spell spell = (Spell) tm.process(data);
                assertNotNull(spell);

                assertEquals(testStrings.get(i), spell.getCaster());
                i++;
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
    void testCasterFromCastingPhrase() {
        SpellTriggers tm = new SpellTriggers();

        // the caster used to be the whole line for these
        String[][] casts = {
                { "Kjeldoran braces her body and yells 'hhhnnnnnrrrrraaahhh!!'", "Kjeldoran", "energy aura yellow" },
                { "Kjeldoran braces her body and yells 'hnnn....Urrgggg.....RRAAHH!!!'", "Kjeldoran", "energy aura red" },
                { "Kjeldoran braces her body and yells 'RRRRAAAAAHHRRRRGGGGGGHHH!!!!!'", "Kjeldoran", "energy aura blue" },
                { "Elemental of fire booms in sinister voice 'zing yulygul bugh'", "Elemental of fire", "fireball" },
                { "Harlon booms in sinister voice 'zing yulygul bugh'", "Harlon", "fireball" },
                { "Shifter throws a pinch of magic dust in air and chants 'gtzt mar nak semen'", "Shifter", "summon greater spores" },
        };
        for (String[] c : casts) {
            Spell spell = (Spell) tm.process(c[0]);
            assertNotNull(spell, c[0]);
            assertEquals(c[2], spell.name, c[0]);
            assertEquals(c[1], spell.getCaster(), c[0]);
        }
    }
}
