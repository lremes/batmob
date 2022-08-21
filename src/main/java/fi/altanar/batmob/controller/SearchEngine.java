package fi.altanar.batmob.controller;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class SearchEngine implements ItemListener {
    private MobPlugin plugin;

    public SearchEngine(MobPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void itemStateChanged( ItemEvent e ) {
    }
}
