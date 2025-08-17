package fi.altanar.batmob.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.MouseInputListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.PlainDocument;

import fi.altanar.batmob.controller.MobEngine;
import fi.altanar.batmob.controller.SearchEngine;
import fi.altanar.batmob.io.IMobListener;
import fi.altanar.batmob.io.IStatusListener;
import fi.altanar.batmob.io.IStudyListener;
import fi.altanar.batmob.vo.Mob;

public class MobDetailsPanel extends JPanel
        implements MouseInputListener, ComponentListener, IMobListener, IStatusListener, IStudyListener {

    private static final long serialVersionUID = 8922764153155463898L;

    private final int LAYOUT_WIDTH = 800;
    private final int LAYOUT_HEIGHT = 550;
    public final Dimension LAYOUT_SIZE = new Dimension(LAYOUT_WIDTH, LAYOUT_HEIGHT);

    private final int PANEL_WIDTH = 390;
    private final int PANEL_HEIGHT = 540;
    public final Dimension MIN_PANEL_SIZE = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);

    private final int TEXT_INPUT_WIDTH = 380;
    protected int BORDERLINE = 7;
    private final int TEXT_INPUT_HEIGHT = 25;
    public final Dimension INPUT_SIZE = new Dimension(380, 30);

    private final Color TEXT_COLOR = Color.LIGHT_GRAY;
    private final Color BG_COLOR = Color.BLACK;

    private JTextArea roomArea = new JTextArea();
    private JScrollPane scrollableRoom;

    private JLabel statusLabel;

    private Font font = new Font("Consolas", Font.PLAIN, 14);
    private Font labelFont = new Font("Consolas", Font.PLAIN, 12);

    private Highlighter.HighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(
            Color.DARK_GRAY);
    private Object currentHighlight = null;

    private Mob selectedMob = null;

    DetailsPanel detailsPanel;

    MobEngine engine;
    SearchEngine searchEngine;

    public MobDetailsPanel(MobEngine engine, SearchEngine searchEngine) {
        super();

        this.engine = engine;
        this.searchEngine = searchEngine;

        this.setPreferredSize(LAYOUT_SIZE);
        this.setMinimumSize(LAYOUT_SIZE);
        this.setMaximumSize(LAYOUT_SIZE);

        this.addComponentListener(this);
        this.setLayout(new BorderLayout());
        this.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel roomPanel = new JPanel();
        roomPanel.setLayout(new BorderLayout());
        roomPanel.setBackground(BG_COLOR);
        roomPanel.setForeground(TEXT_COLOR);
        roomPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        roomPanel.setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        roomPanel.setMaximumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        JPanel statusPanel = new JPanel();
        statusPanel.setPreferredSize(new Dimension(PANEL_WIDTH, TEXT_INPUT_HEIGHT));
        statusPanel.setMinimumSize(new Dimension(PANEL_WIDTH, TEXT_INPUT_HEIGHT));
        statusPanel.setMaximumSize(new Dimension(PANEL_WIDTH, TEXT_INPUT_HEIGHT));
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        statusLabel = new JLabel("");
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusLabel.setPreferredSize(new Dimension(TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT));
        statusLabel.setMinimumSize(new Dimension(TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT));
        statusLabel.setMaximumSize(new Dimension(TEXT_INPUT_WIDTH, TEXT_INPUT_HEIGHT));
        statusLabel.setFont(font);
        statusPanel.add(statusLabel);

        JLabel mobsLabel = new JLabel("Detected mobs");
        mobsLabel.setBackground(BG_COLOR);
        mobsLabel.setForeground(TEXT_COLOR);
        mobsLabel.setFont(labelFont);
        // mobsLabel.setAlignmentY( Component.TOP_ALIGNMENT );
        // mobsLabel.setAlignmentX( Component.LEFT_ALIGNMENT );
        roomPanel.add(mobsLabel, BorderLayout.PAGE_START);

        roomArea.setWrapStyleWord(true);
        roomArea.setEditable(false);
        roomArea.addMouseListener(this);
        roomArea.setFont(font);
        roomArea.setBackground(BG_COLOR);
        roomArea.setForeground(TEXT_COLOR);
        roomArea.setAlignmentY(Component.TOP_ALIGNMENT);
        roomArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        roomArea.setColumns(50);
        roomArea.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT - TEXT_INPUT_HEIGHT));
        roomArea.setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT - TEXT_INPUT_HEIGHT));
        roomArea.setMaximumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT - TEXT_INPUT_HEIGHT));
        scrollableRoom = new JScrollPane(roomArea);
        roomArea.setToolTipText("Mobs detected. Click to show details.");

        roomPanel.add(scrollableRoom, BorderLayout.CENTER);

        roomPanel.add(statusPanel, BorderLayout.PAGE_END);

        detailsPanel = new DetailsPanel(this.engine, this.searchEngine);

        this.add(roomPanel, BorderLayout.LINE_START);
        this.add(detailsPanel, BorderLayout.CENTER);
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void mobsDetected(ArrayList<Mob> roomMobs) {
        if (currentHighlight != null) {
            roomArea.getHighlighter().removeHighlight(currentHighlight);
            currentHighlight = null;
        }

        if (roomMobs.size() > 0) {
            StringBuilder names = new StringBuilder();
            for (Iterator<Mob> iter = roomMobs.iterator(); iter.hasNext();) {
                names.append(iter.next().getName() + "\n");
            }
            this.roomArea.setText(names.toString());
            this.mobSelected(roomMobs.get(roomMobs.size() - 1));
        } else {
            this.roomArea.setText("No mobs");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(roomArea)) {
            try {
                int line;

                // Handle right-click
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int offset = roomArea.viewToModel(e.getPoint());

                    Document roomAreaDoc = roomArea.getDocument();
                    String textToSearch = roomAreaDoc.getText(0, roomAreaDoc.getLength());

                    // Find word boundaries
                    int wordStart = offset;
                    while (wordStart > 0 && !Character.isWhitespace(textToSearch.charAt(wordStart - 1))) {
                        wordStart--;
                    }

                    int wordEnd = offset;
                    while (wordEnd < textToSearch.length() && !Character.isWhitespace(textToSearch.charAt(wordEnd))) {
                        wordEnd++;
                    }

                    String selectedWord = textToSearch.substring(wordStart, wordEnd).trim();
                    this.statusLabel.setText("Study: " + selectedWord);
                    System.out.println(selectedWord);

                    if (!selectedWord.isEmpty()) {
                        this.engine.doCommand("use 'study creature' " + selectedWord);
                    }
                }

                // Add new highlight
                line = roomArea.getLineOfOffset(roomArea.getCaretPosition());
                int start = roomArea.getLineStartOffset(line);
                int end = roomArea.getLineEndOffset(line);
                String text = roomArea.getDocument().getText(start, end - start);

                if (currentHighlight != null) {
                    roomArea.getHighlighter().removeHighlight(currentHighlight);
                }
                currentHighlight = roomArea.getHighlighter().addHighlight(start, end, highlightPainter);

                Mob m = this.engine.getMobStore().get(text.trim());
                this.selectedMob = m;
                this.statusLabel.setText("Selected: " + this.selectedMob.getName());
                this.mobSelected(m);
            } catch (StringIndexOutOfBoundsException e0) {
                e0.printStackTrace();
            } catch (BadLocationException e1) {
                e1.printStackTrace();
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

    @Override
    public void mobSelected(Mob mob) {
        this.detailsPanel.mobSelected(mob);
    }

    @Override
    public void statusChanged(String str) {
        this.statusLabel.setText(str);
    }

    @Override
    public void mobStudied(Mob mob) {
        this.engine.log(mob.getName() + " studied.");
        this.statusLabel.setText("Studied: " + mob.getName());

        if (this.selectedMob != null) {
            this.selectedMob.updateFromStudy(mob);
        }

        this.mobSelected(this.selectedMob);

        this.engine.getMobStore().updateEditableFields(this.selectedMob);
    }
}
