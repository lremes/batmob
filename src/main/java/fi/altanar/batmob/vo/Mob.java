package fi.altanar.batmob.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mob implements Serializable, Cloneable {

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
    private String damageType;

    private String folkloristRating;

    // e.g. "cyan aura", warns about especially dangerous monsters
    private String aura;

    // Descriptors the game appends to monster names in rooms but not in the kill list.
    // Health descriptors appear with or without a percentage, e.g. "(slightly hurt, 80%)".
    public static final Pattern NAME_TAG = Pattern.compile("\\s*("
            + "\\((?:undead|bleeding|cursed|insane|high as a badger"
            + "|(?:in excellent shape|in a good shape|slightly hurt|noticeably hurt|not in a good shape"
            + "|in bad shape|in very bad shape|near death)(?:, \\d+%)?)\\)"
            + "|<[-=]*wrapped[-=]*>|<encircled>|<([a-z]+ aura)>"
            + "|\\[(?:host|following)\\])\\s*$");

    public Mob(int exp, String name) {
        this.setName(name);
        this.updateFromTags(name);
        this.updateExp(exp);
    }

    /**
     * Set the flags the descriptors at the end of a name tell, e.g. "(undead)" or "<cyan aura>".
     */
    public void updateFromTags(String name) {
        for (String tag : nameTags(name)) {
            if (tag.equals("(undead)")) {
                this.undead = true;
            } else if (tag.endsWith(" aura>")) {
                this.aura = tag.substring(1, tag.length() - 1);
            }
        }
    }

    /**
     * @return the descriptors at the end of the name, in the order they appear
     */
    public static ArrayList<String> nameTags(String name) {
        ArrayList<String> tags = new ArrayList<String>();
        Matcher m = NAME_TAG.matcher(name);
        while (m.find()) {
            tags.add(0, m.group(1));
            name = name.substring(0, m.start());
            m = NAME_TAG.matcher(name);
        }
        return tags;
    }

    public static String stripNameTags(String name) {
        Matcher m = NAME_TAG.matcher(name);
        while (m.find()) {
            name = name.substring(0, m.start());
            m = NAME_TAG.matcher(name);
        }
        return name;
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

    public void setName(String name) {
        this.name = normalizeName(name);
    }

    public String getDamageType() {
        return damageType;
    }

    public void setDamageType(String damType) {
        this.damageType = damType;
    }

    public String getNotes() {
        return notes;
    }

    public String getFolkloristRating() {
        return folkloristRating;
    }

    public void setFolkloristRating(String folkloristRating) {
        this.folkloristRating = folkloristRating;
    }

    public String getAura() {
        return aura;
    }

    public void setAura(String aura) {
        this.aura = aura;
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

    public void addShortName(String shortName) {
        if (this.shortNames == null) {
            this.shortNames = new String[] { shortName };
        } else {
            ArrayList<String> list = new ArrayList<>(Arrays.asList(this.shortNames));
            if (!list.contains(shortName)) {
                list.add(shortName);
                this.shortNames = list.toArray(new String[0]);
            }
        }
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

    /**
     * @return true if the spell was not known yet
     */
    public boolean addSpell(String spell) {
        ArrayList<String> list = this.getSpells();
        for (String s : list) {
            if (s.equalsIgnoreCase(spell)) {
                return false;
            }
        }
        list.add(spell);
        this.setSpells(list);
        return true;
    }

    /**
     * @return true if the skill was not known yet
     */
    public boolean addSkill(String skill) {
        ArrayList<String> list = this.getSkills();
        for (String s : list) {
            if (s.equalsIgnoreCase(skill)) {
                return false;
            }
        }
        list.add(skill);
        this.setSkills(list);
        return true;
    }

    public boolean hasShortName(String shortName) {
        if (this.shortNames != null) {
            for (String s : this.shortNames) {
                if (s.equalsIgnoreCase(shortName)) {
                    return true;
                }
            }
        }
        return false;
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

    public void updateFromWiki(Mob other) {
        if (this.gender == null || this.gender.isEmpty()) {
            this.gender = other.gender;
        }
        if (this.race == null || this.race.isEmpty()) {
            this.race = other.race;
        }
        if (this.alignment == null || this.alignment.isEmpty()) {
            this.alignment = other.alignment;
        }
        if (this.notes == null || this.notes.isEmpty()) {
            this.notes = other.notes;
        }
        if (this.spells == null || this.spells.length == 0) {
            this.spells = other.spells;
        }
        if (this.skills == null || this.skills.length == 0) {
            this.skills = other.skills;
        }
        if (this.shortNames == null || this.shortNames.length == 0) {
            this.shortNames = other.shortNames;
        }
    }

    public void updateFromStudy(Mob other) {
        if (this.gender == null || this.gender.isEmpty()) {
            this.gender = other.gender;
        }
        if (this.race == null || this.race.isEmpty()) {
            this.race = other.race;
        }
        if (this.alignment == null || this.alignment.isEmpty()) {
            this.alignment = other.alignment;
        }
        if (this.shortNames == null || this.shortNames.length == 0) {
            this.shortNames = other.shortNames;
        }
        if (this.folkloristRating == null || this.folkloristRating.isEmpty()) {
            this.folkloristRating = other.folkloristRating;
        }
        if (this.damageType == null || this.damageType.isEmpty()) {
            this.damageType = other.damageType;
        }
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
        // pkill drops descriptors like (undead) or <cyan aura>
        name = stripNameTags(name);

        if (!name.isEmpty() && name.charAt(0) == '(') {
            name = name.substring(1);
        }

        // remove trailing bracers
        if (!name.isEmpty() && name.charAt(name.length() - 1) == ')') {
            name = name.substring(0, name.length() - 1);
        }

        // name = name.replace("(bleeding)", "");
        // name = name.replace("<wrapped>", "");

        // pkills limits name to 57 chars
        if (name.length() > 57) {
            name = name.substring(0, 56);
        }
        return name.trim();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
