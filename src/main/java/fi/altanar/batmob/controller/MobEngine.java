package fi.altanar.batmob.controller;

import java.awt.Dimension;
import java.awt.Point;

import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ParsedResult;

import fi.altanar.batmob.io.GuiDataPersister;
import fi.altanar.batmob.io.MobDataPersister;
import fi.altanar.batmob.io.MobListener;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobSaveObject;
import fi.altanar.batmob.vo.MobStore;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;

public class MobEngine implements ItemListener, ComponentListener {

    String baseDir;
    BatWindow batWindow;
    MobPlugin plugin;
    MobStore mobStore = new MobStore();
    private RegexTrigger triggers = new RegexTrigger();
    private String currentAreaName = "";

    private ArrayList<Mob> roomMobs = new ArrayList<Mob>();

    // [1;32mVlad the Inhaler, the slavic golem[0m
    private static final String GREEN_BOLD = "\u001b[1;32m";
    private static final String RED_BOLD = "\u001b[1;31m";

    private ArrayList<MobListener> listeners = new ArrayList<MobListener>();
    
    public MobEngine(MobPlugin plugin) {
        this.plugin = plugin;
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

    public void trigger(ParsedResult input) {
        String stripped = input.getStrippedText().trim();
        Object obj = this.triggers.process(stripped);
        if (obj instanceof Mob) {
            Mob mob = (Mob)obj;
            if (!this.mobStore.store(mob)) {
                plugin.log("NEW: " + mob.toString());
            }
        } else {
            String orig = input.getOriginalText();
            if (orig.startsWith(GREEN_BOLD)) {
                this.handleMob(stripped);
            } else if (orig.startsWith(RED_BOLD)) {
                if (!(stripped.startsWith("You") || stripped.startsWith("( "))) {
                    this.handleMob(stripped);
                }
            }
        }
    }

    private void handleMob(String stripped) {
        Mob m = this.mobStore.get(stripped);
        if (m == null) {
            m = new Mob(0, stripped);
            this.mobStore.store(m);
        }
        this.roomMobs.add(m);
        for (Iterator<MobListener> iter = this.listeners.iterator(); iter.hasNext();) {
            plugin.log("Detected " + this.roomMobs.size() + "mobs in the room.");
            MobListener ml = iter.next();
            ml.mobsDetected(this.roomMobs);
        }
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
    
    public void addMobListener(MobListener l) {
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

        this.mobStore.setCurrentAreaName(currentAreaName);
    }

    public void load() {
        try {
            plugin.log("Loading mobs...");
            MobSaveObject saved = MobDataPersister.load(this.baseDir);
            if (saved != null) {
                this.mobStore.restoreFromSaveObject(saved);
                plugin.log("Loaded " + mobStore.getCount() + " mobs.");
                Iterator<Entry<String,Mob>> it = this.mobStore.iterator();
                while (it.hasNext()) {
                    plugin.log(it.next().getValue().getName());
                }
            }
        } catch (IOException ioe) {
            plugin.log(ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            plugin.log(cnfe.getMessage());
        }
    }

    public void saveMobs() {
        if (this.mobStore != null) {
            try {
                plugin.log("Saving " + this.mobStore.getCount() + " mobs.");
                MobDataPersister.save(this.baseDir, this.mobStore.getSaveObject());
            } catch (IOException e) {
                plugin.log(e.getMessage());
            }
        }
    }

    public MobStore getMobStore() {
        return this.mobStore;
    }

    // TODO: trigger this when we move.
    // otherwise looking will also update the list
    public void roomChanged(ActionEvent event) {
        plugin.log("Room changed");
        this.roomMobs.clear();
    }
}
