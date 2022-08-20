package fi.altanar.batmob.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogPanel extends JPanel implements ComponentListener {

    private static final long serialVersionUID = 8922764153155463898L;
    private JTextArea logTextArea = new JTextArea();
    private final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private final Color BG_COLOR = Color.BLACK;
    private final int BORDERLINE = 7;
    private JScrollPane scrollPane;

    public LogPanel() {
        this.addComponentListener( this );
        this.setLayout( null );
        logTextArea.setWrapStyleWord( true );
        logTextArea.setEditable( false );
        scrollPane = new JScrollPane( logTextArea );
        scrollPane.setPreferredSize( new Dimension( 800, 534 ) );
        scrollPane.setBounds( BORDERLINE, BORDERLINE, 800, 534 );
        this.setPreferredSize( new Dimension( 800, 534 ) );
        this.setBackground( BG_COLOR );
        this.logTextArea.setText("Log started");
        logTextArea.setForeground( TEXT_COLOR );
        logTextArea.setBackground( BG_COLOR );
        logTextArea.setLineWrap( true );
        this.add( scrollPane );
    }

    public void appendText(String text) {
        this.logTextArea.append(text);
    }

    @Override
    public void componentHidden( ComponentEvent e ) {

    }

    @Override
    public void componentMoved( ComponentEvent e ) {

    }

    @Override
    public void componentResized( ComponentEvent e ) {
        this.scrollPane.setBounds( BORDERLINE, BORDERLINE, this.getWidth() - BORDERLINE * 2, this.getHeight() - BORDERLINE * 2 );

    }

    @Override
    public void componentShown( ComponentEvent e ) {

    }


}
