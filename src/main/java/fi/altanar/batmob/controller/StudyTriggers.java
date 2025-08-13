package fi.altanar.batmob.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.altanar.batmob.vo.Mob;

import java.util.ArrayList;
import java.util.List;

public class StudyTriggers {
    List<Pattern> patterns;

    protected Mob mob;

    final Pattern STUDY_START = Pattern.compile("^You start studying ([\\w\\s]+)...$");
    final Pattern STUDY_RACE = Pattern.compile("^([a-zA-Z-]+) is [a|an]+ ([a-zA-Z-]+).$");
    final Pattern STUDY_TYPE = Pattern.compile("^(He|She|It) is [a|an]+ (mythical creature|animal).$");
    final Pattern STUDY_ALIGNMENT = Pattern
            .compile("(He|She|It) is (neutral|good|evil|extremely good|extremely evil).$");
    final Pattern STUDY_RATING = Pattern
            .compile("^(He|She|It) appears to be [of]*\\s*([\\w\\s]+) on the official.*");

    public StudyTriggers() {
    }

    public Object process(String input) {
        Matcher m = STUDY_START.matcher(input);
        if (m.matches()) {
            this.mob = new Mob(0, m.group(1));
            ArrayList<String> list = new ArrayList<String>();
            list.add(m.group(1));
            this.mob.setShortNames(list);

            return null;
        }

        Matcher r = STUDY_TYPE.matcher(input);
        if (r.matches()) {
            return null;
        }

        r = STUDY_RACE.matcher(input);
        if (r.matches()) {
            this.mob.setRace(r.group(2));
            return null;
        }

        Matcher a = STUDY_ALIGNMENT.matcher(input);
        if (a.matches()) {
            String gender = a.group(1);
            switch (gender) {
                case "He":
                    this.mob.setGender("male");
                    break;
                case "She":
                    this.mob.setGender("female");
                    break;
                default:
                    this.mob.setGender("neutral");
                    break;
            }
            this.mob.setAlignment(a.group(2));
            return null;
        }

        Matcher rat = STUDY_RATING.matcher(input);
        if (rat.matches()) {
            this.mob.setFolkloristRating(rat.group(2));
            return this.mob;
        }

        return null;
    }

}
