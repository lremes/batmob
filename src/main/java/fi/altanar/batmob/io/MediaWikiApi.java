package fi.altanar.batmob.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.*;

import fi.altanar.batmob.vo.Mob;

public class MediaWikiApi {

    private static final int TIMEOUT_MS = 10000;

    private String endpoint;

    Pattern redirect = Pattern.compile("\\s*#REDIRECT\\s*\\[\\[([^\\]|#]+).*", Pattern.CASE_INSENSITIVE);

    public MediaWikiApi(String endpoint) {
        this.endpoint = endpoint;
    }

    public Mob fetchMobInfo(String q) {
        return fetchMobInfo(q, q, true);
    }

    private Mob fetchMobInfo(String q, String page, boolean followRedirect) {
        Mob mob = null;
        try {
            StringBuilder sb = new StringBuilder(endpoint);
            sb.append("/w/index.php?action=raw&title=");
            sb.append(URLEncoder.encode(page, "UTF-8"));

            URL url = new URL(sb.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                System.out.println("No match found for [" + q + "]");
                conn.disconnect();
                return null;
            }

            InputStream is = conn.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String inputLine;

            ArrayList<String> spells = new ArrayList<String>();
            ArrayList<String> skills = new ArrayList<String>();

            boolean firstLine = true;
            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    Matcher m = redirect.matcher(inputLine);
                    if (m.matches()) {
                        in.close();
                        if (followRedirect) {
                            return fetchMobInfo(q, m.group(1).trim(), false);
                        }
                        return null;
                    }
                }
                if (inputLine.startsWith("}}")) {
                    break;
                }
                if (inputLine.startsWith("|")) {
                    int idx = inputLine.indexOf('=');
                    if (idx < 0) {
                        continue;
                    }
                    if (mob == null) {
                        mob = new Mob(0, q);
                    }
                    String prop = inputLine.substring(1, idx).trim();
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
                }
            }
            in.close();

            if (mob == null) {
                System.out.println("No match found for [" + q + "]");
                return null;
            }

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
