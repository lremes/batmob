package fi.altanar.batmob.vo;

public class MobFilter {
    
    public String name = "";
    public String area = "";
    public int minExp = 0;
    public String race = "";
    public String alignment = "";
    public Boolean isZinium = null;
    public Boolean exact = false;

    public String toString() {
        return name + "|" +
            area + "|" +
            race + "|" +
            alignment + "|" +
            isZinium;
    }

    public void normalize() {
        this.name = this.name.toLowerCase();
        this.area = this.area.toLowerCase();
        this.race = this.race.toLowerCase();
        this.alignment = this.alignment.toLowerCase();
    }

}
