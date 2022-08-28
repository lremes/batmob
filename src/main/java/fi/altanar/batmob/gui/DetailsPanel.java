package fi.altanar.batmob.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.border.LineBorder;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.io.MediaWikiApi;
import fi.altanar.batmob.io.IMobListener;
import fi.altanar.batmob.vo.Mob;

public class DetailsPanel extends JPanel implements ActionListener, ComponentListener, IMobListener {
    
    private final int PANEL_WIDTH = 390;
    private final int PANEL_HEIGHT = 540;

    private final int LABEL_HEIGHT = 18;

    public final Dimension PANEL_SIZE = new Dimension( PANEL_WIDTH, PANEL_HEIGHT );
    public final Dimension LABEL_SIZE = new Dimension( PANEL_WIDTH, LABEL_HEIGHT );

    private final int TEXT_INPUT_WIDTH = 380;
    private final int DESC_HEIGHT = 270;
    protected int BORDERLINE = 7;
    private final int BUTTON_HEIGHT = 25;
    private final int BUTTON_WIDTH = 100;
    private final int TEXT_INPUT_HEIGHT = 18;
    private final int CHECKBOX_HEIGHT = 20;
    public final Dimension INPUT_SIZE = new Dimension( PANEL_WIDTH, TEXT_INPUT_HEIGHT );

    private final Color BORDER_COLOR = Color.LIGHT_GRAY;
    private final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private final Color BG_COLOR = Color.BLACK;
    private final Color BG_COLOR_NONEDITABLE = Color.DARK_GRAY;
    private final Color BORDER_EDITABLE_COLOR = Color.LIGHT_GRAY;
    private final Color BG_COLOR_EDITABLE = Color.BLACK;

    private Font font = new Font( "Consolas", Font.PLAIN, 12 );
    private Font labelFont = new Font( "Consolas", Font.PLAIN, 11 );

    private JTextArea notesArea = new JTextArea();
    private JScrollPane scrollableNotes;
    private JTextField nameArea = new JTextField();
    private JTextArea descArea = new JTextArea();
    private JTextField areaNameArea = new JTextField();
    private JTextField expArea = new JTextField();
    private JTextField shortNameArea = new JTextField();
    private JTextField raceArea = new JTextField();
    private JTextField alignmentArea = new JTextField();
    private JTextField repArea = new JTextField();
    private JTextField genderArea = new JTextField();
    private JTextField skillsArea = new JTextField();
    private JTextField spellsArea = new JTextField();

    private JCheckBox isUndead = new JCheckBox("Undead");
    private JCheckBox isAggro = new JCheckBox("Aggro");

    private JButton saveButton;
    private JButton deleteButton;
    private JButton wikiButton;

    private MediaWikiApi queryEngine;

    MobEngine engine;

    private Mob mob;
    
    public DetailsPanel(MobEngine engine) {
        super();

        this.engine = engine;

        this.queryEngine = engine.getQueryEngine();
        
        this.setPreferredSize( PANEL_SIZE );
        this.setMinimumSize( PANEL_SIZE );
        this.setMaximumSize( PANEL_SIZE );

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground( BG_COLOR );
        this.setForeground( TEXT_COLOR );
        this.setAlignmentX( Component.LEFT_ALIGNMENT );

        JLabel nameLabel = new JLabel("Long name");
        nameLabel.setBackground( BG_COLOR );
        nameLabel.setForeground( TEXT_COLOR );
        nameLabel.setFont(labelFont);
        nameLabel.setPreferredSize( LABEL_SIZE );
        nameLabel.setMinimumSize( LABEL_SIZE );
        nameLabel.setMaximumSize( LABEL_SIZE );
        nameLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( nameLabel );

        nameArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        nameArea.setEditable( false );
        nameArea.setColumns( 25 );
        nameArea.setBorder( new LineBorder( BORDER_COLOR ) );
        nameArea.setBackground( BG_COLOR_NONEDITABLE );
        nameArea.setForeground( TEXT_COLOR );
        nameArea.setFont( font );
        nameArea.setPreferredSize( INPUT_SIZE );
        nameArea.setMinimumSize( INPUT_SIZE );
        nameArea.setMaximumSize( INPUT_SIZE );
        nameArea.setToolTipText( "This is the long name of the mob" );
        nameArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( nameArea );

        JLabel expLabel = new JLabel("Exp (latest / min / max)");
        expLabel.setBackground( BG_COLOR );
        expLabel.setForeground( TEXT_COLOR );
        expLabel.setFont(labelFont);
        expLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( expLabel );

        expArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        expArea.setEditable( false );
        expArea.setColumns( 25 );
        expArea.setBorder( new LineBorder( BORDER_COLOR ) );
        expArea.setBackground( BG_COLOR_NONEDITABLE );
        expArea.setForeground( TEXT_COLOR );
        expArea.setFont( font );
        expArea.setToolTipText( "This is the latest/min/max recorded exp for the mob" );
        expArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        expArea.setPreferredSize( INPUT_SIZE );
        expArea.setMinimumSize( INPUT_SIZE );
        expArea.setMaximumSize( INPUT_SIZE );

        this.add( expArea );

        JLabel descLabel = new JLabel("Description");
        descLabel.setBackground( BG_COLOR );
        descLabel.setForeground( TEXT_COLOR );
        descLabel.setFont(labelFont);
        descLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( descLabel );

        descArea.setBounds( 0, BORDERLINE, TEXT_INPUT_WIDTH, DESC_HEIGHT );
        descArea.setEditable( false );
        descArea.setColumns( 25 );
        descArea.setBorder( new LineBorder( BORDER_COLOR ) );
        descArea.setAlignmentY( Component.BOTTOM_ALIGNMENT );
        descArea.setLineWrap( true );
        descArea.setBackground( BG_COLOR_NONEDITABLE );
        descArea.setForeground( TEXT_COLOR );
        descArea.setFont( font );
        descArea.setToolTipText( "This is mob description" );
        descArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        descArea.setPreferredSize( INPUT_SIZE );
        descArea.setMinimumSize( INPUT_SIZE );
        descArea.setMaximumSize( INPUT_SIZE );

        this.add( descArea );

        JLabel areaLabel = new JLabel("Area");
        areaLabel.setBackground( BG_COLOR );
        areaLabel.setForeground( TEXT_COLOR );
        areaLabel.setFont(labelFont);
        areaLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( areaLabel );

        areaNameArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        areaNameArea.setEditable( false );
        areaNameArea.setColumns( 25 );
        areaNameArea.setBorder( new LineBorder( BORDER_COLOR ) );
        areaNameArea.setBackground( BG_COLOR_NONEDITABLE );
        areaNameArea.setForeground( TEXT_COLOR );
        areaNameArea.setFont( font );
        areaNameArea.setToolTipText( "This is the area where the mob can be found" );
        areaNameArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        areaNameArea.setPreferredSize( INPUT_SIZE );
        areaNameArea.setMinimumSize( INPUT_SIZE );
        areaNameArea.setMaximumSize( INPUT_SIZE );

        this.add( areaNameArea );

        JLabel snameLabel = new JLabel("Short names");
        snameLabel.setBackground( BG_COLOR );
        snameLabel.setForeground( TEXT_COLOR );
        snameLabel.setFont(labelFont);
        snameLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( snameLabel );

        shortNameArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        shortNameArea.setEditable( true );
        shortNameArea.setColumns( 25 );
        shortNameArea.setBorder( new LineBorder( BORDER_EDITABLE_COLOR ) );
        shortNameArea.setBackground( BG_COLOR_EDITABLE );
        shortNameArea.setForeground( TEXT_COLOR );
        shortNameArea.setFont( font );
        shortNameArea.setToolTipText( "This is a comma-separated list of short names for the mob." );
        shortNameArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        shortNameArea.setPreferredSize( INPUT_SIZE );
        shortNameArea.setMinimumSize( INPUT_SIZE );
        shortNameArea.setMaximumSize( INPUT_SIZE );

        this.add( shortNameArea );

        JLabel raceLabel = new JLabel("Race");
        raceLabel.setBackground( BG_COLOR );
        raceLabel.setForeground( TEXT_COLOR );
        raceLabel.setFont(labelFont);
        raceLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( raceLabel );

        raceArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        raceArea.setEditable( true );
        raceArea.setColumns( 25 );
        raceArea.setBorder( new LineBorder( BORDER_EDITABLE_COLOR ) );
        raceArea.setBackground( BG_COLOR_EDITABLE );
        raceArea.setForeground( TEXT_COLOR );
        raceArea.setFont( font );
        raceArea.setToolTipText( "This is the race of the mob." );
        raceArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        raceArea.setPreferredSize( INPUT_SIZE );
        raceArea.setMinimumSize( INPUT_SIZE );
        raceArea.setMaximumSize( INPUT_SIZE );
        this.add( raceArea );

        JLabel spellsLabel = new JLabel("Spells");
        spellsLabel.setBackground( BG_COLOR );
        spellsLabel.setForeground( TEXT_COLOR );
        spellsLabel.setFont(labelFont);
        spellsLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( spellsLabel );

        spellsArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        spellsArea.setEditable( true );
        spellsArea.setColumns( 25 );
        spellsArea.setBorder( new LineBorder( BORDER_EDITABLE_COLOR ) );
        spellsArea.setBackground( BG_COLOR_EDITABLE );
        spellsArea.setForeground( TEXT_COLOR );
        spellsArea.setFont( font );
        spellsArea.setToolTipText( "Comma-separted list of spells" );
        spellsArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        spellsArea.setPreferredSize( INPUT_SIZE );
        spellsArea.setMinimumSize( INPUT_SIZE );
        spellsArea.setMaximumSize( INPUT_SIZE );
        this.add( spellsArea );

        JLabel skillsLabel = new JLabel("Skills");
        skillsLabel.setBackground( BG_COLOR );
        skillsLabel.setForeground( TEXT_COLOR );
        skillsLabel.setFont(labelFont);
        skillsLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( skillsLabel );

        skillsArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        skillsArea.setEditable( true );
        skillsArea.setColumns( 25 );
        skillsArea.setBorder( new LineBorder( BORDER_EDITABLE_COLOR ) );
        skillsArea.setBackground( BG_COLOR_EDITABLE );
        skillsArea.setForeground( TEXT_COLOR );
        skillsArea.setFont( font );
        skillsArea.setToolTipText( "Comma-separted list of spells" );
        skillsArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        skillsArea.setPreferredSize( INPUT_SIZE );
        skillsArea.setMinimumSize( INPUT_SIZE );
        skillsArea.setMaximumSize( INPUT_SIZE );
        this.add( skillsArea );

        JLabel alignmentLabel = new JLabel("Alignment");
        alignmentLabel.setBackground( BG_COLOR );
        alignmentLabel.setForeground( TEXT_COLOR );
        alignmentLabel.setFont(labelFont);
        alignmentLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( alignmentLabel );

        alignmentArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        alignmentArea.setEditable( true );
        alignmentArea.setColumns( 25 );
        alignmentArea.setBorder( new LineBorder( BORDER_EDITABLE_COLOR ) );
        alignmentArea.setBackground( BG_COLOR_EDITABLE );
        alignmentArea.setForeground( TEXT_COLOR );
        alignmentArea.setFont( font );
        alignmentArea.setToolTipText( "This is the alignment of the mob." );
        alignmentArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        alignmentArea.setPreferredSize( INPUT_SIZE );
        alignmentArea.setMinimumSize( INPUT_SIZE );
        alignmentArea.setMaximumSize( INPUT_SIZE );
        this.add( alignmentArea );

        JLabel repLabel = new JLabel("Rep");
        repLabel.setBackground( BG_COLOR );
        repLabel.setForeground( TEXT_COLOR );
        repLabel.setFont(labelFont);
        repLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( repLabel );

        repArea.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        repArea.setEditable( true );
        repArea.setColumns( 25 );
        repArea.setBorder( new LineBorder( BORDER_EDITABLE_COLOR ) );
        repArea.setBackground( BG_COLOR_EDITABLE );
        repArea.setForeground( TEXT_COLOR );
        repArea.setFont( font );
        repArea.setToolTipText( "This is the rep gained for the mob." );
        repArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        repArea.setPreferredSize( INPUT_SIZE );
        repArea.setMinimumSize( INPUT_SIZE );
        repArea.setMaximumSize( INPUT_SIZE );
        this.add( repArea );

        isUndead.setBackground( BG_COLOR );
        isUndead.setForeground( TEXT_COLOR );
        isUndead.setAlignmentX( Component.LEFT_ALIGNMENT );
        isUndead.setPreferredSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isUndead.setMinimumSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isUndead.setMaximumSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isUndead.setAlignmentX( Component.LEFT_ALIGNMENT );
        isUndead.setFont( font );
        this.add(isUndead);

        isAggro.setBackground( BG_COLOR );
        isAggro.setForeground( TEXT_COLOR );
        isAggro.setAlignmentX( Component.LEFT_ALIGNMENT );
        isAggro.setPreferredSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isAggro.setMinimumSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isAggro.setMaximumSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isAggro.setAlignmentX( Component.LEFT_ALIGNMENT );
        isAggro.setFont( font );
        this.add(isAggro);

        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setBackground( BG_COLOR );
        genderLabel.setForeground( TEXT_COLOR );
        genderLabel.setFont(labelFont);
        this.add( genderLabel );

        genderArea.setBorder( new LineBorder( BORDER_EDITABLE_COLOR ) );
        genderArea.setBackground( BG_COLOR_EDITABLE );
        genderArea.setForeground( TEXT_COLOR );
        genderArea.setFont( font );
        genderArea.setToolTipText( "This is the rep gained for the mob." );
        genderArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        genderArea.setPreferredSize( INPUT_SIZE );
        genderArea.setMinimumSize( INPUT_SIZE );
        genderArea.setMaximumSize( INPUT_SIZE );
        this.add(genderArea);

        JLabel notesLabel = new JLabel("Notes");
        notesLabel.setBackground( BG_COLOR_EDITABLE );
        notesLabel.setForeground( TEXT_COLOR );
        notesLabel.setFont(labelFont);
        notesLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        this.add( notesLabel );

        notesArea.setWrapStyleWord( true );
        notesArea.setEditable( true );
        notesArea.setColumns( 50 );
        notesArea.setForeground( TEXT_COLOR );
        notesArea.setBackground( BG_COLOR_EDITABLE );
        notesArea.setLineWrap( true );
        //notesArea.setPreferredSize( new Dimension( 370, 100 ) );
        //notesArea.setMinimumSize( new Dimension( 370, 500 ) );
        //notesArea.setMaximumSize( new Dimension( 370, 100 ) );
        scrollableNotes = new JScrollPane( notesArea );
        scrollableNotes.setAlignmentX( Component.LEFT_ALIGNMENT );
        scrollableNotes.setBorder( new LineBorder( BORDER_EDITABLE_COLOR ) );
        this.add( scrollableNotes );

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.X_AXIS) );
        buttonPanel.setAlignmentX( Component.LEFT_ALIGNMENT );

        saveButton = new JButton( "Save" );
        saveButton.setFont( font );
        saveButton.setBounds( 0, BUTTON_HEIGHT + BORDERLINE, BUTTON_WIDTH, BUTTON_HEIGHT );
        saveButton.setToolTipText( "Save the mob data." );
        saveButton.setAlignmentX( Component.LEFT_ALIGNMENT );
        buttonPanel.add( saveButton );
        saveButton.addActionListener( this );

        deleteButton = new JButton( "Remove" );
        deleteButton.setFont( font );
        deleteButton.setBounds( 0, BUTTON_HEIGHT + BORDERLINE, BUTTON_WIDTH, BUTTON_HEIGHT );
        deleteButton.setToolTipText( "Remove this mob data from database." );
        buttonPanel.add( deleteButton );
        deleteButton.addActionListener( this );

        wikiButton = new JButton( "Wiki" );
        wikiButton.setFont( font );
        wikiButton.setBounds( 0, BUTTON_HEIGHT + BORDERLINE, BUTTON_WIDTH, BUTTON_HEIGHT );
        wikiButton.setToolTipText( "Update data from wiki." );
        buttonPanel.add( wikiButton );
        wikiButton.addActionListener( this );

        this.add(buttonPanel);
    }

    public void setQueryEngine(MediaWikiApi queryEngine) {
        this.queryEngine = queryEngine;
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        if (e.getSource().equals( saveButton )) {
            this.mob.setNotes(this.notesArea.getText());

            ArrayList<String> sNames = new ArrayList<String>(Arrays.asList(this.shortNameArea.getText().split("\\.")));
            this.mob.setShortNames(sNames);
            this.mob.setRace(this.raceArea.getText());
            this.mob.setAlignment(this.alignmentArea.getText());
            this.mob.setGender(this.genderArea.getText());
            this.mob.setRep(this.repArea.getText());

            String skillz = this.skillsArea.getText();
            if (!skillz.isEmpty()) {
                String[] sArray = skillz.split(",");
                ArrayList<String> list = new ArrayList<String>();
                for (String s : sArray) {
                    String trimmed = s.trim();
                    if (!trimmed.isEmpty()) {
                        list.add(trimmed);
                    }
                }
                this.mob.setSkills(list);
            }

            String spellz = this.spellsArea.getText();
            if (!spellz.isEmpty()) {
                String[] sArray = spellz.split(",");
                ArrayList<String> list = new ArrayList<String>();
                for (String s : sArray) {
                    String trimmed = s.trim();
                    if (!trimmed.isEmpty()) {
                        list.add(trimmed);
                    }
                }
                this.mob.setSkills(list);
            }

            this.mob.setUndead(this.isUndead.isSelected());
            this.mob.setAggro(this.isAggro.isSelected());

            engine.getMobStore().updateEditableFields(this.mob);
        } else if (e.getSource().equals( deleteButton )) {            
            engine.getMobStore().remove(this.mob);
            this.mob = null;
            this.clearAll();
        } else if (e.getSource().equals( wikiButton )) {
            if (this.queryEngine != null) {
                Mob m = this.queryEngine.fetchMobInfo(this.mob.getName());
                if (m != null) {
                    this.mob.updateFrom(m);
                    showMob(this.mob);
                }
            }
        }
    }

    @Override
    public void mobSelected(Mob mob) {
        this.mob = mob;
        showMob(this.mob);
    }

    private void showMob(Mob m) {
        try {
            this.nameArea.setText(this.mob.getName());
            this.expArea.setText(String.valueOf(this.mob.getAllExpAsString()));
            if (this.mob.getShortNames() != null) {
                this.shortNameArea.setText(String.join(",", this.mob.getShortNames()));
            }
    
            this.areaNameArea.setText(this.mob.getArea());
            this.alignmentArea.setText(this.mob.getAlignment());
    
            ArrayList<String> list = m.getSkills();
            if (list != null) {
                String[] skills = list.toArray(new String[0]);
                this.skillsArea.setText(String.join(",", skills));
            }
    
            list = m.getSpells();
            if (list != null) {
                String[] spells = list.toArray(new String[0]);
                this.skillsArea.setText(String.join(",", spells));
            }
    
            this.raceArea.setText(this.mob.getRace());
            this.repArea.setText(this.mob.getRep());
            this.notesArea.setText(this.mob.getNotes());
            this.isUndead.setSelected(this.mob.isUndead());
            this.isAggro.setSelected(this.mob.isAggro());
        } catch (Exception e) {
            this.engine.log(e.getMessage());
        }
    }

    private void clearAll() {
        this.nameArea.setText("");
        this.expArea.setText("");
        this.shortNameArea.setText("");
        this.areaNameArea.setText("");
        this.alignmentArea.setText("");

        this.skillsArea.setText("");
        this.skillsArea.setText("");

        this.raceArea.setText("");
        this.repArea.setText("");
        this.notesArea.setText("");
        this.isUndead.setSelected(false);
        this.isAggro.setSelected(false);;
    }

    @Override
    public void mobsDetected(ArrayList<Mob> mob) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void componentHidden( ComponentEvent e ) {

    }

    @Override
    public void componentMoved( ComponentEvent e ) {

    }

    @Override
    public void componentResized( ComponentEvent e ) {

    }

    @Override
    public void componentShown( ComponentEvent e ) {

    }
}
