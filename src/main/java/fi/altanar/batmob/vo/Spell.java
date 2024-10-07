package fi.altanar.batmob.vo;

import java.util.regex.Pattern;

public class Spell {
    public String name;
    public Pattern pattern;
    public Object[] attributeValuePairs;

    public Spell(String name, Pattern pattern, Object[] attributeValuePairs) {
        this.name = name;
        this.pattern = pattern;
        this.attributeValuePairs = attributeValuePairs;
    }
}