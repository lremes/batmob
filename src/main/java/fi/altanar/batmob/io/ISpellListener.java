package fi.altanar.batmob.io;

import fi.altanar.batmob.vo.Spell;

public interface ISpellListener {
    public void spellDetected(Spell spell);
}
