package fi.altanar.batmob.vo;

import java.io.Serializable;
import java.util.HashMap;

public class MobSaveObject implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private HashMap<String,Mob> saveData;

    public MobSaveObject( HashMap<String,Mob> mobs ) {
        this.saveData = mobs;
    }

    public HashMap<String,Mob> getData() {
        return this.saveData;
    }
    
}
