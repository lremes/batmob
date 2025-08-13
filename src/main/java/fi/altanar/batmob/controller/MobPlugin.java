package fi.altanar.batmob.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ParsedResult;

import fi.altanar.batmob.gui.MobDetailsPanel;
import fi.altanar.batmob.gui.SearchPanel;
import fi.altanar.batmob.gui.SpellsPanel;
import fi.altanar.batmob.io.GuiDataPersister;
import fi.altanar.batmob.vo.GuiData;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobStore;
import fi.altanar.batmob.io.MediaWikiApi;
import fi.altanar.batmob.io.IMobListener;

public class MobPlugin extends BatClientPlugin implements
        BatClientPluginTrigger,
        ActionListener,
        BatClientPluginUtil,
        IMobListener,
        InternalFrameListener {

    private String BASEDIR = null;

    private MobEngine engine;
    private SearchPanel searchPanel;
    private SearchEngine searchEngine;
    private MobDetailsPanel mobDetailPanel;
    private SpellsPanel spellsPanel;

    private final String CHANNEL_PREFIX = "BAT_MAPPER";
    private final int PREFIX = 0;
    private final int AREA_NAME = 1;

    private final int MESSAGE_LENGTH = 9;
    private final int EXIT_AREA_LENGTH = 2;

    private final String EXIT_AREA_MESSAGE = "REALM_MAP";

    private BatWindow clientWin;

    public void loadPlugin() {
        BASEDIR = this.getBaseDirectory();
        GuiData guiData = GuiDataPersister.load(BASEDIR);

        if (guiData != null) {
            clientWin = this.getClientGUI().createBatWindow("Mobs", guiData.getX(), guiData.getY(), guiData.getWidth(),
                    guiData.getHeight());
        } else {
            clientWin = this.getClientGUI().createBatWindow("Mobs", 300, 300, 820, 550);
        }

        MobStore store = new MobStore();
        MediaWikiApi api = new MediaWikiApi("https://taikajuoma.ovh/");

        engine = new MobEngine(this, store, api);
        engine.setBatWindow(clientWin);
        engine.setBaseDir(BASEDIR);
        engine.setClientGui(this.getClientGUI());

        clientWin.addInternalFrameListener(this);

        searchEngine = new SearchEngine(store, engine);
        searchPanel = new SearchPanel(searchEngine);
        searchPanel.addMobListener(this);
        clientWin.addComponentListener(searchPanel);

        mobDetailPanel = new MobDetailsPanel(engine, searchEngine);
        engine.addStatusListener(mobDetailPanel);
        clientWin.newTab("Details", mobDetailPanel);
        clientWin.newTab("Search", searchPanel);

        spellsPanel = new SpellsPanel(engine);
        clientWin.newTab("Spells", spellsPanel);

        clientWin.setVisible(true);
        clientWin.removeTabAt(0);
        this.getPluginManager().addProtocolListener(this);
        clientWin.addComponentListener(engine);

        engine.addMobListener(this.mobDetailPanel);
        engine.addSpellListener(this.spellsPanel);

        engine.load();
    }

    @Override
    public String getName() {
        return "batMob";
    }

    public void search(String text) {
        String[] parts = text.split(" ");

        if (parts.length > 1) {
            this.getClientGUI().printText("general", "Top 15 Search results\n", "6AFA63");
            this.getClientGUI().printText("general", "------------------------------\n", "6AFA63");
            MobFilter f = new MobFilter();
            f.exact = false;
            f.name = parts[1];

            ArrayList<Mob> results = this.searchEngine.search(f);
            int i = 0;
            for (ListIterator<Mob> iter = results.listIterator(); iter.hasNext();) {
                Mob m = iter.next();
                this.getClientGUI().printText("general", m.getName() + "\n", "6AFA63");
                i++;
                if (i > 15) {
                    break;
                }
            }
        }
    }

    public void showMobs(String text) {
        String[] parts = text.split(" ");

        if (parts.length > 1) {
            this.getClientGUI().printText("general", "Top 15 Search results\n", "6AFA63");
            this.getClientGUI().printText("general", "------------------------------\n", "6AFA63");
            MobFilter f = new MobFilter();
            f.exact = false;
            f.name = parts[1];

            ArrayList<Mob> results = this.searchEngine.search(f);
            int i = 0;
            for (ListIterator<Mob> iter = results.listIterator(); iter.hasNext();) {
                Mob m = iter.next();
                this.reportMod(m);
                this.getClientGUI().printText("general", m.getName() + "\n", "6AFA63");
                i++;
                if (i > 15) {
                    break;
                }
            }
        }
    }

    public void reportMod(Mob mob) {
        this.engine.doCommand("party report " + mob.getName());
        this.engine.doCommand("party report " + mob.getAllExpAsString());
        if (mob.getRace() != null && !mob.getRace().isEmpty()) {
            this.engine.doCommand("party report Race: " + mob.getRace());
        }
        if (mob.getAlignment() != null && !mob.getAlignment().isEmpty()) {
            this.engine.doCommand("party report Align: " + mob.getAlignment());
        }
        ArrayList<String> spells = mob.getSpells();
        if (spells.size() > 0) {
            this.engine.doCommand("party report Spells: " + spells);
        }
        ArrayList<String> skills = mob.getSkills();
        if (skills.size() > 0) {
            this.engine.doCommand("party report Skills: " + skills);
        }
    }

    public void top() {
        this.engine.doCommand("party report Top exp mobs for " + this.engine.getCurrentAreaName());
        this.engine.doCommand("party report -------------------------------------");

        try {
            MobFilter f = new MobFilter();
            f.area = this.engine.getCurrentAreaName().toLowerCase();
            f.exact = true;
            this.engine.log("Searching " + f);

            ArrayList<Mob> results = this.searchEngine.search(f);
            this.engine.log("Found " + results.size() + " mobs");

            Collections.sort(results, new Comparator<Mob>() {
                @Override
                public int compare(final Mob mob1, final Mob mob2) {
                    return mob2.getMaxExp() - mob1.getMaxExp();
                }
            });

            int i = 0;
            for (ListIterator<Mob> iter = results.listIterator(); iter.hasNext();) {
                Mob m = iter.next();
                this.engine.doCommand("party report " + Integer.toString(m.getMaxExp()) + " \t" + m.getName());
                i++;
                if (i > 15) {
                    break;
                }
            }
        } catch (Exception e1) {
            this.engine.log(e1.getMessage());
        }
    }

    // ArrayList<BatClientPlugin> plugins=this.getPluginManager().getPlugins();
    @Override
    public ParsedResult trigger(ParsedResult input) {
        engine.trigger(input);
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // cMapper;areaname;roomUID;exitUsed;indoor boolean;shortDesc;longDesc;exits

        String input = event.getActionCommand();
        String[] values = input.split(";;", -1);

        if (values[PREFIX].equals(CHANNEL_PREFIX) && values.length == MESSAGE_LENGTH) {
            String areaName = values[AREA_NAME];
            this.engine.setCurrentAreaName(areaName);
            this.engine.log("Area: " + areaName);
        } else if (values[PREFIX].equals(CHANNEL_PREFIX) && values.length == EXIT_AREA_LENGTH) {
            if (values[AREA_NAME].equals(EXIT_AREA_MESSAGE)) {
                this.engine.setCurrentAreaName(EXIT_AREA_MESSAGE);
            }
        }
    }

    @Override
    public void clientExit() {
        this.engine.saveMobs();
    }

    @Override
    public void process(Object input) {
        if (input == null) {
            printConsoleMessage("Mob companion has following commands:");
        }
    }

    private void printConsoleError(String msg) {
        this.getClientGUI().printText("general", "[batMob error] " + msg + "\n", "F7856D");
    }

    private void printConsoleMessage(String msg) {
        this.getClientGUI().printText("general", "[batMob] " + msg + "\n", "6AFA63");
    }

    public void doCommand(String string) {
        this.getClientGUI().doCommand(string);

    }

    @Override
    public void mobSelected(Mob m) {
        this.mobDetailPanel.mobSelected(m);
    }

    @Override
    public void mobsDetected(ArrayList<Mob> mob) {
        // ignored
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void internalFrameClosed(InternalFrameEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void internalFrameClosing(InternalFrameEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent arg0) {
        // TODO Auto-generated method stub
        if (arg0.getSource().equals(clientWin)) {
            this.clientWin.setVisible(true);
        }
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent arg0) {
        // TODO Auto-generated method stub
        if (arg0.getSource().equals(clientWin)) {
            this.clientWin.setVisible(false);
        }
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent arg0) {
        // TODO Auto-generated method stub

    }
}