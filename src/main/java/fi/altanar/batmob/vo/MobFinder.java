package fi.altanar.batmob.vo;

import java.util.ArrayList;
import fi.altanar.batmob.vo.Mob;

public class MobFinder {

    protected ArrayList<Mob> mobs;

    public MobFinder(ArrayList<Mob> mobs) {
        super();
        this.mobs = mobs;
    }

    public Mob findByShortName(Mob mob) {
        if (mob.getShortNames() == null || mob.getShortNames().isEmpty()) {
            return null; // No short names to match against
        }

        String name = mob.getShortNames().get(0);
        for (Mob m : this.mobs) {
            if (m.getShortNames() != null) {
                for (String shortName : m.getShortNames()) {
                    if (shortName.equalsIgnoreCase(name)) {
                        return m;
                    }
                }
            }
        }

        // try partial match with short name
        for (Mob m : this.mobs) {
            if (m.getName().toLowerCase().contains(name.toLowerCase())) {
                return m;
            }
        }
        return null;
    }
}
