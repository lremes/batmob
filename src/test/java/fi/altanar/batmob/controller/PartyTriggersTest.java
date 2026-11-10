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

        // Bobby left, but is still a player
        assertTrue(feed(pt, START, "| 1.1   Baroness    ldr   812( 812) |", END));
        assertFalse(pt.getMembers().contains("Bobby"));
        assertTrue(pt.isMember("Bobby"));
        assertTrue(pt.isMember("Bobby's soul"));
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

    @Test
    void testRealOutput() {
        PartyTriggers pt = new PartyTriggers();

        // single player party, as printed by batmud
        assertTrue(feed(pt,
                ",-----------------------------------------------------------------------------.",
                "| 1.2  Armin          ldr  473( 473)  956( 956) 267(267) |  59 |            0 |",
                "`-----------------------------------------------------------------------------'"));

        assertTrue(pt.isMember("Armin"));
        assertEquals(1, pt.getMembers().size());
    }

    @Test
    void testRealOutputWithMinions() {
        PartyTriggers pt = new PartyTriggers();

        assertTrue(feed(pt,
                ",-----------------------------------------------------------------------------.",
                "| 1.1  Brurin         fol 1593(1593)  169( 169) 442(442) | 100 |        69933 |",
                "| 1.2  Lysander       fol 1236(1337)  697( 697) 350(408) |  96 |       183942 |",
                "| 2.1  Armin          fol  440( 473)  781( 956) 267(267) |  59 |            0 |",
                "| 2.2  Kjeldoran      ldr  339( 360)  653(1639) 164(231) |  83 |       194269 |",
                "`-----------------------------------------------------------------------------'",
                "| Brurin's soul       fol VERY high     full      full   | ??? |              |",
                "| Lysander's soul    form    full       full      full   | ??? |              |",
                "`-----------------------------------------------------------------------------'"));

        assertEquals(4, pt.getMembers().size());
        assertTrue(pt.isMember("Kjeldoran"));
        assertFalse(pt.isMember("Brurin's"));

        // minions and unrecognized casters (whole line) of members
        assertTrue(pt.isMember("Brurin's soul"));
        assertTrue(pt.isMember("Lysander's soul utters the magic words 'xyz'"));
        assertTrue(pt.isMember("Kjeldoran waves his hands"));

        // not members even if the name starts the same
        assertFalse(pt.isMember("Brurinson"));
        assertFalse(pt.isMember("Baroness gets an evil gleam in her eyes"));
    }

    @Test
    void testLeavingTheParty() {
        PartyTriggers pt = new PartyTriggers();
        feed(pt, START, "| 1.1   Lysander    ldr   812( 812) |", "| 1.2   Brog        fol   500( 640) |",
                "| 1.3   Armin       fol   500( 640) |", END);

        assertTrue(feed(pt, "Brog left the party."));
        assertEquals(2, pt.getMembers().size());
        assertTrue(pt.isMember("Brog"));

        // minions leaving aren't members
        assertFalse(feed(pt, "Air the christmas shadow left the party."));

        assertTrue(feed(pt, "Lysander kicks Armin out of party."));
        assertEquals(1, pt.getMembers().size());

        assertTrue(feed(pt, "Lysander disbands the party."));
        assertTrue(pt.getMembers().isEmpty());
        assertTrue(pt.isMember("Lysander"));

        assertFalse(feed(pt, "You left the party."));

        feed(pt, START, "| 1.1   Armin       ldr   812( 812) |", "| 1.2   Derfel      fol   500( 640) |", END);
        assertTrue(feed(pt, "You left the party."));
        assertTrue(pt.getMembers().isEmpty());
    }

    @Test
    void testJoiningTheParty() {
        PartyTriggers pt = new PartyTriggers();

        // the party isn't listed yet, members are learned as they join
        assertTrue(feed(pt, "Tails is now acting member of the party."));
        assertTrue(feed(pt, "Mesardus is now acting member of the party."));
        assertFalse(feed(pt, "Tails is now acting member of the party."));
        assertTrue(pt.isMember("Tails"));
        assertTrue(pt.isMember("Mesardus"));
        assertEquals(2, pt.getMembers().size());

        // joining another party drops the old members until the next listing
        assertTrue(feed(pt, "You are a member of the party now."));
        assertTrue(pt.getMembers().isEmpty());
        assertTrue(pt.isMember("Tails"));

        feed(pt, "Brog is now acting member of the party.");
        assertTrue(feed(pt, "You are kicked out of party."));
        assertTrue(pt.getMembers().isEmpty());
    }
}
