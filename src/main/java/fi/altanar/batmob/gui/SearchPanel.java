package fi.altanar.batmob.gui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JComboBox;
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
import fi.altanar.batmob.io.IMobListener;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobFilter;

public class SearchPanel extends JPanel implements
        MouseInputListener,
        ComponentListener,
        ActionListener,
        KeyListener,
        ListSelectionListener {

    private final int LAYOUT_WIDTH = 800;
    private final int LAYOUT_HEIGHT = 550;
    public final Dimension LAYOUT_SIZE = new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT);
    public final Dimension MIN_PANEL_SIZE = new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT);
    private final int TEXT_INPUT_WIDTH = 150;
    private final int TEXT_INPUT_HEIGHT = 30;

    public final Dimension INPUT_SIZE = new Dimension(TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT);

    private final Color BORDER_COLOR = Color.LIGHT_GRAY;
    private final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private final Color BG_COLOR = Color.BLACK;

    private JPanel filterPanel = new JPanel();
    private JPanel resultPanel = new JPanel();

    private JTextField searchInput = new JTextField();
    private JTextField expInput = new JTextField();
    private JComboBox<String> areaSelect = new JComboBox<String>();
    private JList<Mob> resultList;
    private JScrollPane scrollableResults;
    private JButton searchButton = new JButton("Search");

    private JCheckBox isZinium = new JCheckBox("Zin");

    private Font font = new Font("Consolas", Font.PLAIN, 14);
    private Font labelFont = new Font("Consolas", Font.PLAIN, 12);

    private TreeSet<String> areas = new TreeSet<String>();

    SearchEngine searchEngine;

    private ArrayList<IMobListener> listeners = new ArrayList<IMobListener>();

    // private SearchResults listModel = new SearchResults(null);
    private DefaultListModel<Mob> listModel = new DefaultListModel<Mob>();

    public SearchPanel(SearchEngine searchEngine) {
        super();

        this.searchEngine = searchEngine;

        this.setPreferredSize(LAYOUT_SIZE);
        this.setMinimumSize(LAYOUT_SIZE);
        this.setMaximumSize(LAYOUT_SIZE);

        this.addComponentListener(this);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);

        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.setPreferredSize(new Dimension(LAYOUT_WIDTH, TEXT_INPUT_HEIGHT));
        filterPanel.setMinimumSize(new Dimension(LAYOUT_WIDTH, TEXT_INPUT_HEIGHT));
        filterPanel.setMaximumSize(new Dimension(LAYOUT_WIDTH, TEXT_INPUT_HEIGHT));
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.setBackground(BG_COLOR);
        filterPanel.setForeground(TEXT_COLOR);

        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setPreferredSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT - TEXT_INPUT_HEIGHT));
        resultPanel.setMinimumSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT - TEXT_INPUT_HEIGHT));
        resultPanel.setMaximumSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT - TEXT_INPUT_HEIGHT));
        resultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultPanel.setBackground(BG_COLOR);
        resultPanel.setForeground(TEXT_COLOR);

        JLabel searchLabel = new JLabel("Name");
        searchLabel.setBackground(BG_COLOR);
        searchLabel.setForeground(TEXT_COLOR);
        searchLabel.setPreferredSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        searchLabel.setMinimumSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        searchLabel.setMaximumSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        searchLabel.setFont(labelFont);
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(searchLabel);

        searchInput.setEditable(true);
        searchInput.setColumns(25);
        searchInput.setBorder(new LineBorder(BORDER_COLOR));
        searchInput.setPreferredSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        searchInput.setMaximumSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        searchInput.setMinimumSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        searchInput.setBackground(BG_COLOR);
        searchInput.setForeground(TEXT_COLOR);
        searchInput.setFont(font);
        searchInput.setToolTipText("Name to search for");
        searchInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchInput.addActionListener(this);
        searchInput.addKeyListener(this);
        filterPanel.add(searchInput);

        JLabel expLabel = new JLabel("Min Exp");
        expLabel.setBackground(BG_COLOR);
        expLabel.setForeground(TEXT_COLOR);
        expLabel.setPreferredSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        expLabel.setMinimumSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        expLabel.setMaximumSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        expLabel.setFont(labelFont);
        expLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(expLabel);

        expInput.setEditable(true);
        expInput.setColumns(7);
        expInput.setBorder(new LineBorder(BORDER_COLOR));
        expInput.setPreferredSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        expInput.setMaximumSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        expInput.setMinimumSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        expInput.setBackground(BG_COLOR);
        expInput.setForeground(TEXT_COLOR);
        expInput.setFont(font);
        expInput.setToolTipText("Min exp");
        expInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        expInput.addActionListener(this);
        expInput.addKeyListener(this);
        filterPanel.add(expInput);

        JLabel areaLabel = new JLabel("Area");
        areaLabel.setBackground(BG_COLOR);
        areaLabel.setForeground(TEXT_COLOR);
        areaLabel.setPreferredSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        areaLabel.setMinimumSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        areaLabel.setMaximumSize(new Dimension(60, TEXT_INPUT_HEIGHT));
        areaLabel.setFont(labelFont);
        areaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterPanel.add(areaLabel);

        areaSelect.setEditable(true);
        areaSelect.setBorder(new LineBorder(BORDER_COLOR));
        // areaSelect.setPreferredSize( new Dimension( 60, TEXT_INPUT_HEIGHT) );
        // areaSelect.setMaximumSize( new Dimension( LAYOUT_WIDTH - 60,
        // TEXT_INPUT_HEIGHT) );
        // areaSelect.setMinimumSize( new Dimension( LAYOUT_WIDTH - 60,
        // TEXT_INPUT_HEIGHT) );
        areaSelect.setBackground(BG_COLOR);
        areaSelect.setForeground(TEXT_COLOR);
        areaSelect.setFont(font);
        areaSelect.setToolTipText("Area to search for, type to filter");
        areaSelect.setAlignmentX(Component.LEFT_ALIGNMENT);
        // key events go to the editor of an editable combo box, not to the box itself
        areaSelect.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        areaSelect.hidePopup();
                        doSearch();
                        break;
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_ESCAPE:
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_SHIFT:
                        break;
                    default:
                        filterAreas();
                }
            }
        });
        // areas of mobs detected since the window was shown
        areaSelect.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                populateSelect();
            }
        });
        filterPanel.add(areaSelect);

        isZinium.setBackground(BG_COLOR);
        isZinium.setForeground(TEXT_COLOR);
        filterPanel.add(isZinium);

        searchButton.addActionListener(this);
        filterPanel.add(searchButton);

        resultList = new JList<Mob>(this.listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        resultList.setLayoutOrientation(JList.VERTICAL);
        resultList.setVisibleRowCount(-1);
        resultList.setForeground(TEXT_COLOR);
        resultList.setBackground(BG_COLOR);
        resultList.setPreferredSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT - (TEXT_INPUT_HEIGHT * 1)));
        resultList.setMaximumSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT - (TEXT_INPUT_HEIGHT * 1)));
        resultList.setMinimumSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT - (TEXT_INPUT_HEIGHT * 1)));
        resultList.addListSelectionListener(this);
        resultList.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollableResults = new JScrollPane(resultList);
        scrollableResults.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollableResults.setBorder(new LineBorder(BORDER_COLOR));
        resultList.setVisible(true);
        resultList.setVisibleRowCount(25);
        resultPanel.add(scrollableResults);

        this.add(filterPanel);
        this.add(resultPanel);

        populateSelect();
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
        if (e.getSource() == this.searchButton) {
            this.doSearch();
        }
    }

    public void setResults(ArrayList<Mob> results) {
        this.listModel.clear();
        for (Mob m : results) {
            this.listModel.addElement(m);
        }
        // this.resultList.repaint();
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
    public void componentShown(ComponentEvent ev) {
        populateSelect();
    }

    void populateSelect() {
        Iterator<Entry<String, Mob>> iter = this.searchEngine.getMobStore().iterator();
        this.areas.clear();
        while (iter.hasNext()) {
            Entry<String, Mob> e = iter.next();
            if (e.getValue().getArea() != null) {
                this.areas.add(e.getValue().getArea());
            }
        }

        String text = this.getAreaText();
        this.setAreaItems(matchingAreas(this.areas, ""));
        this.areaSelect.getEditor().setItem(text);
    }

    /**
     * Narrow the area list to the areas containing the typed text.
     */
    void filterAreas() {
        String text = this.getAreaText();
        ArrayList<String> matching = matchingAreas(this.areas, text);

        this.setAreaItems(matching);
        // replacing the items resets the editor, put the typed text back
        this.areaSelect.getEditor().setItem(text);

        if (this.areaSelect.isShowing()) {
            this.areaSelect.hidePopup();
            if (!matching.isEmpty()) {
                this.areaSelect.showPopup();
            }
        }
    }

    static ArrayList<String> matchingAreas(Collection<String> areas, String text) {
        String needle = text.trim().toLowerCase();
        ArrayList<String> matching = new ArrayList<String>();
        for (String area : areas) {
            if (area.toLowerCase().contains(needle)) {
                matching.add(area);
            }
        }
        return matching;
    }

    private void setAreaItems(ArrayList<String> items) {
        this.areaSelect.setModel(new DefaultComboBoxModel<String>(items.toArray(new String[0])));
        this.areaSelect.setSelectedIndex(-1);
    }

    String getAreaText() {
        Object item = this.areaSelect.getEditor().getItem();
        return item == null ? "" : item.toString();
    }

    void setAreaText(String text) {
        this.areaSelect.getEditor().setItem(text);
    }

    ArrayList<String> getAreaItems() {
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 0; i < this.areaSelect.getItemCount(); i++) {
            items.add(this.areaSelect.getItemAt(i));
        }
        return items;
    }

    protected void doSearch() {
        MobFilter f = new MobFilter();
        f.name = searchInput.getText();
        // the typed text, the selected item isn't updated until the edit is committed
        f.area = this.getAreaText().trim();
        try {
            f.minExp = Integer.parseInt(expInput.getText());
        } catch (Exception ex) {
            // ignored
        }
        f.isZinium = isZinium.isSelected();

        ArrayList<Mob> results = this.searchEngine.search(f);

        this.setResults(results);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // TODO Auto-generated method stub

    }

    public void addMobListener(IMobListener l) {
        this.listeners.add(l);
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            this.doSearch();
        }
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
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
