package fi.altanar.batmob.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.*;

import fi.altanar.batmob.vo.Mob;

public class MediaWikiApi {

    private String endpoint;

    private String baseDir = null;

    Pattern title = Pattern.compile("\\s*\\[title\\] => (.*)$");
    Pattern missing = Pattern.compile("\\s*\\[missing\\] => .*$");

    public MediaWikiApi(String endpoint, String baseDir) {
        this.endpoint = endpoint;
        this.baseDir = baseDir;
    }
    
    public Mob fetchMobInfo(String q) {
        Mob mob = null;
        try {
            StringBuilder sb = new StringBuilder(endpoint);
            sb.append("/w/api.php?action=query&titles=");
            sb.append(URLEncoder.encode(q, "UTF-8"));
            sb.append("&prop=revisions&rvprop=content&format=txt&redirects");

            int state = 0;
            URL url = new URL(sb.toString());

            InputStream is = url.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String inputLine;

            ArrayList<String> spells = new ArrayList<String>();
            ArrayList<String> skills = new ArrayList<String>();

            while ((inputLine = in.readLine()) != null) {
                switch(state) {
                    case 0:
                        Matcher m = title.matcher(inputLine);
                        if(m.matches())
                        {
                            state++;
                        }
                        break;
                    case 1:
                        if (inputLine.startsWith("|")) {
                            if (mob == null) {
                                mob = new Mob(0, q);
                            }
                            int idx = inputLine.indexOf('=');
                            String prop = inputLine.substring(2, idx).trim();
                            String value = inputLine.substring(idx + 1).trim();
                            
                            if (!value.isEmpty()) {
                                if (prop.contains("spell")) {
                                    spells.add(value);
                                } else if (prop.contains("skill")) {
                                    skills.add(value);
                                } else {
                                    switch (prop) {
                                        case "name":
                                            ArrayList<String> snames = new ArrayList<String>();
                                            snames.add(value);
                                            mob.setShortNames(snames);
                                            break;
                                        case "description":
                                            mob.setDescription(value);
                                            break;
                                        case "alignment":
                                            mob.setAlignment(value);
                                            break;
                                        case "align":
                                            mob.setAlignment(value);
                                            break;
                                        case "race":
                                            mob.setRace(value);
                                            break;
                                        case "gender":
                                            mob.setGender(value);
                                            break;
                                        case "undead":
                                            if (value.contains("yes")) {
                                                mob.setUndead(true);
                                            }
                                            break;
                                        case "other":
                                            mob.setNotes(value);
                                            break;
                                    }
                                }
                            }
                        } else if ((missing.matcher(inputLine)).matches()) {
                            System.out.println("No match found for [" + q + "]");
                        }
                        break;
                    case 2:
                        if(inputLine.startsWith("}}"))
                            state++;
                    break;
                }
            }
            in.close();

            if (skills.size() > 0) {
                mob.setSkills(skills);
            }
            if (spells.size() > 0) {
                mob.setSpells(spells);
            }

        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
        return mob;
    }
}
