package fi.altanar.batmob.vo;

import java.util.regex.Pattern;

public class Spell implements Cloneable {
    public String name;
    public Pattern pattern;
    public Object[] attributeValuePairs;
    public String trigger = "";

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

    public String toString() {
        return this.name +
                "|" + this.trigger;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}