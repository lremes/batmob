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
 * Each complete listing replaces the previous member list. Everyone seen in a
 * listing is remembered as a player, as the party changes during the session.
 */
public class PartyTriggers {

    final Pattern PARTY_START = Pattern.compile("^,-{20,}\\.$");
    final Pattern PARTY_END = Pattern.compile("^`-{20,}'$");
    // idle marker, place (row.column or ?.?) and the member name
    final Pattern PARTY_MEMBER = Pattern.compile("^\\|[* ]([1-3?])\\.([1-3?])\\s+([A-Za-z]+)\\s.*");

    final Pattern MEMBER_JOINED = Pattern.compile("^([A-Z][a-z]+) is now acting member of the party\\.$");
    final Pattern MEMBER_LEFT = Pattern
            .compile("^(?:([A-Z][a-z]+) left the party|[A-Z][a-z]+ kicks ([A-Z][a-z]+) out of party)\\.$");
    // our party ended, or we joined another one whose members come with the next listing
    final Pattern PARTY_GONE = Pattern.compile("^(?:You left the party|You disband the party|[A-Z][a-z]+ disbands the party"
            + "|You are kicked out of party|You were the last member of the party|You are a member of the party now)\\.$");

    private Set<String> members = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

    // everyone who has been in the party, players are never mobs
    private Set<String> players = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

    // members of the listing being read, null when not inside one
    private Set<String> pending = null;

    /**
     * @return true if the party member list changed
     */
    public boolean process(String input) {
        // the client may hand the whole listing over as one multi-line chunk
        if (input.indexOf('\n') >= 0) {
            boolean changed = false;
            for (String line : input.split("\\r?\\n")) {
                changed |= this.processLine(line.trim());
            }
            return changed;
        }
        return this.processLine(input);
    }

    private boolean processLine(String input) {
        // before the member messages, "You left the party." isn't about a member called You
        if (PARTY_GONE.matcher(input).matches()) {
            if (this.members.isEmpty()) {
                return false;
            }
            this.members = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            return true;
        }

        Matcher joined = MEMBER_JOINED.matcher(input);
        if (joined.matches()) {
            this.players.add(joined.group(1));
            return this.members.add(joined.group(1));
        }

        Matcher left = MEMBER_LEFT.matcher(input);
        if (left.matches()) {
            return this.members.remove(left.group(1) != null ? left.group(1) : left.group(2));
        }

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
                this.players.addAll(listed);
                return true;
            }
        }
        return false;
    }

    /**
     * @param name caster name, or the whole line when the caster wasn't recognized
     * @return true for a current or former party member, their minions ("Brurin's soul") or a line starting with a member name
     */
    public boolean isMember(String name) {
        if (name == null) {
            return false;
        }
        String trimmed = name.trim();
        if (this.players.contains(trimmed)) {
            return true;
        }
        String lower = trimmed.toLowerCase();
        for (String member : this.players) {
            String m = member.toLowerCase();
            if (lower.startsWith(m + "'s ") || lower.startsWith(m + " ")) {
                return true;
            }
        }
        return false;
    }

    public Set<String> getMembers() {
        return this.members;
    }
}
