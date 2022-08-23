package fi.altanar.batmob.gui;


import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.controller.SearchEngine;
import fi.altanar.batmob.controller.SearchEngine.SearchCriteria;
import fi.altanar.batmob.vo.Mob;

public class SearchPanel extends JPanel implements MouseInputListener, ComponentListener, ActionListener {

    public final Dimension MIN_LAYOUT_SIZE = new Dimension( 800, 550 );
    public final Dimension MIN_PANEL_SIZE = new Dimension( 780, 250 );
    private final int TEXT_INPUT_WIDTH = 150;
    private final int TEXT_INPUT_HEIGHT = 30;
    private final int PANEL_WIDTH = 780;

    public final Dimension INPUT_SIZE = new Dimension( 780, 50 );

    private final Color BORDER_COLOR = Color.LIGHT_GRAY;
    private final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private final Color BG_COLOR = Color.BLACK;

    private JPanel filterPanel = new JPanel();
    private JPanel resultPanel = new JPanel();

    private JTextField searchInput = new JTextField();

    private JList resultList = new JList();
    private JScrollPane scrollableResults;



    private Font font = new Font( "Consolas", Font.PLAIN, 14 );
    private Font labelFont = new Font( "Consolas", Font.PLAIN, 12 );

    MobEngine engine;
    SearchEngine searchEngine;

    public SearchPanel(MobEngine engine) {
        super();

        this.engine = engine;

        this.setPreferredSize( MIN_LAYOUT_SIZE );

        this.addComponentListener( this );
        this.setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );

        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.setPreferredSize( new Dimension( PANEL_WIDTH, TEXT_INPUT_HEIGHT ) );
        filterPanel.setBorder( new LineBorder( Color.RED ) );

        JLabel searchLabel = new JLabel("Search");
        searchLabel.setBackground( BG_COLOR );
        searchLabel.setForeground( TEXT_COLOR );
        searchLabel.setFont(labelFont);
        searchLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        filterPanel.add(searchLabel);

        //searchInput.setBounds( 0, 0, TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );
        searchInput.setEditable( true );
        searchInput.setColumns( 25 );
        searchInput.setBorder( new LineBorder( BORDER_COLOR ) );
        searchInput.setPreferredSize( new Dimension( TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT ) );
        searchInput.setBackground( BG_COLOR );
        searchInput.setForeground( TEXT_COLOR );
        searchInput.setFont( font );
        searchInput.setPreferredSize( INPUT_SIZE );
        searchInput.setToolTipText( "Name to search for" );
        searchInput.setAlignmentX( Component.LEFT_ALIGNMENT );
        searchInput.addActionListener(this);
        filterPanel.add(searchInput);

        resultPanel.setLayout(new BoxLayout(resultPanel,  BoxLayout.Y_AXIS));

        JLabel resultsLabel = new JLabel("Results");
        resultsLabel.setBackground( BG_COLOR );
        resultsLabel.setForeground( TEXT_COLOR );
        resultsLabel.setFont(labelFont);
        //searchLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        resultPanel.add(resultsLabel);

        resultList.setForeground( TEXT_COLOR );
        resultList.setBackground( BG_COLOR );
        resultList.setPreferredSize( new Dimension( PANEL_WIDTH, 750 ) );
        //notesArea.setMinimumSize( new Dimension( 370, 500 ) );
        //notesArea.setMaximumSize( new Dimension( 370, 100 ) );
        scrollableResults = new JScrollPane( resultList );
        scrollableResults.setAlignmentX( Component.LEFT_ALIGNMENT );
        scrollableResults.setBorder( new LineBorder( BORDER_COLOR ) );
        resultPanel.add( scrollableResults );

        this.add(filterPanel);
        this.add(resultPanel);
    }

    public void setSearchEngine(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(searchInput)) {
            JTextField source = (JTextField) e.getSource();
            String textFieldContent = source.getText();
            ArrayList<Mob> results = engine.getSearchEngine().search(textFieldContent, SearchCriteria.NAME, 0);

            this.resultList.removeAll();
            for (Mob m : results) {
                this.resultList.add(new JLabel(m.toString()));
            }
        }
    }

    public void setResults(String[] results)
    {
        this.resultList.removeAll();
        for (String s : results) {
            this.resultList.add(new JLabel(s));
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentShown(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

}
