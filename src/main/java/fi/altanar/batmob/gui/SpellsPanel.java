package fi.altanar.batmob.gui;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.awt.event.ActionEvent;

import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.BoxLayout;
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

import java.awt.event.ComponentListener;
import java.awt.event.ActionListener;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.io.IMobListener;
import fi.altanar.batmob.vo.Mob;
import fi.altanar.batmob.vo.MobFilter;

public class SpellsPanel extends JPanel implements
        MouseInputListener,
        ActionListener,
        ListSelectionListener {
    MobEngine engine;

    private final int LAYOUT_WIDTH = 800;
    private final int LAYOUT_HEIGHT = 550;

    public final Dimension LAYOUT_SIZE = new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT);
    public final Dimension MIN_PANEL_SIZE = new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT);

    private final Color BORDER_COLOR = Color.LIGHT_GRAY;
    private final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private final Color BG_COLOR = Color.BLACK;

    private JList<Object[]> resultList;
    private JScrollPane scrollableResults;
    private JPanel resultPanel = new JPanel();

    private DefaultListModel<Object[]> listModel = new DefaultListModel<Object[]>();

    public SpellsPanel(MobEngine engine) {
        super();

        this.engine = engine;

        this.setPreferredSize(LAYOUT_SIZE);
        this.setMinimumSize(LAYOUT_SIZE);
        this.setMaximumSize(LAYOUT_SIZE);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(Component.LEFT_ALIGNMENT);

        resultList = new JList<Object[]>(this.listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        resultList.setLayoutOrientation(JList.VERTICAL);
        resultList.setVisibleRowCount(-1);
        resultList.setForeground(TEXT_COLOR);
        resultList.setBackground(BG_COLOR);
        resultList.setPreferredSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
        resultList.setMaximumSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
        resultList.setMinimumSize(new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT));
        resultList.addListSelectionListener(this);
        resultList.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollableResults = new JScrollPane(resultList);
        scrollableResults.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollableResults.setBorder(new LineBorder(BORDER_COLOR));
        resultList.setVisible(true);
        resultList.setVisibleRowCount(25);
        resultPanel.add(scrollableResults);

        this.add(resultPanel);
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
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        // search result clicked
    }
}
