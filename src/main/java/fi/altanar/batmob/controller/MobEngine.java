package fi.altanar.batmob.controller;

import java.awt.Dimension;
import java.awt.Point;

import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ClientGUI;
import com.mythicscape.batclient.interfaces.ParsedResult;

import fi.altanar.batmob.io.GuiDataPersister;
import fi.altanar.batmob.io.ILogger;
import fi.altanar.batmob.io.MediaWikiApi;
import fi.altanar.batmob.io.MobDataPersister;
import fi.altanar.batmob.io.IMobListener;
import fi.altanar.batmob.io.IMobStoreListener;
import fi.altanar.batmob.io.IStatusListener;
import fi.altanar.batmob.io.ISpellListener;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobSaveObject;
import fi.altanar.batmob.vo.MobStore;
import fi.altanar.batmob.vo.Spell;

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
    private SpellTriggers spellTriggers = new SpellTriggers();
    private ClientGUI clientGui;

    private ArrayList<Mob> roomMobs = new ArrayList<Mob>();

    private ArrayList<IStatusListener> statusListeners = new ArrayList<IStatusListener>();
    private ArrayList<ISpellListener> spellListeners = new ArrayList<ISpellListener>();

    // [1;32mVlad the Inhaler, the slavic golem[0m
    private static final String GREEN_BOLD = "\u001b[1;32m";
    private static final String RED_BOLD = "\u001b[1;31m";

    public static final String[] IGNORED = new String[] {
            "Your ",
            "You ",
            "'",
            "A hot",
            "an essence",
            "a flask",
            "A virulent"
    };

    // public static final Pattern IGNORE_MAPS = Pattern.compile("");
    // public static final Pattern IGNORE_MAPS = Pattern.compile("^\\w{9}\\s");
    public static final Pattern IGNORED_PATTERNS = Pattern.compile(
            "^[\\Q{<([\\E]+[\\w\\s-]{10}[\\Q>])}\\E]+|^[\\Q<([{\\E]+[\\d\\s]{3}[\\Q>]})\\E]+|^[(\\[][\\d\\s]{3}[])]|^\\[\\d\\d:\\d\\d\\]|^[\\Q?^*+$~|\\/\\Ef#vyrbhHxfzFpd]{9}\\s+");
    // public static final Pattern IGNORE_TITLES =
    // Pattern.compile("^[\\(\\[][\\d\\s]{3}[\\]\\)]");

    private ArrayList<IMobListener> listeners = new ArrayList<IMobListener>();

    private MediaWikiApi queryEngine;

    public MobEngine(MobPlugin plugin, MobStore store, MediaWikiApi queryEngine) {
        this.plugin = plugin;
        this.mobStore = store;
        this.queryEngine = queryEngine;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // Object subject = e.getItem();
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public void setClientGui(ClientGUI gui) {
        this.clientGui = gui;
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

    public void doCommand(String string) {
        this.clientGui.doCommand(string);

    }

    public Mob trigger(ParsedResult input) {
        String stripped = input.getStrippedText().trim();

        Object spell = this.spellTriggers.process(stripped);
        if (spell instanceof Spell) {
            this.notifySpellListeners((Spell) spell);
        }

        Object obj = this.triggers.process(stripped);
        if (obj instanceof Mob) {
            // from pkills
            Mob mob = (Mob) obj;
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

        for (String s : IGNORED) {
            if (strippedName.startsWith(s)) {
                return null;
            }
        }

        if (IGNORED_PATTERNS.matcher(strippedName).find()) {
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
            // update status of old mobs if some data is missing
            if (m.getArea() == null || m.getArea() != this.currentAreaName) {
                m.setArea(this.currentAreaName);
            }
            m.setAggro(isAgro);
            m = this.mobStore.updateAutofilledFields(m);
        }

        while (this.roomMobs.size() > 25) {
            this.roomMobs.remove(0); // remove oldest entry from view
        }

        // latest mob to bottom of list
        int pos = this.roomMobs.indexOf(m);
        if (pos == -1) {
            this.roomMobs.add(m);
        } else {
            this.roomMobs.remove(pos); // remove oldest entry from view
            this.roomMobs.add(m);
        }

        for (Iterator<IMobListener> iter = this.listeners.iterator(); iter.hasNext();) {
            IMobListener ml = iter.next();
            ml.mobsDetected(this.roomMobs);
        }
        return m;
    }

    public void setBatWindow(BatWindow clientWin) {
        this.batWindow = clientWin;
    }

    public void saveGuiData(Point location, Dimension size) {
        GuiDataPersister.save(this.baseDir, location, size);
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    public void addMobListener(IMobListener l) {
        this.listeners.add(l);
    }

    public void addSpellListener(ISpellListener l) {
        this.spellListeners.add(l);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        if (this.batWindow != null) {
            GuiDataPersister.save(this.baseDir, this.batWindow.getLocation(), this.batWindow.getSize());
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (this.batWindow != null) {
            GuiDataPersister.save(this.baseDir, this.batWindow.getLocation(), this.batWindow.getSize());
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    public void sendToMud(String command) {
        this.plugin.doCommand(command);
    }

    public void sendToParty(String message) {
        this.plugin.doCommand("party say " + message);
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

    public void addStatusListener(IStatusListener l) {
        this.statusListeners.add(l);
    }

    public void notifyStatusListeners(String str) {
        for (IStatusListener s : this.statusListeners) {
            s.statusChanged(str);
        }
    }

    public void notifySpellListeners(Spell spell) {
        for (ISpellListener s : this.spellListeners) {
            s.spellDetected(spell);
        }
    }

    public void log(String msg) {
        if (this.baseDir != null) {
            try {
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

                File logFile = getFile("logs", dateFormat.format(date) + "-batmob.txt");
                FileWriter myWriter = new FileWriter(logFile, true);
                myWriter.write(msg + '\n');
                myWriter.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private File getFile(String subDir, String filename) {
        File dirFile = new File(this.baseDir, subDir);
        File file = new File(dirFile, filename);
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
