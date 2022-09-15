package fi.altanar.batmob.vo;

public class MobFilter {
    
    public String name = "";
    public String area = "";
    public int minExp = 0;
    public String race = "";
    public String alignment = "";
    public boolean isZinium = false;

    public String toString() {
        return name + "|" +
            area + "|" +
            race + "|" +
            alignment + "|" +
            isZinium;
    }

}
