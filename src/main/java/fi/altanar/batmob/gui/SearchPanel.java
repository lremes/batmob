package fi.altanar.batmob.gui;


import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.controller.SearchEngine;
import fi.altanar.batmob.controller.SearchEngine.SearchCriteria;
import fi.altanar.batmob.io.IMobListener;
import fi.altanar.batmob.vo.Mob;

public class SearchPanel extends JPanel implements 
    MouseInputListener, 
    ComponentListener, 
    ActionListener,
    ListSelectionListener {

    private final int LAYOUT_WIDTH = 800;
    private final int LAYOUT_HEIGHT = 550;
    public final Dimension LAYOUT_SIZE = new Dimension( LAYOUT_WIDTH, LAYOUT_HEIGHT );
    public final Dimension MIN_PANEL_SIZE = new Dimension( LAYOUT_WIDTH, LAYOUT_HEIGHT );
    private final int TEXT_INPUT_WIDTH = 150;
    private final int TEXT_INPUT_HEIGHT = 30;

    public final Dimension INPUT_SIZE = new Dimension( TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT );

    private final Color BORDER_COLOR = Color.LIGHT_GRAY;
    private final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private final Color BG_COLOR = Color.BLACK;

    private JPanel filterPanel = new JPanel();
    private JPanel resultPanel = new JPanel();

    private JTextField searchInput = new JTextField();

    private JList<Mob> resultList;
    private JScrollPane scrollableResults;

    private Font font = new Font( "Consolas", Font.PLAIN, 14 );
    private Font labelFont = new Font( "Consolas", Font.PLAIN, 12 );

    MobEngine engine;
    SearchEngine searchEngine;

    private ArrayList<IMobListener> listeners = new ArrayList<IMobListener>();

    //private SearchResults listModel = new SearchResults(null);
    private DefaultListModel<Mob> listModel = new DefaultListModel<Mob>();

    public SearchPanel(MobEngine engine) {
        super();

        this.engine = engine;

        this.setPreferredSize( LAYOUT_SIZE );
        this.setMinimumSize( LAYOUT_SIZE );
        this.setMaximumSize( LAYOUT_SIZE );

        this.addComponentListener( this );
        this.setLayout( new BoxLayout(this, BoxLayout.Y_AXIS) );
        this.setAlignmentX( Component.LEFT_ALIGNMENT );

        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.setPreferredSize( new Dimension( LAYOUT_WIDTH, TEXT_INPUT_HEIGHT) );
        filterPanel.setMinimumSize( new Dimension( LAYOUT_WIDTH, TEXT_INPUT_HEIGHT) );
        filterPanel.setMaximumSize( new Dimension( LAYOUT_WIDTH, TEXT_INPUT_HEIGHT) );
        filterPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
        filterPanel.setBackground( BG_COLOR );
        filterPanel.setForeground( TEXT_COLOR );

        resultPanel.setLayout(new BoxLayout(resultPanel,  BoxLayout.Y_AXIS));
        resultPanel.setPreferredSize( new Dimension( LAYOUT_WIDTH, LAYOUT_HEIGHT - TEXT_INPUT_HEIGHT) );
        resultPanel.setMinimumSize( new Dimension( LAYOUT_WIDTH, LAYOUT_HEIGHT - TEXT_INPUT_HEIGHT) );
        resultPanel.setMaximumSize( new Dimension( LAYOUT_WIDTH, LAYOUT_HEIGHT - TEXT_INPUT_HEIGHT) );
        resultPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
        resultPanel.setBackground( BG_COLOR );
        resultPanel.setForeground( TEXT_COLOR );

        JLabel searchLabel = new JLabel("Search");
        searchLabel.setBackground( BG_COLOR );
        searchLabel.setForeground( TEXT_COLOR );
        searchLabel.setPreferredSize( new Dimension( 60, TEXT_INPUT_HEIGHT) );
        searchLabel.setMinimumSize( new Dimension( 60, TEXT_INPUT_HEIGHT) );
        searchLabel.setMaximumSize( new Dimension( 60, TEXT_INPUT_HEIGHT) );
        searchLabel.setFont(labelFont);
        searchLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        filterPanel.add(searchLabel);

        searchInput.setEditable( true );
        searchInput.setColumns( 25 );
        searchInput.setBorder( new LineBorder( BORDER_COLOR ) );
        searchInput.setPreferredSize( new Dimension( LAYOUT_WIDTH - 60, TEXT_INPUT_HEIGHT) );
        searchInput.setMaximumSize( new Dimension( LAYOUT_WIDTH - 60, TEXT_INPUT_HEIGHT) );
        searchInput.setMinimumSize( new Dimension( LAYOUT_WIDTH - 60, TEXT_INPUT_HEIGHT) );
        searchInput.setBackground( BG_COLOR );
        searchInput.setForeground( TEXT_COLOR );
        searchInput.setFont( font );
        searchInput.setToolTipText( "Name to search for" );
        searchInput.setAlignmentX( Component.LEFT_ALIGNMENT );
        searchInput.addActionListener(this);
        filterPanel.add(searchInput);

        resultList = new JList<Mob>(this.listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        resultList.setLayoutOrientation(JList.VERTICAL);
        resultList.setVisibleRowCount(-1);
        resultList.setForeground( TEXT_COLOR );
        resultList.setBackground( BG_COLOR );
        resultList.setPreferredSize( new Dimension( LAYOUT_WIDTH, LAYOUT_HEIGHT - (TEXT_INPUT_HEIGHT * 1)) );
        resultList.setMaximumSize( new Dimension( LAYOUT_WIDTH, LAYOUT_HEIGHT - (TEXT_INPUT_HEIGHT * 1)) );
        resultList.setMinimumSize( new Dimension( LAYOUT_WIDTH, LAYOUT_HEIGHT - (TEXT_INPUT_HEIGHT * 1)) );
        resultList.addListSelectionListener(this);
        resultList.setBorder( new LineBorder( Color.GREEN ) );
        resultList.setAlignmentX( Component.LEFT_ALIGNMENT );
        scrollableResults = new JScrollPane( resultList );
        scrollableResults.setAlignmentX( Component.LEFT_ALIGNMENT );
        scrollableResults.setBorder( new LineBorder( BORDER_COLOR ) );
        resultList.setVisible( true );
        resultList.setVisibleRowCount(25);
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

            this.setResults(results);
        }
    }

    public void setResults(ArrayList<Mob> results)
    {
        this.listModel.clear();
        for (Mob m : results) {
            this.listModel.addElement(m);
        }
        //this.resultList.repaint();
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

    public void addMobListener(IMobListener l)
    {
        this.listeners.add(l);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        // search result clicked
        if (e.getValueIsAdjusting() == false) {
            if (resultList.getSelectedIndex() != -1) {
                Mob m = resultList.getSelectedValue();
                for (IMobListener ml : this.listeners) {
                    ml.mobSelected(m);
                }

            }
        }
    }
}
