package fi.altanar.batmob.controller;

import java.awt.Dimension;
import java.awt.Point;

import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ParsedResult;

import fi.altanar.batmob.io.GuiDataPersister;
import fi.altanar.batmob.io.MobDataPersister;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobSaveObject;
import fi.altanar.batmob.vo.MobStore;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MobEngine implements ItemListener, ComponentListener {

    String baseDir;
    BatWindow batWindow;
    MobPlugin plugin;
    MobStore mobStore = new MobStore();
    private RegexTrigger triggers = new RegexTrigger();
    private String currentAreaName = "";

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
        Object obj = this.triggers.process(input.getStrippedText().trim());
        if (obj instanceof Mob) {
            Mob mob = (Mob)obj;
            if (!mobStore.store(mob)) {
                plugin.log("NEW: " + mob.getName());
            }
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
        plugin.log("Loading mobs...");
        MobSaveObject saved = MobDataPersister.load(this.baseDir);
        if (saved != null) {
            this.mobStore.restoreFromSaveObject(saved);
            plugin.log("Loaded " + mobStore.getCount() + " mobs.");
        }
    }

    public void saveMobs() {
        if (this.mobStore != null) {
            MobDataPersister.save(this.baseDir, this.mobStore.getSaveObject());
            plugin.log("Mobs saved.");
        }
    }

}
