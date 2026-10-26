package fi.altanar.batmob.controller;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tracks party members from the "party status" (pss) output:
 *
 * ,-----------------------------------------------------------------------------.
 * |* 1.1   Name        ldr  ...
 * | 1.2   Other       fol  ...
 * `-----------------------------------------------------------------------------'
 *
 * Each complete listing replaces the previous member list.
 */
public class PartyTriggers {

    final Pattern PARTY_START = Pattern.compile("^,-{20,}\\.$");
    final Pattern PARTY_END = Pattern.compile("^`-{20,}'$");
    // idle marker, place (row.column or ?.?) and the member name
    final Pattern PARTY_MEMBER = Pattern.compile("^\\|[* ]([1-3?])\\.([1-3?])\\s+([A-Za-z]+)\\s.*");

    private Set<String> members = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

    // members of the listing being read, null when not inside one
    private Set<String> pending = null;

    /**
     * @return true if the party member list changed
     */
    public boolean process(String input) {
        if (PARTY_START.matcher(input).matches()) {
            this.pending = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            return false;
        }

        if (this.pending == null) {
            return false;
        }

        Matcher m = PARTY_MEMBER.matcher(input);
        if (m.matches()) {
            this.pending.add(m.group(3));
            return false;
        }

        if (PARTY_END.matcher(input).matches()) {
            Set<String> listed = this.pending;
            this.pending = null;

            // the same box style is used by other outputs, only a listing with members is a party
            if (!listed.isEmpty() && !listed.equals(this.members)) {
                this.members = listed;
                return true;
            }
        }
        return false;
    }

    public boolean isMember(String name) {
        return name != null && this.members.contains(name.trim());
    }

    public Set<String> getMembers() {
        return this.members;
    }
}
