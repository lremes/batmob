package fi.altanar.batmob.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.text.BadLocationException;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.io.MobListener;
import fi.altanar.batmob.vo.Mob;

public class MobDetailsPanel extends JPanel implements MouseInputListener, ComponentListener, ActionListener, MobListener {

    private static final long serialVersionUID = 8922764153155463898L;
    
    public final Dimension MIN_LAYOUT_SIZE = new Dimension( 800, 550 );
    public final Dimension MIN_PANEL_SIZE = new Dimension( 400, 550 );
    private final int TEXT_INPUT_WIDTH = 380;
    private final int DESC_HEIGHT = 270;
    protected int BORDERLINE = 7;
    private final int BUTTON_HEIGHT = 25;
    private final int BUTTON_WIDTH = 100;
    private final int TEXT_INPUT_HEIGHT = 30;

    private final Color BORDER_COLOR = Color.WHITE;   
    private final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private final Color BG_COLOR = Color.BLACK;

    private JTextArea roomArea = new JTextArea();
    private JScrollPane scrollableRoom;

    private JTextArea notesArea = new JTextArea();
    private JScrollPane scrollableNotes;

    private JTextArea nameArea = new JTextArea();
    private JTextArea descArea = new JTextArea();
    private JTextArea areaNameArea = new JTextArea();
    private JTextArea expArea = new JTextArea();
    private JTextArea shortNameArea = new JTextArea();
    private JTextArea raceArea = new JTextArea();
    private JTextArea alignmentArea = new JTextArea();
    private JTextArea repArea = new JTextArea();

    private Font font = new Font( "Consolas", Font.PLAIN, 14 );
    private Font labelFont = new Font( "Consolas", Font.PLAIN, 12 );
   
    private Mob mob;

    private JButton saveButton;
    private JButton deleteButton;

    MobEngine engine;
    
    public MobDetailsPanel(MobEngine engine) {
        super();

        this.engine = engine;

        this.setPreferredSize( MIN_LAYOUT_SIZE );
        this.setMinimumSize( MIN_LAYOUT_SIZE );

        this.addComponentListener( this );
        this.setLayout( new BoxLayout(this, BoxLayout.X_AXIS) );

        JPanel roomPanel = new JPanel();
        roomPanel.setLayout(new BoxLayout(roomPanel, BoxLayout.Y_AXIS));
        roomPanel.setPreferredSize( MIN_PANEL_SIZE );
        roomPanel.setMinimumSize( MIN_PANEL_SIZE );
        roomPanel.setBackground( BG_COLOR );
        roomPanel.setForeground( TEXT_COLOR );

        roomArea.setWrapStyleWord( true );
        roomArea.setEditable( false );
        roomArea.addMouseListener( this );
        roomArea.setFont( font );
        roomArea.setBackground( BG_COLOR );
        roomArea.setForeground( TEXT_COLOR );
        roomArea.setAlignmentY( Component.TOP_ALIGNMENT );
        roomArea.setColumns( 50 );
        scrollableRoom = new JScrollPane( roomArea );
        scrollableRoom.setPreferredSize( new Dimension( 400, 534 ) );
        scrollableRoom.setBounds( BORDERLINE, BORDERLINE, 800, 534 );
        roomArea.setToolTipText( "Mobs in the current room. Click to show details." );

        roomPanel.add(scrollableRoom);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground( BG_COLOR );
        detailsPanel.setForeground( TEXT_COLOR );
        detailsPanel.setPreferredSize( MIN_PANEL_SIZE );
        detailsPanel.setMinimumSize( MIN_PANEL_SIZE );

        JLabel nameLabel = new JLabel("Long name");
        nameLabel.setBackground( BG_COLOR );
        nameLabel.setForeground( TEXT_COLOR );
        nameLabel.setFont(labelFont);
        nameLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        detailsPanel.add( nameLabel );

        nameArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        nameArea.setEditable( false );
        nameArea.setColumns( 25 );
        nameArea.setBorder( new LineBorder( BORDER_COLOR ) );
        nameArea.setLineWrap( true );
        nameArea.setBackground( BG_COLOR );
        nameArea.setForeground( TEXT_COLOR );
        nameArea.setFont( font );
        nameArea.setToolTipText( "This is the long name of the mob" );
        detailsPanel.add( nameArea );

        JLabel expLabel = new JLabel("Exp (latest / min / max)");
        expLabel.setBackground( BG_COLOR );
        expLabel.setForeground( TEXT_COLOR );
        expLabel.setFont(labelFont);
        detailsPanel.add( expLabel );

        expArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        expArea.setEditable( false );
        expArea.setColumns( 25 );
        expArea.setBorder( new LineBorder( BORDER_COLOR ) );
        expArea.setLineWrap( true );
        expArea.setBackground( BG_COLOR );
        expArea.setForeground( TEXT_COLOR );
        expArea.setFont( font );
        expArea.setToolTipText( "This is the latest/min/max recorded exp for the mob" );
        detailsPanel.add( expArea );

        JLabel descLabel = new JLabel("Description");
        descLabel.setBackground( BG_COLOR );
        descLabel.setForeground( TEXT_COLOR );
        descLabel.setFont(labelFont);
        detailsPanel.add( descLabel );
        
        descArea.setBounds( 0, BORDERLINE, TEXT_INPUT_WIDTH, DESC_HEIGHT );
        descArea.setEditable( false );
        descArea.setColumns( 25 );
        descArea.setBorder( new LineBorder( BORDER_COLOR ) );
        descArea.setAlignmentY( Component.BOTTOM_ALIGNMENT );
        descArea.setLineWrap( true );
        descArea.setBackground( BG_COLOR );
        descArea.setForeground( TEXT_COLOR );
        descArea.setFont( font );
        descArea.setToolTipText( "This is mob description" );
        detailsPanel.add( descArea );

        JLabel areaLabel = new JLabel("Area");
        areaLabel.setBackground( BG_COLOR );
        areaLabel.setForeground( TEXT_COLOR );
        areaLabel.setFont(labelFont);
        detailsPanel.add( areaLabel );

        areaNameArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        areaNameArea.setEditable( false );
        areaNameArea.setColumns( 25 );
        areaNameArea.setBorder( new LineBorder( BORDER_COLOR ) );
        areaNameArea.setLineWrap( true );
        areaNameArea.setBackground( BG_COLOR );
        areaNameArea.setForeground( TEXT_COLOR );
        areaNameArea.setFont( font );
        areaNameArea.setToolTipText( "This is the area where the mob can be found" );
        detailsPanel.add( areaNameArea );

        JLabel snameLabel = new JLabel("Short names");
        snameLabel.setBackground( BG_COLOR );
        snameLabel.setForeground( TEXT_COLOR );
        snameLabel.setFont(labelFont);
        detailsPanel.add( snameLabel );

        shortNameArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        shortNameArea.setEditable( true );
        shortNameArea.setColumns( 25 );
        shortNameArea.setBorder( new LineBorder( BORDER_COLOR ) );
        shortNameArea.setLineWrap( true );
        shortNameArea.setBackground( BG_COLOR );
        shortNameArea.setForeground( TEXT_COLOR );
        shortNameArea.setFont( font );
        shortNameArea.setToolTipText( "This is a comma-separated list of short names for the mob." );
        detailsPanel.add( shortNameArea );

        JLabel raceLabel = new JLabel("Race");
        raceLabel.setBackground( BG_COLOR );
        raceLabel.setForeground( TEXT_COLOR );
        raceLabel.setFont(labelFont);
        detailsPanel.add( raceLabel );

        raceArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        raceArea.setEditable( true );
        raceArea.setColumns( 25 );
        raceArea.setBorder( new LineBorder( BORDER_COLOR ) );
        raceArea.setLineWrap( true );
        raceArea.setBackground( BG_COLOR );
        raceArea.setForeground( TEXT_COLOR );
        raceArea.setFont( font );
        raceArea.setToolTipText( "This is the race of the mob." );
        detailsPanel.add( raceArea );

        JLabel alignmentLabel = new JLabel("Alignment");
        alignmentLabel.setBackground( BG_COLOR );
        alignmentLabel.setForeground( TEXT_COLOR );
        alignmentLabel.setFont(labelFont);
        detailsPanel.add( alignmentLabel );

        alignmentArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        alignmentArea.setEditable( true );
        alignmentArea.setColumns( 25 );
        alignmentArea.setBorder( new LineBorder( BORDER_COLOR ) );
        alignmentArea.setLineWrap( true );
        alignmentArea.setBackground( BG_COLOR );
        alignmentArea.setForeground( TEXT_COLOR );
        alignmentArea.setFont( font );
        alignmentArea.setToolTipText( "This is the alignment of the mob." );
        detailsPanel.add( alignmentArea );

        JLabel repLabel = new JLabel("Rep");
        repLabel.setBackground( BG_COLOR );
        repLabel.setForeground( TEXT_COLOR );
        repLabel.setFont(labelFont);
        detailsPanel.add( repLabel );

        repArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        repArea.setEditable( true );
        repArea.setColumns( 25 );
        repArea.setBorder( new LineBorder( BORDER_COLOR ) );
        repArea.setLineWrap( true );
        repArea.setBackground( BG_COLOR );
        repArea.setForeground( TEXT_COLOR );
        repArea.setFont( font );
        repArea.setToolTipText( "This is the rep gained for the mob." );
        detailsPanel.add( repArea );

        JLabel notesLabel = new JLabel("Notes");
        notesLabel.setBackground( BG_COLOR );
        notesLabel.setForeground( TEXT_COLOR );
        notesLabel.setFont(labelFont);
        detailsPanel.add( notesLabel );

        notesArea.setWrapStyleWord( true );
        notesArea.setEditable( true );
        scrollableNotes = new JScrollPane( notesArea );
        scrollableNotes.setPreferredSize( new Dimension( 400, 534 ) );
        scrollableNotes.setBounds( BORDERLINE, BORDERLINE, 800, 534 );
        notesArea.setColumns( 50 );
        notesArea.setAlignmentY( Component.TOP_ALIGNMENT );
        notesArea.setForeground( TEXT_COLOR );
        notesArea.setBackground( BG_COLOR );
        notesArea.setLineWrap( true );
        detailsPanel.add( scrollableNotes );

        saveButton = new JButton( "Save" );
        saveButton.setFont( font );
        saveButton.setBounds( 0, BUTTON_HEIGHT + BORDERLINE, BUTTON_WIDTH, BUTTON_HEIGHT );
        saveButton.setToolTipText( "Save the mob data." );
        detailsPanel.add( saveButton );
        saveButton.addActionListener( this );

        deleteButton = new JButton( "Remove" );
        deleteButton.setFont( font );
        deleteButton.setBounds( 0, BUTTON_HEIGHT + BORDERLINE, BUTTON_WIDTH, BUTTON_HEIGHT );
        deleteButton.setToolTipText( "Remove this mob data from database." );
        detailsPanel.add( deleteButton );
        deleteButton.addActionListener( this );

        this.add(roomPanel);
        this.add(detailsPanel);
    }

    public void setMob(Mob mob) {
        this.mob = mob;
        this.nameArea.setText(this.mob.getName());
        this.expArea.setText(String.valueOf(this.mob.getAllExpAsString()));
        this.shortNameArea.setText(String.join(",", this.mob.getShortNames()));

        this.areaNameArea.setText(this.mob.getArea());
        this.alignmentArea.setText(this.mob.getAlignment());
        this.raceArea.setText(this.mob.getRace());
        this.repArea.setText(this.mob.getRep());
        this.notesArea.setText(this.mob.getNotes());
    }

    @Override
    public void componentHidden( ComponentEvent e ) {

    }

    @Override
    public void componentMoved( ComponentEvent e ) {

    }

    @Override
    public void componentResized( ComponentEvent e ) {
        this.scrollableNotes.setBounds( BORDERLINE, BORDERLINE, this.getWidth() - BORDERLINE * 2, this.getHeight() - BORDERLINE * 2 );
    }

    @Override
    public void componentShown( ComponentEvent e ) {

    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        if (e.getSource().equals( saveButton )) {
            this.mob.setNotes(this.notesArea.getText());

            String[] sNames = this.shortNameArea.getText().split("\\.");
            this.mob.setShortNames(sNames);

            engine.getMobStore().updateEditableFields(this.mob);
        } else if (e.getSource().equals( deleteButton )) {
            engine.getMobStore().remove(this.mob);
        }
    }

    @Override
    public void mobsDetected(ArrayList<Mob> roomMobs) {
        if (roomMobs.size() > 0) {
            StringBuilder names = new StringBuilder();
            for (Iterator<Mob> iter = roomMobs.iterator(); iter.hasNext();) {
                names.append(iter.next().getName() + "\n");
            }
            this.roomArea.setText(names.toString());
            this.setMob(roomMobs.get(0));
        } else {
            this.roomArea.setText("No mobs");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals( roomArea )) {
            int line;
            try {
                line = roomArea.getLineOfOffset( roomArea.getCaretPosition() );
                int start = roomArea.getLineStartOffset( line );
                int end = roomArea.getLineEndOffset( line );
                String text = roomArea.getDocument().getText(start, end - start);

                Mob m = this.engine.getMobStore().get(text.trim());
                this.setMob(m);
            } catch (BadLocationException e1) {
                //e1.printStackTrace();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

}
