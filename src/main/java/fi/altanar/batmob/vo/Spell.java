package fi.altanar.batmob.vo;

import java.util.regex.Pattern;

public class Spell implements Cloneable {
    public String name;
    public Pattern pattern;
    public Object[] attributeValuePairs;
    public String trigger = "";
    public String caster = "";

    public Spell(String name, Pattern pattern, Object[] attributeValuePairs) {
        this.name = name;
        this.pattern = pattern;
        this.attributeValuePairs = attributeValuePairs;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getTrigger() {
        return this.trigger;
    }

    public void setCaster(String caster) {
        this.caster = caster;
    }

    public String getCaster() {
        return this.caster;
    }

    public String toString() {
        return this.name +
                "|" + this.caster;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}