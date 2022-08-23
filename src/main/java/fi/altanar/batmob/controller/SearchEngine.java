package fi.altanar.batmob.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobStore;

public class SearchEngine {
    private MobStore store;

    public enum SearchCriteria {
      NAME,
      AREA,
      MIN_EXP,
      MAX_EXP,
      ALIGNMENT,
      RACE,
      REP,
      RIXX
    };

    public SearchEngine(MobStore store) {
        this.store = store;
    }

    public ArrayList<Mob> search( String name, SearchCriteria c, int... threshold) {
        ArrayList<Mob> results = new ArrayList<Mob>();
        Iterator<Entry<String,Mob>> iter = store.iterator();
        switch (c) {
            case NAME:
                while(iter.hasNext())
                {
                    Entry<String,Mob> e = iter.next();

                    if (e.getKey().contains(name)) {
                        results.add(e.getValue());
                    }
                }
                break;
            case AREA:
            while(iter.hasNext())
                {
                    Entry<String,Mob> e = iter.next();

                    if (e.getValue().getArea().contains(name)) {
                        results.add(e.getValue());
                    }
                }
                break;
            case MIN_EXP:
                while(iter.hasNext())
                {
                    Entry<String,Mob> e = iter.next();

                    if (e.getValue().getMinExp() > threshold[0]) {
                        results.add(e.getValue());
                    }
                }
                break;
            case MAX_EXP:
                while(iter.hasNext())
                {
                    Entry<String,Mob> e = iter.next();

                    if (e.getValue().getMaxExp() < threshold[0]) {
                        results.add(e.getValue());
                    }
                }
                break;
            case ALIGNMENT:
                while(iter.hasNext())
                {
                    Entry<String,Mob> e = iter.next();

                    if (e.getValue().getAlignment().contains(name)) {
                        results.add(e.getValue());
                    }
                }
                break;
            case RACE:
                while(iter.hasNext())
                {
                    Entry<String,Mob> e = iter.next();

                    if (e.getValue().getRace().contains(name)) {
                        results.add(e.getValue());
                    }
                }
                break;
            case REP:
                while(iter.hasNext())
                {
                    Entry<String,Mob> e = iter.next();

                    if (e.getValue().getRep().contains(name)) {
                        results.add(e.getValue());
                    }
                }
                break;
            case RIXX:
                while(iter.hasNext())
                {
                    Entry<String,Mob> e = iter.next();

                    if (e.getValue().isRixx()) {
                        results.add(e.getValue());
                    }
                }
                break;

        }
        return results;
    }
}
