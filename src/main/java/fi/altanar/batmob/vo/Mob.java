package fi.altanar.batmob.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mob implements Serializable {

    private static final long serialVersionUID = 0L;

    private String name;
    private int exp = 0;
    private int minExp = 0;
    private int maxExp = 0;
    private String area;
    private String notes;

    private String[] shortNames;
    private String[] spells;
    private String[] skills;
    private String race;
    private String alignment;
    private String description;
    private String rep;
    private boolean rixx;
    private boolean aggro;
    private boolean undead;
    private String gender;

    public static final Pattern IGNORE_PARTS = Pattern.compile("\\((undead|bleeding)\\)|<[-=]*wrapped[-=]*>|<encircled>");

    public Mob(int exp, String name) {
        this.undead = name.contains("(undead)");
        this.setName(name);
        this.updateExp(exp);
    }

    public int getExp() {
        return exp;
    }

    public void updateExp(int exp) {
        if (exp > 0) {
            this.exp = exp;
            if (this.minExp == 0 || this.minExp > exp) {
                this.minExp = exp;
            }

            if (this.maxExp == 0 || this.maxExp < exp) {
                this.maxExp = exp;
            }
        }
    }

    public String getAllExpAsString() {
        return " " + this.exp + " / " +
            this.minExp + " / " +
            this.maxExp;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = normalizeName(name);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<String> getShortNames() {
        if (shortNames != null) {
            return new ArrayList<String>(Arrays.asList(shortNames));
        }
        return null;
    }

    public void setShortNames(ArrayList<String> shortNames) {
        this.shortNames = shortNames.toArray(new String[0]);
    }

    public ArrayList<String> getSpells() {
        if (spells != null) {
            return new ArrayList<String>(Arrays.asList(spells));
        }
        return new ArrayList<String>();
    }

    public void setSpells(ArrayList<String> spells) {
        this.spells = spells.toArray(new String[0]);
    }

    public ArrayList<String> getSkills() {
        if (skills != null) {
            return new ArrayList<String>(Arrays.asList(skills));
        }
        return new ArrayList<String>();
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills.toArray(new String[0]);
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && !description.isEmpty()) {
            this.description = description;
        }
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        if (area != null && !area.isEmpty()) {
            this.area = area;
        }
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public boolean isZinium() {
        return rixx;
    }

    public void setZinium(boolean rixx) {
        this.rixx = rixx;
    }

    public int getMinExp() {
        return this.minExp;
    }

    public int getMaxExp() {
        return this.maxExp;
    }

    public boolean isAggro() {
        return aggro;
    }

    public void setAggro(boolean aggro) {
        this.aggro = aggro;
    }

    public boolean isUndead() {
        return undead;
    }

    public void setUndead(boolean undead) {
        this.undead = undead;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String toString() {
        return this.name +
            "|" + this.exp +
            "|" + this.minExp +
            "|" + this.maxExp +
            "|" + this.area;
    }

    public void updateFrom(Mob other) {
        this.gender = other.gender;
        this.race = other.race;
        this.alignment = other.alignment;
        this.notes = other.notes;
        this.spells = other.spells;
        this.skills = other.skills;
        this.shortNames = other.shortNames;
        this.undead = other.undead;
    }

    public String dump() {
        return this.name +
            "|" + this.exp +
            "|" + this.minExp +
            "|" + this.maxExp +
            "|" + this.area +
            "|" + this.race +
            "|" + this.alignment +
            "|" + this.isAggro() +
            "|" + this.isUndead() +
            "|" + this.rep +
            "|" + this.shortNames +
            "|" + this.skills +
            "|" + this.spells;
    }

    public static String normalizeName(String name) {
        // pkill removes (undead)
        Matcher mtch = IGNORE_PARTS.matcher(name);
        name = mtch.replaceAll("");

        //name = name.replace("(bleeding)", "");
        //name = name.replace("<wrapped>", "");

        // pkills limits name to 57 chars
        if (name.length() > 57) {
            name = name.substring(0,56);
        }
        return name.trim();
    }

}
