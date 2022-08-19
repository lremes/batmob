package fi.altanar.batmob.vo;

import java.io.Serializable;

public class Mob implements Serializable {
    
    private static final long serialVersionUID = 0L;

    private String name;
    private int exp;

    public Mob(int exp, String name) {
        this.name = name;
        this.exp = exp;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String toString() {
        return this.name + "|" + this.exp;
    }
}
