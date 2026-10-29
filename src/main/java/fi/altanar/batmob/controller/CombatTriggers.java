package fi.altanar.batmob.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Picks the short name of the monster being fought from combat output:
 *
 * Skinned fox is in very bad shape (20%).
 * Skinned fox is DEAD, R.I.P.
 *
 * and spells named in casting messages:
 *
 * Rapunzel claps her hands and says (acid blast)
 */
public class CombatTriggers {

    // shown once per round for the current target, e.g. "... (45%) and STUNNED."
    final Pattern HEALTH = Pattern.compile(
            "^(.+?) is (?:in excellent shape|in a good shape|slightly hurt|noticeably hurt|not in a good shape"
                    + "|in bad shape|in very bad shape|near death) \\((\\d+)%\\)(?: and [^.]+)?\\.$");

    // casting messages seen in the session logs, followed by the chant or the spell name in parentheses
    public static final String CASTING_PHRASES = "(?:"
            + "claps (?:his|her|its) hands and (?:whispers|says)"
            + "|utters the magic words"
            + "|rolls (?:his|her|its) eyes wildly and exclaims"
            + "|flaps (?:(?:his|her|its) )?arms and utters the magic words"
            + "|sneers and cackles the word"
            + "|fills up (?:his|her|its) cheeks with air and exhales"
            + "|stands firmly and chants"
            + "|booms in sinister voice"
            + "|waves (?:his|her|its) index finger while uttering"
            + "|points an accusing finger and cries"
            + "|clenches (?:his|her|its) fists and yells"
            + "|throws a pinch of magic dust in air and chants"
            + "|taps (?:his|her|its) foot three times and utters the words"
            + "|gets an evil gleam in (?:his|her|its) eyes and chants"
            + "|is surrounded by blue waves as you hear the words"
            + "|traces fiery red runes on the ground"
            + "|traces fiery blue runes in the air'?"
            + "|traces fiery demonic night runes in the air"
            + "|mutters the holy words"
            + "|breaks up a mirror and chants"
            + "|intones in a strong voice"
            + "|shreds air with (?:his|her|its) hands and screams"
            + "|makes shooing gestures with (?:his|her|its) hands and whispers"
            + "|bows (?:his|her|its) head in concentration and chants"
            + "|frantically waves (?:his|her|its) hands and yells"
            + "|is encased in a blue aura as (?:he|she|it) chants"
            + "|points upwards and screams"
            + "|screams in rage and yells"
            + "|raises a clenched fist and yells"
            + "|braces (?:his|her|its) body and yells"
            + ")";

    final Pattern CAST = Pattern.compile("^(.+?) " + CASTING_PHRASES + " \\(([a-z][a-z' -]*)\\)$");

    // area spells name themselves when they hit several targets:
    // "Snow devil hits you, Croesus and Online with its hailstorm."
    final Pattern AREA_HIT = Pattern
            .compile("^(.+?) hits (?:[^,]+, )*[^,]+ and [^,]+ with (?:his|her|its) ([a-z][a-z' -]*)\\.$");

    // the spell is named when it hits: "Hell hound grins as its flame arrow hits Arasaka."
    final Pattern SPELL_HIT = Pattern.compile("^(.+?) grins as (?:his|her|its) ([a-z][a-z' -]*?) hits .+\\.$");

    // effect of the psi spell paralyze, also when it fails
    final Pattern PARALYZE = Pattern
            .compile("^(.+?) (?:paralyzes .+ with (?:his|her|its) mind|fails to paralyze .+)\\.$");

    final Pattern DEAD = Pattern.compile("^(.+?) is DEAD, R\\.I\\.P\\.$");

    /**
     * @return the short name of the monster in combat, or null
     */
    public String process(String input) {
        Matcher m = HEALTH.matcher(input);
        if (m.matches()) {
            return m.group(1);
        }
        m = DEAD.matcher(input);
        if (m.matches()) {
            return m.group(1);
        }
        return null;
    }

    /**
     * @return caster and spell name, or null
     */
    public String[] spellCast(String input) {
        Matcher m = CAST.matcher(input);
        if (m.matches()) {
            return new String[] { m.group(1), m.group(2) };
        }
        m = AREA_HIT.matcher(input);
        if (m.matches()) {
            return new String[] { m.group(1), m.group(2) };
        }
        m = SPELL_HIT.matcher(input);
        if (m.matches()) {
            return new String[] { m.group(1), m.group(2) };
        }
        m = PARALYZE.matcher(input);
        if (m.matches()) {
            return new String[] { m.group(1), "paralyze" };
        }
        return null;
    }
}
