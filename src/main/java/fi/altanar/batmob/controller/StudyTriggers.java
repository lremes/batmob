package fi.altanar.batmob.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.altanar.batmob.vo.Mob;

import java.util.ArrayList;
import java.util.List;

public class StudyTriggers {
    List<Pattern> patterns;

    protected Mob mob = null;

    protected int lineCount = -1;

    final Pattern STUDY_START = Pattern.compile("^You start studying ([\\w\\s',._-]+)...$");
    final Pattern STUDY_DONE = Pattern.compile("^... You are done studying!$");
    final Pattern STUDY_RACE = Pattern.compile("^([a-zA-Z- ]+) is [a|an]+ ([a-zA-Z-]+).$");
    final Pattern STUDY_TYPE = Pattern.compile("^(He|She|It) is [a|an]+ (mythical creature|animal).$");
    final Pattern STUDY_RESISTS0 = Pattern.compile("^(He|She|It) is defenseless against ([A-Za-z]+) damage.$");
    final Pattern STUDY_RESISTS1 = Pattern
            .compile("^(He|She|It) seems to be almost defenseless against ([A-Za-z]+) damage.$");
    final Pattern STUDY_RESISTS2 = Pattern.compile("^(He|She|It) has some resistance against ([A-Za-z]+) damage.$");
    final Pattern STUDY_RESISTS3 = Pattern
            .compile("^(He|She|It) seems to be moderately resistant against ([A-Za-z]+) damage.$");
    final Pattern STUDY_RESISTS4 = Pattern.compile("^(He|She|It) has good resistance against ([A-Za-z]+) damage.$");
    final Pattern STUDY_RESISTS5 = Pattern.compile("^(He|She|It) seems almost immune against ([A-Za-z]+) damage.$");

    final Pattern STUDY_ALIGNMENT = Pattern
            .compile("(He|She|It) is (neutral|good|evil|extremely good|extremely evil).$");
    final Pattern STUDY_RATING = Pattern
            .compile("^(He|She|It) appears to be [of]*\\s*([\\w\\s]+) on the official.*");

    final Pattern STUDY_STUDIED = Pattern.compile("^You have already studied this creature.$");

    final Pattern STUDY_NEW = Pattern.compile("^You haven't studied this creature before, adding to your guide.$");

    final Pattern STUDY_DAMTYPE = Pattern.compile("^(He|She|It) inflicts ([A-Za-z]+) damage.$");

    public StudyTriggers() {
    }

    public Object process(String input) {
        Matcher m = STUDY_START.matcher(input);
        if (m.matches()) {
            this.mob = new Mob(0, m.group(1));
            ArrayList<String> list = new ArrayList<String>();
            list.add(m.group(1));
            this.mob.setShortNames(list);
            this.lineCount = -1;

            return null;
        }

        Matcher d = STUDY_DONE.matcher(input);
        if (d.matches()) {
            this.lineCount = 0;
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
            try {
                Mob clone = (Mob) this.mob.clone();
                this.mob = null;
                return clone;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        Matcher s = STUDY_STUDIED.matcher(input);
        if (s.matches()) {
            return null;
        }

        Matcher n = STUDY_NEW.matcher(input);
        if (n.matches()) {
            return null;
        }

        Matcher r0 = STUDY_RESISTS0.matcher(input);
        if (r0.matches()) {
            return null;
        }

        Matcher r1 = STUDY_RESISTS1.matcher(input);
        if (r1.matches()) {
            return null;
        }

        Matcher r2 = STUDY_RESISTS2.matcher(input);
        if (r2.matches()) {
            return null;
        }

        Matcher r3 = STUDY_RESISTS3.matcher(input);
        if (r3.matches()) {
            return null;
        }

        Matcher r4 = STUDY_RESISTS4.matcher(input);
        if (r4.matches()) {
            return null;
        }

        Matcher r5 = STUDY_RESISTS5.matcher(input);
        if (r5.matches()) {
            return null;
        }

        Matcher dam = STUDY_DAMTYPE.matcher(input);
        if (dam.matches()) {
            this.mob.setDamageType(dam.group(2));
            return null;
        }

        // count non-matching lines and trigger return after non-matching 3 lines
        if (this.mob != null && this.lineCount >= 0) {
            this.lineCount++;

            if (this.lineCount > 3) {
                Mob clone = this.mob;
                this.mob = null;
                return clone;
            }
        }

        return null;
    }

}
