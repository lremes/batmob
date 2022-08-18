package fi.altanar.batmob.vo;

import java.io.Serializable;

public class Mob implements Serializable {
    
    private static final long serialVersionUID = 0L;

    private String name;
    private int[] exp;

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }
/*
* 
public int[] getExp() {
    
}

public void addExp( int exp ) {
    this.exp.append(exp);
}
*/
}
