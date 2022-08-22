package fi.altanar.batmob.vo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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

    public Mob updateAutofilledFields(Mob mob) {
        Mob m = this.mobs.get(mob.getName());
        if (m != null) {
            if (mob.getExp() > 0) {
                m.setExp(mob.getExp());
            }
            if (mob.getArea() != null && m.getArea() == null) {
                m.setArea(mob.getArea());
            }
            this.mobs.put(m.getName(), m);

            return m;
        }
        return null;
    }

    public Mob updateEditableFields(Mob mob) {
        Mob m = this.mobs.get(mob.getName());
        if (m != null) {
            m.setAlignment(mob.getAlignment());
            m.setRace(mob.getRace());
            m.setShortNames(mob.getShortNames());
            m.setSkills(mob.getSkills());
            m.setSpells(mob.getSpells());
            m.setNotes(mob.getNotes());
            m.setRep(mob.getRep());
            m.setRixx(mob.isRixx());
            this.mobs.put(m.getName(), m);

            return m;
        }
        return null;
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

        // Trim all mob names
        HashMap<String,Mob> fixed = new HashMap<String,Mob>();
        for (Map.Entry<String, Mob> entry : this.mobs.entrySet()) {
            String tName = entry.getKey().trim();
            if (!tName.startsWith("|")) {
                Mob e = entry.getValue();
                fixed.put(tName, e);
            }
        }

        this.mobs = fixed;
    }

    public void remove(Mob m) {
        this.mobs.remove(m.getName());
    }

    public Iterator<Entry<String,Mob>> iterator() {
        return this.mobs.entrySet().iterator();
    }

}
