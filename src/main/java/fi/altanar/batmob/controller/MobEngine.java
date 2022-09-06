package fi.altanar.batmob.controller;

import java.awt.Dimension;
import java.awt.Point;

import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ParsedResult;

import fi.altanar.batmob.io.GuiDataPersister;
import fi.altanar.batmob.io.ILogger;
import fi.altanar.batmob.io.MediaWikiApi;
import fi.altanar.batmob.io.MobDataPersister;
import fi.altanar.batmob.io.IMobListener;
import fi.altanar.batmob.io.IMobStoreListener;
import fi.altanar.batmob.io.IStatusListener;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobSaveObject;
import fi.altanar.batmob.vo.MobStore;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.*;

public class MobEngine implements ItemListener, ComponentListener, ILogger, IMobStoreListener {

    private String baseDir;
    private BatWindow batWindow;
    private MobPlugin plugin;
    private MobStore mobStore;
    private RegexTrigger triggers = new RegexTrigger();
    private String currentAreaName = "";

    private SearchEngine searchEngine;

    private ArrayList<Mob> roomMobs = new ArrayList<Mob>();

    private ArrayList<IStatusListener> statusListeners = new ArrayList<IStatusListener>();

    // [1;32mVlad the Inhaler, the slavic golem[0m
    private static final String GREEN_BOLD = "\u001b[1;32m";
    private static final String RED_BOLD = "\u001b[1;31m";

    public static final String[] IGNORED = new String[]{
        "Your ",
        "You ",
        "'",
        "A hot",
        "an essence",
        "a flask"
    };

    public static final Pattern IGNORE_MAPS = Pattern.compile("^[\\Q?*$~|\\/\\EfzFpd^]{9}\\s+");
    //public static final Pattern IGNORE_MAPS = Pattern.compile("^\\w{9}\\s");
    public static final Pattern IGNORE_TITLES = Pattern.compile("^[\\Q{<([\\E]+[\\w\\s-]{10}[\\Q>])}\\E]+|^[\\Q<([{\\E]+[\\d\\s]{3}[\\Q>]})\\E]+|^[(\\[][\\d\\s]{3}[])]");
    //public static final Pattern IGNORE_TITLES = Pattern.compile("^[\\(\\[][\\d\\s]{3}[\\]\\)]");

    private ArrayList<IMobListener> listeners = new ArrayList<IMobListener>();

    private MediaWikiApi queryEngine;

    public MobEngine(MobPlugin plugin, MobStore store, MediaWikiApi queryEngine) {
        this.plugin = plugin;
        this.mobStore = store;
        this.searchEngine = new SearchEngine(this.mobStore);
        this.queryEngine = queryEngine;
    }

    @Override
    public void itemStateChanged( ItemEvent e ) {
        //Object subject = e.getItem();
    }

    public void setBaseDir( String baseDir ) {
        this.baseDir = baseDir;
    }

    public String getBaseDir() {
        return this.baseDir;
    }

    public void setQueryEngine(MediaWikiApi queryEngine) {
        this.queryEngine = queryEngine;
    }

    public MediaWikiApi getQueryEngine() {
        return this.queryEngine;
    }

    public Mob trigger(ParsedResult input) {
        String stripped = input.getStrippedText().trim();
        Object obj = this.triggers.process(stripped);
        if (obj instanceof Mob) {
            // from pkils
            Mob mob = (Mob)obj;
            if (!this.mobStore.contains(mob)) {
                this.log("NEW: " + mob.getName());
                this.mobStore.store(mob);
            } else {
                mob = this.mobStore.updateExp(mob);
            }
            return mob;
        } else {
            // from "look"
            String orig = input.getOriginalText();
            if (orig.startsWith(GREEN_BOLD)) {
                this.log(input.getOriginalText());
                return this.handleMob(stripped, false);
            } else if (orig.startsWith(RED_BOLD)) {
                this.log(input.getOriginalText());
                return this.handleMob(stripped, true);
            }
        }
        return null;
    }

    private Mob handleMob(String strippedName, boolean isAgro) {
        if (strippedName.isEmpty()) {
            return null;
        }

        for (String s: IGNORED) {
            if (strippedName.startsWith(s)) {
                return null;
            }
        }

        if (IGNORE_MAPS.matcher(strippedName).find() || IGNORE_TITLES.matcher(strippedName).find()) {
            return null;
        }

        Mob m = this.mobStore.get(strippedName);
        if (m == null) {
            m = new Mob(0, strippedName);
            m.setArea(this.currentAreaName);
            m.setAggro(isAgro);
            this.log("NEW: " + m.getName());
            this.mobStore.store(m);
        } else {
            // update status of old mobs if data is missing
            if (m.getArea() == null || m.getArea() != this.currentAreaName) {
                m.setArea(this.currentAreaName);
            }
            m.setAggro(isAgro);
            m = this.mobStore.updateAutofilledFields(m);
            this.log("Update: " + m.getName());
        }

        while (this.roomMobs.size() > 20) {
            this.roomMobs.remove(0); // remove oldest entry
        }
        if (!this.roomMobs.contains(m)) {
            this.roomMobs.add(m);
        }

        for (Iterator<IMobListener> iter = this.listeners.iterator(); iter.hasNext();) {
            IMobListener ml = iter.next();
            ml.mobsDetected(this.roomMobs);
        }
        return m;
    }

    public void setBatWindow( BatWindow clientWin ) {
        this.batWindow = clientWin;
    }

    public void saveGuiData( Point location, Dimension size ) {
        GuiDataPersister.save( this.baseDir, location, size );
    }

    @Override
    public void componentHidden( ComponentEvent e ) {

    }

    public void addMobListener(IMobListener l) {
        this.listeners.add(l);
    }

    @Override
    public void componentMoved( ComponentEvent e ) {
        if (this.batWindow != null) {
            GuiDataPersister.save( this.baseDir, this.batWindow.getLocation(), this.batWindow.getSize() );
        }
    }

    @Override
    public void componentResized( ComponentEvent e ) {
        if (this.batWindow != null) {
            GuiDataPersister.save( this.baseDir, this.batWindow.getLocation(), this.batWindow.getSize() );
        }
    }

    @Override
    public void componentShown( ComponentEvent e ) {

    }

    public void sendToMud(String command){
        this.plugin.doCommand( command );
    }

    public void sendToParty(String message){
        this.plugin.doCommand( "party say " + message );
    }

    public String getCurrentAreaName() {
        return currentAreaName;
    }

    public void setCurrentAreaName(String currentAreaName) {
        this.currentAreaName = currentAreaName;
        this.notifyStatusListeners(this.currentAreaName);
    }

    public void load() {
        try {
            this.log("Loading mobs...");
            MobSaveObject saved = MobDataPersister.load(this.baseDir);
            if (saved != null) {
                this.mobStore.restoreFromSaveObject(saved);
                this.log("Loaded " + mobStore.getCount() + " mobs.");
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");

                this.notifyStatusListeners("Saved at " + dateFormat.format(date));
            }
        } catch (IOException ioe) {
            this.log(ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            this.log(cnfe.getMessage());
        }
    }

    public void saveMobs() {
        if (this.mobStore != null) {
            try {
                this.log("Saving " + this.mobStore.getCount() + " mobs.");
                MobDataPersister.save(this.baseDir, this.mobStore.getSaveObject());
            } catch (IOException e) {
                this.log(e.getMessage());
            }
        }
    }

    public MobStore getMobStore() {
        return this.mobStore;
    }

    public SearchEngine getSearchEngine() {
        return this.searchEngine;
    }

    public void addStatusListener(IStatusListener l) {
        this.statusListeners.add(l);
    }

    public void notifyStatusListeners(String str) {
        for (IStatusListener s : this.statusListeners) {
            s.statusChanged(str);
        }
    }

    public void log(String msg) {
        if (this.baseDir != null) {
            try {
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

                File logFile = getFile( "logs", dateFormat.format(date) + "-batmob.txt" );
                FileWriter myWriter = new FileWriter(logFile, true);
                myWriter.write(msg + '\n');
                myWriter.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private File getFile( String subDir, String filename ) {
        File dirFile = new File( this.baseDir, subDir );
        File file = new File( dirFile, filename );
        return file;
    }

    @Override
    public void mobRemoved(Mob m) {
        this.roomMobs.remove(m);
        for (Iterator<IMobListener> iter = this.listeners.iterator(); iter.hasNext();) {
            IMobListener ml = iter.next();
            ml.mobsDetected(this.roomMobs);
        }
    }
}
