package fi.altanar.batmob.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fi.altanar.batmob.io.IMobStoreListener;

public class MobStore {

    private HashMap<String, Mob> mobs = new HashMap<String,Mob>();

    private ArrayList<IMobStoreListener> listeners = new ArrayList<IMobStoreListener>();
    public MobStore() {
        super();
    }

    /**
     * Store mob data.
     * 
     * @param mob
     * @return false is this is a new entry, true if update
     */
    public boolean store(Mob mob) {
        if (!mob.getName().isEmpty()) {
            boolean ret = this.mobs.containsKey(mob.getName());
            this.mobs.put(mob.getName(), mob);
            return ret;
        }
        return false;
    }

    public void addListener(IMobStoreListener l) {
        this.listeners.add(l);
    }

    public Mob updateAutofilledFields(Mob mob) {
        String name = mob.getName();
        Mob existingEntry = this.mobs.get(name);
        if (existingEntry != null) {
            existingEntry.updateExp(mob.getExp());
            existingEntry.setArea(mob.getArea());
            existingEntry.setAggro(mob.isAggro());
            this.mobs.put(name, existingEntry);

            return existingEntry;
        }
        return null;
    }

    public Mob updateExp(Mob mob) {
        String name = mob.getName();
        Mob existingEntry = this.mobs.get(name);
        if (existingEntry != null) {
            existingEntry.updateExp(mob.getExp());
            this.mobs.put(name, existingEntry);
            return existingEntry;
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
            m.setZinium(mob.isZinium());
            m.setAggro(mob.isAggro());
            m.setUndead(mob.isUndead());
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

    public boolean contains(Mob m) {
        // pkills limits name to 57 chars
        if (m.getName().length() > 57) {
            return this.mobs.containsKey(m.getName().substring(0,56));
        }
        return this.mobs.containsKey(m.getName());
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
            Mob e = entry.getValue();
            if (tName.contains(" (undead)")) {
                e.setUndead(true);
                tName.replace(" (undead)", "");
            }
            if (tName.length() > 57) {
                tName = tName.substring(0, 56);
            }
            // Remove names starting with |
            if (!tName.startsWith("|")) {
                fixed.put(tName, e);
            }
        }

        this.mobs = fixed;
    }

    public void remove(Mob m) {
        this.mobs.remove(m.getName());
        for (IMobStoreListener l : this.listeners) {
            l.mobRemoved(m);
        }
    }

    public Iterator<Entry<String,Mob>> iterator() {
        return this.mobs.entrySet().iterator();
    }

}
