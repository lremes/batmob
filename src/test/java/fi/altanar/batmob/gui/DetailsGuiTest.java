package fi.altanar.batmob.gui;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import fi.altanar.batmob.controller.MobEngine;

import static org.mockito.Mockito.*;

public class DetailsGuiTest {

    public static void main( String[] args ) {

        JFrame frame = new JFrame( "" );
        frame.setLayout( new FlowLayout() );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        MobEngine engine = mock(MobEngine.class);
        MobDetailsPanel panel = new MobDetailsPanel(engine);

        frame.getContentPane().add( panel );
        frame.setSize( 1200, 800 );
        frame.setVisible( true );

    }
}