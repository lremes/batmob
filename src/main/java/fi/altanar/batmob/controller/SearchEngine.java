package fi.altanar.batmob.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
        filter.normalize();
        ArrayList<Mob> results = new ArrayList<Mob>();
        Iterator<Entry<String,Mob>> i = this.store.iterator();
        
        try {
            while(i.hasNext()) {
                Mob m = i.next().getValue();
                if (filter.name != null && ! filter.name.equals("")) {
                    if (m.getName() == null || !m.getName().toLowerCase().contains(filter.name)) {
                        continue;
                    }
                }
                if (filter.area != null && ! filter.area.equals("")) {
                    if (m.getArea() == null) {
                        continue;
                    }
                    if (filter.exact && !m.getArea().toLowerCase().equals(filter.area)) {
                        continue;
                    } else if (!m.getArea().toLowerCase().contains(filter.area)) {
                        continue;
                    }
                } 
                if (filter.minExp > 0) {
                    if (m.getMinExp() < filter.minExp) {
                        continue;
                    }
                }
                if (filter.race != null  && ! filter.race.equals("")) {
                    if (m.getRace() == null || !m.getRace().toLowerCase().contains(filter.race)) {
                        continue;
                    }
                }
                if (filter.alignment != null && ! filter.alignment.equals("")) {
                    if (m.getAlignment() == null || !m.getAlignment().toLowerCase().contains(filter.alignment)) {
                        continue;
                    }
                }
                if (filter.isZinium != null && filter.isZinium) {
                    if (!m.isZinium()) {
                        continue;
                    }
                }
                System.out.println(m.getName());
                results.add(m);
            }

            Collections.sort(results, new Comparator<Mob>() {
                @Override
                public int compare(final Mob object1, final Mob object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });

        } catch (Throwable ex) {
            if (this.logger != null) {
                this.logger.log(ex.toString());
            } else {
                System.out.println(ex.toString());
            }
        }

        return results;
    }
}


