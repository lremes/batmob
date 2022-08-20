package fi.altanar.batmob.vo;

import java.util.HashMap;

public class MobStore {

    private HashMap<String, Mob> mobs = new HashMap<String,Mob>();

    private String currentAreaName;

    public MobStore() {
        super();
    }

    public String getCurrentAreaName() {
        return currentAreaName;
    }

    public void setCurrentAreaName(String currentAreaName) {
        this.currentAreaName = currentAreaName;
    }

    /**
     * Store mob data.
     * 
     * @param mob
     * @return false is this is a new entry, true if update
     */
    public boolean store(Mob mob) {
        boolean ret = this.mobs.containsKey(mob.getName());
        mob.setArea(this.currentAreaName);
        this.mobs.put(mob.getName(), mob);
        return ret;
    }

    public Mob get(String name) {
        return this.mobs.get(name);
    }

    public boolean contains(String name) {
        return this.mobs.containsKey(name);
    }

    public int getCount() {
        return this.mobs.size();
    }

    public MobSaveObject getSaveObject() {
        return new MobSaveObject(this.mobs);
    }

    public void restoreFromSaveObject(MobSaveObject saved) {
        this.mobs = saved.getData();
    }

}
