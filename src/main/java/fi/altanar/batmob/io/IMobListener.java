package fi.altanar.batmob.io;

import java.util.ArrayList;

import fi.altanar.batmob.vo.Mob;

public interface IMobListener {
    public void mobsDetected(ArrayList<Mob> mob);

    public void mobSelected(Mob mob);
}
