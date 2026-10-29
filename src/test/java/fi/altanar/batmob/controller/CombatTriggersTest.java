package fi.altanar.batmob.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CombatTriggersTest {

    @Test
    void testShortNames() {
        CombatTriggers ct = new CombatTriggers();

        // from a real fight
        assertEquals("Skinned fox", ct.process("Skinned fox is noticeably hurt (50%)."));
        assertEquals("Skinned fox", ct.process("Skinned fox is in very bad shape (20%)."));
        assertEquals("Skinned fox", ct.process("Skinned fox is DEAD, R.I.P."));
        assertEquals("Manannan, the evil mage", ct.process("Manannan, the evil mage is slightly hurt (75%)."));
        assertEquals("Air Elemental", ct.process("Air Elemental is near death (5%)."));
        assertEquals("Fire Elemental", ct.process("Fire Elemental is in a good shape (85%)."));
        assertEquals("Kender of kenders", ct.process("Kender of kenders is in excellent shape (100%)."));
        assertEquals("Apprentice sorcerer", ct.process("Apprentice sorcerer is not in a good shape (40%)."));
        assertEquals("Elemental of water", ct.process("Elemental of water is in bad shape (25%)."));

        // stunned targets
        assertEquals("Huge Maggot", ct.process("Huge Maggot is not in a good shape (45%) and STUNNED."));
        assertEquals("Guard", ct.process("Guard is in a good shape (85%) and *STUNNED*."));
        assertEquals("Troll", ct.process("Troll is slightly hurt (70%) and Stunned."));

        assertNull(ct.process("Skinned fox convulses uncontrollably!"));
        assertNull(ct.process("Elemental of water is stunned."));
        assertNull(ct.process("********************** Round 3 **********************"));
    }

    @Test
    void testSpellCasts() {
        CombatTriggers ct = new CombatTriggers();

        // from the session logs
        String[][] casts = {
                { "Setutu claps her hands and whispers (cure critical wounds)", "Setutu", "cure critical wounds" },
                { "Rapunzel claps her hands and says (acid blast)", "Rapunzel", "acid blast" },
                { "Setutu utters the magic words (unpain)", "Setutu", "unpain" },
                { "Fimir rolls his eyes wildly and exclaims (golden arrow)", "Fimir", "golden arrow" },
                { "Gether flaps arms and utters the magic words (acid ray)", "Gether", "acid ray" },
                { "Astrax flaps her arms and utters the magic words (blast vacuum)", "Astrax", "blast vacuum" },
                { "Belse sneers and cackles the word (word of spite)", "Belse", "word of spite" },
                { "Butterball the Troll fills up her cheeks with air and exhales (acid arrow)", "Butterball the Troll", "acid arrow" },
                { "Drakh stands firmly and chants (spider demon control)", "Drakh", "spider demon control" },
                { "Oize tyl Hesp booms in sinister voice (acid wind)", "Oize tyl Hesp", "acid wind" },
                { "Guard waves his index finger while uttering (firebolt)", "Guard", "firebolt" },
                { "Kjeldoran points an accusing finger and cries (drain enemy)", "Kjeldoran", "drain enemy" },
                { "Gurd clenches her fists and yells (psychic crush)", "Gurd", "psychic crush" },
                { "Spider demon clenches its fists and yells (psychic crush)", "Spider demon", "psychic crush" },
                { "Black Knight throws a pinch of magic dust in air and chants (mind blast)", "Black Knight", "mind blast" },
                { "Henri taps his foot three times and utters the words (mind blast)", "Henri", "mind blast" },
                { "Lady gets an evil gleam in her eyes and chants (mana drain)", "Lady", "mana drain" },
                { "Balron is surrounded by blue waves as you hear the words (force shield)", "Balron", "force shield" },
                { "Being traces fiery red runes on the ground (earthquake)", "Being", "earthquake" },
                { "Wick traces fiery blue runes in the air' (flex shield)", "Wick", "flex shield" },
                { "Demon archivist traces fiery demonic night runes in the air (wither flesh)", "Demon archivist", "wither flesh" },
                { "Cadalerid mutters the holy words (dispel evil)", "Cadalerid", "dispel evil" },
                { "Suamiel Nelut breaks up a mirror and chants (mirror image)", "Suamiel Nelut", "mirror image" },
                { "Kilgader intones in a strong voice (word of blasting)", "Kilgader", "word of blasting" },
                { "Shadowfang shreds air with his hands and screams (tiger claw)", "Shadowfang", "tiger claw" },
                { "Demon of draen-dalar makes shooing gestures with its hands and whispers (suppress magic)", "Demon of draen-dalar", "suppress magic" },
                { "Balron bows his head in concentration and chants (floating)", "Balron", "floating" },
                { "Wight guard frantically waves his hands and yells (summon)", "Wight guard", "summon" },
                { "Balron is encased in a blue aura as he chants (relocate)", "Balron", "relocate" },
                { "Demon miner points upwards and screams (black hole)", "Demon miner", "black hole" },
                { "Gray Slaad screams in rage and yells (aura of hate)", "Gray Slaad", "aura of hate" },
                { "Apep raises a clenched fist and yells (word of genocide)", "Apep", "word of genocide" },
                // area spells hitting several targets
                { "Snow devil hits you, Croesus and Online with its hailstorm.", "Snow devil", "hailstorm" },
                { "Bantha hits Rhoads, you and Blnk with her fireball.", "Bantha", "fireball" },
                { "Female hill giant hits you, Pazi, Yggdrasil and Vedrfolnir with her mana drain.", "Female hill giant", "mana drain" },
                { "Ulmat-Sumtra hits you, Yggdrasil and Vedrfolnir with its cone of cold.", "Ulmat-Sumtra", "cone of cold" },
                // named when the spell hits
                { "Hell hound grins as its flame arrow hits Arasaka.", "Hell hound", "flame arrow" },
                { "Knight of Ni grins as his acid arrow hits Yggdrasil.", "Knight of Ni", "acid arrow" },
                // psi paralyze, also when it fails
                { "Guard paralyzes Deepwood with his mind.", "Guard", "paralyze" },
                { "Frederick paralyzes you with his mind.", "Frederick", "paralyze" },
                { "Guard fails to paralyze Deepwood.", "Guard", "paralyze" },
        };
        for (String[] c : casts) {
            assertArrayEquals(new String[] { c[1], c[2] }, ct.spellCast(c[0]), c[0]);
        }

        // own casts and items with descriptors
        assertNull(ct.spellCast("You clap your hands and whisper (cure serious wounds)"));
        assertNull(ct.spellCast("a large fountain (full)"));
        // single target hits and names with descriptors are not spells
        assertNull(ct.spellCast("Dragon hits you with its tail."));
        assertNull(ct.spellCast("Wellamo is petting her dog (undead)"));
        assertNull(ct.spellCast("Hera devastates Balron's mind with her powers!"));
        assertNull(ct.spellCast("TimberMoss casts a harming spell on Yggdrasil."));
        assertNull(ct.spellCast("Mayor flaps arms and utters the magic words 'ghht mar nak semen'"));
    }
}
