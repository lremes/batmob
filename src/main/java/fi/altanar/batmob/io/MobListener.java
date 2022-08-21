package fi.altanar.batmob.io;

import java.util.ArrayList;

import fi.altanar.batmob.vo.Mob;

public interface MobListener {
    public void mobsDetected(ArrayList<Mob> mob);
}
