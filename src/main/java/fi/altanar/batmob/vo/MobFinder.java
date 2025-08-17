package fi.altanar.batmob.vo;

import java.util.ArrayList;
import java.util.Iterator;
import fi.altanar.batmob.vo.Mob;

public class MobFinder {

    protected ArrayList<Mob> mobs;

    public MobFinder(ArrayList<Mob> mobs) {
        super();
        this.mobs = mobs;
    }

    public Mob findByShortName(Mob mob) {
        if (mob.getShortNames() != null && mob.getShortNames().isEmpty()) {
            return null; // No short names to match against
        }

        Object[] names = mob.getShortNames().toArray();
        Iterator<Mob> mi = this.mobs.iterator();
        while (mi.hasNext()) {
            Mob m = mi.next();
            if (m.getShortNames() != null && !m.getShortNames().isEmpty()) {
                Iterator<String> it = m.getShortNames().iterator();
                while (it.hasNext()) {
                    String shortName = it.next();
                    if (shortName.equals((String) names[0])) {
                        return m;
                    }
                }
            }
            // try partial match with short name
            if (m.getName().toLowerCase().contains(((String) names[0]).toLowerCase())) {
                return m;
            }
        }
        return null;
    }
}
