package fi.altanar.batmob.gui;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JFrame;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.controller.MobPlugin;
import fi.altanar.batmob.io.MediaWikiApi;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobStore;

import static org.mockito.Mockito.*;

public class DetailsGuiTest {

    public static void main( String[] args ) {

        JFrame frame = new JFrame( "" );
        frame.setLayout( new FlowLayout() );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        
        MediaWikiApi api = new MediaWikiApi("https://taikajuoma.ovh/");

        ArrayList<Mob> mobs = new ArrayList<Mob>();
        mobs.add(new Mob(1, "Test1"));
        mobs.add(new Mob(2, "Corneliu the dwarven smith"));
        mobs.add(new Mob(3000, "Corneliu the dwarven smith"));

        MobStore store = new MobStore();
        for (Mob m : mobs) {
            store.store(m);
        }

        MobPlugin plugin = mock(MobPlugin.class);
        MobEngine engine = new MobEngine(plugin, store, api);
        MobDetailsPanel panel = new MobDetailsPanel(engine);

        panel.mobsDetected(mobs);

        frame.getContentPane().add( panel );
        frame.setSize( 1200, 800 );
        frame.setVisible( true );

    }
}