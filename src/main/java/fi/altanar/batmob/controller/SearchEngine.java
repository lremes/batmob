package fi.altanar.batmob.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map.Entry;

import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobFilter;
import fi.altanar.batmob.vo.MobStore;

public class SearchEngine {
    
    private MobStore store;

    public SearchEngine(MobStore store) {
        this.store = store;
    }

    public ArrayList<Mob> search(MobFilter filter) {
        ArrayList<Mob> results = new ArrayList<Mob>();
        Iterator<Entry<String,Mob>> iter = store.iterator();
        
        while(iter.hasNext()) {
            Entry<String,Mob> e = iter.next();
            results.add(e.getValue());
        }

        ListIterator<Mob> i = results.listIterator();
        while(i.hasNext()) {
            Mob m = i.next();
            if (filter.name != null && !filter.name.isBlank()) {
                if (m.getName()== null || !m.getName().contains(filter.name)) {
                    i.remove();
                    continue;
                }
            }
            if (filter.area != null && !filter.area.isBlank()) {
                if (m.getArea() == null || !m.getArea().contains(filter.area)) {
                    i.remove();
                    continue;
                }
            } 
            if (filter.minExp > 0) {
                if (m.getMinExp() < filter.minExp) {
                    i.remove();
                    continue;
                }
            }
            if (filter.race != null && !filter.race.isBlank()) {
                if (m.getRace() == null || !m.getRace().contains(filter.race)) {
                    i.remove();
                    continue;
                }
            }
            if (filter.alignment != null && !filter.alignment.isBlank()) {
                if (m.getAlignment() == null || !m.getAlignment().contains(filter.alignment)) {
                    i.remove();
                    continue;
                }
            }
            if (filter.isZinium) {
                if (!m.isZinium()) {
                    i.remove();
                    continue;
                }
            }
        }

        return results;
    }
}


