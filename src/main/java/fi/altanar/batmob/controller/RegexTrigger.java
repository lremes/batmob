package fi.altanar.batmob.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.altanar.batmob.vo.Mob;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegexTrigger {
    
    List<Pattern> patterns;

    //| 22:34    8830: an infant frost giant, rolling in the snow   |
    final String PATTERN_PKILL = "^\\|\\s\\d{1,2}:\\d{2}\\s+(\\d+):\\s([a-zA-Z-\\s,.'\\(\\)]+)\\|$";

    public RegexTrigger() {
        patterns = new ArrayList<Pattern>();
        patterns.add(Pattern.compile(PATTERN_PKILL));
    }

    public Object process(String input) {
        Iterator<Pattern> i = patterns.iterator();
        while (i.hasNext()) {
            Matcher m = i.next().matcher(input);
            if (m.matches() == true) {
                Mob mob = new Mob(Integer.parseInt(m.group(1)), m.group(2).trim());
                return mob;
            }
        }
        return null;
    }
}
