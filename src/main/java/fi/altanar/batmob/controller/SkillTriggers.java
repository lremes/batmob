package fi.altanar.batmob.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Recognizes skills used by monsters from their combat messages, e.g.
 *
 * Smoking miner performs a fast kick at Arasaka's stomach, leaving foe gasping for air.
 *
 * Messages are collected from session logs. Patterns match from the start of the line
 * since long messages may continue on the next line.
 */
public class SkillTriggers {

    private static final String PRON = "(?:his|her|its)";

    // pattern -> skill name, the first group is the user of the skill
    private Map<Pattern, String> skills = new LinkedHashMap<Pattern, String>();

    public SkillTriggers() {
        this.define("kick", "^(.+?) performs a (?:fast|quick) kick (?:at|to) ");
        this.define("kick", "^(.+?) swings a foot in a long arc (?:landing the kick at|that ends at) ");
        this.define("kick", "^(.+?) aims a fast kick at .+, but miscalculates");
        this.define("kick", "^(.+?) kicks .+ in the groin very hard");

        this.define("bash", "^(.+?) bashes " + PRON + " foe in berserk rage");
        this.define("bash", "^(.+?) tries to bash .+?,? but promptly falls flat on ");

        this.define("push", "^(.+?) pushes .+ (?:extremely hard|very hard|with a bone crushing sound)\\.");
        this.define("push", "^(.+?) pushes you\\.$");

        this.define("grapple", "^(.+?) starts grappling .+\\.$");

        this.define("slash", "^(.+?) quickly steps forward and (?:slashes|pounds|stabs) ");

        this.define("riposte", "^(.+?) slaps .+ weapon aside, and (?:pounds|stabs|strikes) at the opening");

        this.define("crushing blow", "^(.+?) delivers a crushing blow to ");

        this.define("pummel", "^(.+?) pummels .+ in the jaw!");

        // special attacks whose skill isn't known, named after what they do
        this.define("special: kneecap smash", "^(.+?) SMASHES .+ kneecap!");
        this.define("special: jaw hit", "^(.+?) hits (?:your|.+'s?) jaw so hard");
        this.define("special: through defenses", "^(.+?) hits through (?:your|.+'s?) defenses");
        this.define("special: ear rip", "^(.+?) grasps .+ ear in one hand and with a brutal movement rips it");
        this.define("special: hair grab", "^(?:Then, )?(.+?) grasps .+ by the hair and (?:forces the head down|pummels)");
        this.define("special: body punches", "^(?:Then, )?(.+?) repeatedly beats .+ back and stomach with fast punches");
    }

    protected void define(String skill, String regexp) {
        this.skills.put(Pattern.compile(regexp), skill);
    }

    /**
     * @return user and skill name, or null
     */
    public String[] process(String input) {
        for (Map.Entry<Pattern, String> e : this.skills.entrySet()) {
            Matcher m = e.getKey().matcher(input);
            if (m.find()) {
                return new String[] { m.group(1), e.getValue() };
            }
        }
        return null;
    }
}
