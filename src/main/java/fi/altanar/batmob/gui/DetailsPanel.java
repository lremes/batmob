package fi.altanar.batmob.gui;

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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.io.MediaWikiApi;
import fi.altanar.batmob.io.IMobListener;
import fi.altanar.batmob.vo.Mob;

public class DetailsPanel extends JPanel implements ActionListener, ComponentListener, IMobListener {
    
    private final int PANEL_WIDTH = 390;
    private final int PANEL_HEIGHT = 540;

    private final int LABEL_HEIGHT = 20;

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
    private JScrollPane scrollableDesc;
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

        this.setLayout(new GridBagLayout());
        this.setBackground( BG_COLOR );
        this.setForeground( TEXT_COLOR );
        this.setAlignmentX( Component.LEFT_ALIGNMENT );

        nameArea = createPanel(this, 0, 0, 2, 1, "Long name", BORDER_COLOR, "This is the long name of the mob", false);

        expArea = createPanel(this, 1, 0, 2, 1, "Exp (latest/min/max)", BORDER_COLOR, "This is the exp gained for the mob", false);

        areaNameArea = createPanel(this, 3, 0, 1, 1, "Area", BORDER_COLOR, "This is the area where the mob is", false);

        shortNameArea = createPanel(this, 3, 1, 1, 1, "Names", BORDER_COLOR, "Altenative names for mob", true);

        raceArea = createPanel(this, 4, 0, 1, 1, "Race", BORDER_COLOR, "Race of the mob", true);

        genderArea = createPanel(this, 4, 1, 1, 1, "Gender", BORDER_COLOR, "Gender of the mob", true);

        spellsArea = createPanel(this, 5, 0, 2, 1, "Spells", BORDER_COLOR, "Comma-separted list of spells", true);

        skillsArea = createPanel(this, 6, 0, 2, 1, "Skills", BORDER_COLOR, "Comma-separted list of skill", true);

        alignmentArea = createPanel(this, 7, 0, 1, 1, "Alignment", BORDER_COLOR, "Alignment of the mob", true);

        repArea = createPanel(this, 7, 1, 1, 1, "Rep", BORDER_COLOR, "Rep gained for killing the mob", true);

        isUndead.setBackground( BG_COLOR );
        isUndead.setForeground( TEXT_COLOR );
        isUndead.setAlignmentX( Component.LEFT_ALIGNMENT );
        isUndead.setPreferredSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isUndead.setMinimumSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isUndead.setMaximumSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isUndead.setAlignmentX( Component.LEFT_ALIGNMENT );
        isUndead.setFont( font );

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridy = 8;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(isUndead, c);

        isAggro.setBackground( BG_COLOR );
        isAggro.setForeground( TEXT_COLOR );
        isAggro.setAlignmentX( Component.LEFT_ALIGNMENT );
        isAggro.setPreferredSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isAggro.setMinimumSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isAggro.setMaximumSize( new Dimension( PANEL_WIDTH, CHECKBOX_HEIGHT ) );
        isAggro.setAlignmentX( Component.LEFT_ALIGNMENT );
        isAggro.setFont( font );

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridy = 8;
        c.gridx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(isAggro, c);

        JPanel descPanel = new JPanel();
        descPanel.setBackground( BG_COLOR );
        descPanel.setForeground( TEXT_COLOR );
        descPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));

        JLabel descLabel = createLabel("Description");
        
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
        scrollableDesc = new JScrollPane( descArea );
        scrollableDesc.setPreferredSize( new Dimension( PANEL_WIDTH, TEXT_INPUT_HEIGHT * 2) );
        scrollableDesc.setMinimumSize( new Dimension( PANEL_WIDTH, TEXT_INPUT_HEIGHT * 2 ) );
        scrollableDesc.setMaximumSize( new Dimension( PANEL_WIDTH, TEXT_INPUT_HEIGHT * 2 ) );
        descPanel.add(descLabel);
        descPanel.add(scrollableDesc);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.gridy = 9;
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        this.add( descPanel, c );

        JPanel notesPanel = new JPanel();
        notesPanel.setBackground( BG_COLOR );
        notesPanel.setForeground( TEXT_COLOR );
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));
        notesPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
        JLabel notesLabel = createLabel("Notes");
        notesPanel.add(notesLabel);

        notesArea.setWrapStyleWord( true );
        notesArea.setEditable( true );
        notesArea.setColumns( 50 );
        notesArea.setForeground( TEXT_COLOR );
        notesArea.setBackground( BG_COLOR_EDITABLE );
        notesArea.setLineWrap( true );
        notesArea.setAlignmentX( Component.LEFT_ALIGNMENT );
        notesArea.setBorder( new LineBorder( BORDER_EDITABLE_COLOR ) );
        scrollableNotes = new JScrollPane( notesArea );
        scrollableNotes.setPreferredSize( new Dimension( PANEL_WIDTH, TEXT_INPUT_HEIGHT * 2) );
        scrollableNotes.setMinimumSize( new Dimension( PANEL_WIDTH, TEXT_INPUT_HEIGHT * 2 ) );
        scrollableNotes.setMaximumSize( new Dimension( PANEL_WIDTH, TEXT_INPUT_HEIGHT * 2 ) );
        
        notesPanel.add(notesLabel);
        notesPanel.add( scrollableNotes );

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridy = 10;
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        this.add(notesPanel, c);

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

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 11;
        c.gridwidth = 2;
        c.gridheight = 1;
        this.add(buttonPanel, c);
    }

    public void setQueryEngine(MediaWikiApi queryEngine) {
        this.queryEngine = queryEngine;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBackground( BG_COLOR );
        label.setForeground( TEXT_COLOR );
        label.setBorder(new EmptyBorder(5,3,3,3));
        label.setFont(labelFont);
        //label.setPreferredSize( LABEL_SIZE );
        //label.setMinimumSize( LABEL_SIZE );
        //label.setMaximumSize( LABEL_SIZE );
        label.setAlignmentX( Component.LEFT_ALIGNMENT );
        return label;
    }

    private JTextField createInput(Color borderColor, String tooltip, boolean editable) {
        JTextField field = new JTextField();
        if (editable) {
            field.setBorder( new LineBorder( BORDER_EDITABLE_COLOR ) );
            field.setBackground( BG_COLOR_EDITABLE );
        } else {
            field.setBorder( new LineBorder( BORDER_COLOR ) );
            field.setBackground( BG_COLOR_NONEDITABLE );
        }
        field.setForeground( TEXT_COLOR );
        field.setFont( font );
        field.setToolTipText( tooltip );
        field.setAlignmentX( Component.LEFT_ALIGNMENT );
        //field.setPreferredSize( INPUT_SIZE );
        //field.setMinimumSize( INPUT_SIZE );
        //field.setMaximumSize( INPUT_SIZE );

        return field;
    }

    private JTextField createPanel(JPanel container, int gridY, int gridX, int w, int h, String labelText, Color borderColor, String tooltip, boolean editable) {
        JPanel subContainer = new JPanel();
        subContainer.setLayout(new BoxLayout(subContainer, BoxLayout.Y_AXIS));
        JLabel label = createLabel(labelText);
        JTextField field = createInput(borderColor, tooltip, editable);
        subContainer.setBackground( BG_COLOR );
        subContainer.setForeground( TEXT_COLOR );
        subContainer.add(label);
        subContainer.add(field);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.0;
        c.gridx = gridX;
        c.gridy = gridY;
        c.gridwidth = w;
        c.gridheight = h;
        c.ipadx = 5;
        c.ipady = 5;
        container.add(subContainer, c);
        return field;
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
                this.mob.setSpells(list);
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
                } else {
                    this.engine.notifyStatusListeners("Nothing found");
                }
            }
        }
    }

    @Override
    public void mobSelected(Mob mob) {
        this.mob = mob;
        clearAll();
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
                this.spellsArea.setText(String.join(",", spells));
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
        this.spellsArea.setText("");

        this.raceArea.setText("");
        this.genderArea.setText("");
        this.repArea.setText("");
        this.notesArea.setText("");
        this.isUndead.setSelected(false);
        this.isAggro.setSelected(false);
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
