package fi.altanar.batmob.gui;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.controller.MobPlugin;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobStore;

import static org.mockito.Mockito.*;

public class SearchGuiTest {

    public static void main( String[] args ) {

        JFrame frame = new JFrame( "" );
        frame.setLayout( new FlowLayout() );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        MobStore store = new MobStore();
        MobPlugin plugin = mock(MobPlugin.class);
        MobEngine engine = new MobEngine(plugin, store, null);

        SearchPanel panel = new SearchPanel(engine);

        Mob m = new Mob(0, "a rabbit");
        m.setArea("REALM_MAP");
        store.store(m);

        m = new Mob(0, "a fox");
        m.setZinium(true);
        store.store(m);

        m = new Mob(0, "a jumpy rabbit");
        m.setArea("Diggas");
        store.store(m);

        frame.setSize( 1200, 800 );
        frame.getContentPane().add( panel );
        frame.setVisible( true );
    }
}