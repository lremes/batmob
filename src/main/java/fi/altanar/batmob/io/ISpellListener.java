package fi.altanar.batmob.io;

import java.util.function.Predicate;

import fi.altanar.batmob.vo.Spell;

public interface ISpellListener {
    public void spellDetected(Spell spell);

    // spells already listed whose caster turned out to be in the party
    public default void partyCastersFound(Predicate<String> isMember) {
    }
}
