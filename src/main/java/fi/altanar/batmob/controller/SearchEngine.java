package fi.altanar.batmob.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map.Entry;

import fi.altanar.batmob.io.ILogger;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobFilter;
import fi.altanar.batmob.vo.MobStore;

public class SearchEngine {
    
    private MobStore store;
    private ILogger logger;

    public SearchEngine(MobStore store, ILogger logger) {
        this.store = store;
        this.logger = logger;
    }

    public MobStore getMobStore() {
        return this.store;
    }

    public ArrayList<Mob> search(MobFilter filter) {
        ArrayList<Mob> results = new ArrayList<Mob>();
        Iterator<Entry<String,Mob>> i = store.iterator();
        
        try {
            while(i.hasNext()) {
                Mob m = i.next().getValue();
                if (filter.name != null && filter.name != "") {
                    if (m.getName() == null || !m.getName().contains(filter.name)) {
                        continue;
                    }
                }
                if (filter.area != null && filter.area != "") {
                    if (m.getArea() == null || !m.getArea().contains(filter.area)) {
                        continue;
                    }
                } 
                if (filter.minExp > 0) {
                    if (m.getMinExp() < filter.minExp) {
                        continue;
                    }
                }
                if (filter.race != null && filter.race != "") {
                    if (m.getRace() == null || !m.getRace().contains(filter.race)) {
                        continue;
                    }
                }
                if (filter.alignment != null && filter.alignment != "") {
                    if (m.getAlignment() == null || !m.getAlignment().contains(filter.alignment)) {
                        continue;
                    }
                }
                if (filter.isZinium) {
                    if (!m.isZinium()) {
                        continue;
                    }
                }
                results.add(m);
            }

            Collections.sort(results, new Comparator<Mob>() {
                @Override
                public int compare(final Mob object1, final Mob object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });

        } catch (Throwable ex) {
            if (logger != null) {
                this.logger.log(ex.toString());
            } else {
                System.out.println(ex.toString());
            }
        }

        return results;
    }
}


