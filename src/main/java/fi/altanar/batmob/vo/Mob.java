package fi.altanar.batmob.vo;

import java.io.Serializable;

public class Mob implements Serializable {

    private static final long serialVersionUID = 0L;

    private String name;
    private int exp;
    private int minExp;
    private int maxExp;
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

    public Mob(int exp, String name) {
        this.name = name;
        this.setExp(exp);
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
        if (this.minExp == 0 || this.minExp > exp) {
            this.minExp = exp;
        }

        if (this.maxExp == 0 || this.maxExp < exp) {
            this.maxExp = exp;
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
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String[] getShortNames() {
        return shortNames;
    }

    public void setShortNames(String[] shortNames) {
        this.shortNames = shortNames;
    }

    public String[] getSpells() {
        return spells;
    }

    public void setSpells(String[] spells) {
        this.spells = spells;
    }

    public String[] getSkills() {
        return skills;
    }

    public void setSkills(String[] skills) {
        this.skills = skills;
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
        this.description = description;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public boolean isRixx() {
        return rixx;
    }

    public void setRixx(boolean rixx) {
        this.rixx = rixx;
    }

    public int getMinExp() {
        return this.minExp;
    }

    public int getMaxExp() {
        return this.maxExp;
    }

    public String toString() {
        return this.name +
            "|" + this.exp +
            "|" + this.minExp +
            "|" + this.maxExp +
            "|" + this.area;
    }
}
