package fi.altanar.batmob.gui;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import fi.altanar.batmob.controller.SearchEngine;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobStore;

public class SearchGuiTest {

    public static void main( String[] args ) {

        JFrame frame = new JFrame( "" );
        frame.setLayout( new FlowLayout() );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        MobStore store = new MobStore();
        SearchEngine se = new SearchEngine(store, null);

        SearchPanel panel = new SearchPanel(se);

        Mob m = new Mob(1500, "a rabbit");
        m.setArea("REALM_MAP");
        store.store(m);

        m = new Mob(1000, "a fox");
        m.setZinium(true);
        store.store(m);

        m = new Mob(500, "a jumpy rabbit");
        m.setArea("Diggas");
        store.store(m);

        frame.setSize( 1200, 800 );
        frame.getContentPane().add( panel );
        frame.setVisible( true );
    }
}