package fi.altanar.batmob.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PartyTriggersTest {

    private static final String START = ",-----------------------------------------------------------------------------.";
    private static final String END = "`-----------------------------------------------------------------------------'";

    private static boolean feed(PartyTriggers pt, String... lines) {
        boolean changed = false;
        for (String line : lines) {
            changed |= pt.process(line);
        }
        return changed;
    }

    @Test
    void testPartyStatus() {
        PartyTriggers pt = new PartyTriggers();

        assertTrue(feed(pt,
                START,
                "|*1.1   Baroness    ldr   812( 812)  320( 320)  410( 410) |",
                "| 1.2   Bobby       fol   500( 640)  220( 400)  300( 410) |",
                "| ?.?   Derfel      mbr   300( 300)  100( 100)  200( 200) |",
                END));

        assertTrue(pt.isMember("Baroness"));
        assertTrue(pt.isMember("bobby"));
        assertTrue(pt.isMember("Derfel "));
        assertFalse(pt.isMember("Maltrak"));
        assertFalse(pt.isMember(null));
        assertEquals(3, pt.getMembers().size());
    }

    @Test
    void testNewListingReplacesOld() {
        PartyTriggers pt = new PartyTriggers();
        feed(pt, START, "| 1.1   Baroness    ldr   812( 812) |", "| 1.2   Bobby       fol   500( 640) |", END);

        // Bobby left
        assertTrue(feed(pt, START, "| 1.1   Baroness    ldr   812( 812) |", END));
        assertFalse(pt.isMember("Bobby"));
        assertTrue(pt.isMember("Baroness"));

        // same listing again, no change
        assertFalse(feed(pt, START, "| 1.1   Baroness    ldr   812( 812) |", END));
    }

    @Test
    void testOtherBoxesAndLinesAreIgnored() {
        PartyTriggers pt = new PartyTriggers();
        feed(pt, START, "| 1.1   Baroness    ldr   812( 812) |", END);

        // a box without member lines doesn't clear the party
        assertFalse(feed(pt, START, "| Score: 1234                                  |", END));
        assertTrue(pt.isMember("Baroness"));

        // member-like lines outside a listing are ignored
        assertFalse(feed(pt, "| 1.2   Bobby       fol   500( 640) |", END));
        assertFalse(pt.isMember("Bobby"));
    }
}
