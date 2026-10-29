package fi.altanar.batmob.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SkillTriggersTest {

    @Test
    void testSkills() {
        SkillTriggers st = new SkillTriggers();

        // from the session logs, long lines may be cut where the client wrapped them
        String[][] uses = {
                { "Smoking miner performs a fast kick at Arasaka's stomach, leaving foe gasping for air.", "Smoking miner", "kick" },
                { "Yves performs a quick kick to your stomach, almost making you lose your", "Yves", "kick" },
                { "Dwarven traveller swings a foot in a long arc landing the kick at Arasaka's kneecap. 'CRACK!'", "Dwarven traveller", "kick" },
                { "Guard swings a foot in a long arc that ends at your kneecap. 'CRACK!'", "Guard", "kick" },
                { "Barmaid aims a fast kick at Silencer's head, but miscalculates, only managing to", "Barmaid", "kick" },
                { "Barmaid kicks Darna in the groin very hard making the foe gasp and double up", "Barmaid", "kick" },
                { "Guard kicks you in the groin very hard. You gasp with pain and double up.", "Guard", "kick" },
                { "Troll bashes his foe in berserk rage!!", "Troll", "bash" },
                { "Grumpy ram tries to bash you, but promptly falls flat on his face.", "Grumpy ram", "bash" },
                { "Exmensun tries to bash Sentence but promptly falls flat on his face.", "Exmensun", "bash" },
                { "Wicked looking guard pushes Trement extremely hard.", "Wicked looking guard", "push" },
                { "Giant spider pushes Deepwood very hard.", "Giant spider", "push" },
                { "Wellforge pushes Baroque with a bone crushing sound.", "Wellforge", "push" },
                { "Guard pushes you.", "Guard", "push" },
                { "R'ami starts grappling Arasaka.", "R'ami", "grapple" },
                { "Lion guard quickly steps forward and slashes Brurin in the temple!", "Lion guard", "slash" },
                { "Paladin quickly steps forward and pounds Brurin in the temple!", "Paladin", "slash" },
                { "Lion guard quickly steps forward and slashes you, before you can move!", "Lion guard", "slash" },
                { "Old miner slaps Arasaka's weapon aside, and pounds at the opening!", "Old miner", "riposte" },
                { "Colin Robertson slaps Arasaka's weapon aside, and stabs at the opening!", "Colin Robertson", "riposte" },
                { "Ranger slaps Park warden's weapon aside, and strikes at the opening!", "Ranger", "riposte" },
                { "Paladin delivers a crushing blow to Brurin's windpipe!", "Paladin", "crushing blow" },
                { "Busy miner pummels Arasaka in the jaw!", "Busy miner", "pummel" },
                // specials, skill unknown
                { "Lion guard SMASHES Moonglaze's kneecap!", "Lion guard", "special: kneecap smash" },
                { "Savage chief hits your jaw so hard, you taste blood!", "Savage chief", "special: jaw hit" },
                { "Savage man hits through your defenses, slashing a terrible wound in your", "Savage man", "special: through defenses" },
                { "Khalun grasps Arasaka's ear in one hand and with a brutal movement rips it right off, that must hurt!", "Khalun", "special: ear rip" },
                { "Then, Khalun grasps Arasaka by the hair and pummels Arasaka's head with maniacal rage.", "Khalun", "special: hair grab" },
                { "Khalun grasps Brurin by the hair and forces the head down to meet Khalun's knee.", "Khalun", "special: hair grab" },
                { "Then, Morak-Orc repeatedly beats Arasaka's back and stomach with fast punches.", "Morak-Orc", "special: body punches" },
        };
        for (String[] u : uses) {
            assertArrayEquals(new String[] { u[1], u[2] }, st.process(u[0]), u[0]);
        }

        // normal hits and effects are not skills
        assertNull(st.process("Servant maid boots Arasaka producing large scratches."));
        assertNull(st.process("Michelle kicks Arasaka producing large scratches."));
        assertNull(st.process("Barbarian tries to pound Armin, but his attack is easily blocked."));
        assertNull(st.process("Guard pushes you back."));
        assertNull(st.process("Huge Maggot trips all over his feet!"));
    }
}
