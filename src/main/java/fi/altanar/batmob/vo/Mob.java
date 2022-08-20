package fi.altanar.batmob.vo;

import java.io.Serializable;

public class Mob implements Serializable {
    
    private static final long serialVersionUID = 0L;

    private String name;
    private int exp;
    private int minExp;
    private int maxExp;
    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

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
