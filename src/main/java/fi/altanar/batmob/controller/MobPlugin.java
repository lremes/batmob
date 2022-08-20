package fi.altanar.batmob.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.BatClientPluginTrigger;
import com.mythicscape.batclient.interfaces.BatClientPluginUtil;
import com.mythicscape.batclient.interfaces.BatWindow;
import com.mythicscape.batclient.interfaces.ParsedResult;

import fi.altanar.batmob.gui.LogPanel;
import fi.altanar.batmob.io.GuiDataPersister;
import fi.altanar.batmob.vo.GuiData;

public class MobPlugin extends BatClientPlugin implements BatClientPluginTrigger, ActionListener, BatClientPluginUtil {

    private String BASEDIR = null;

    private MobEngine engine;
    private LogPanel logPanel;

    private final String CHANNEL_PREFIX = "BAT_MAPPER";
    private final int PREFIX = 0;
    private final int AREA_NAME = 1;

    private final int MESSAGE_LENGTH = 9;
    private final int EXIT_AREA_LENGTH = 2;

    private final String EXIT_AREA_MESSAGE = "REALM_MAP";

    public void loadPlugin() {
        BASEDIR = this.getBaseDirectory();
        GuiData guiData = GuiDataPersister.load( BASEDIR );

        BatWindow clientWin;
        if (guiData != null) {
            clientWin = this.getClientGUI().createBatWindow( "Mobs", guiData.getX(), guiData.getY(), guiData.getWidth(), guiData.getHeight() );
        } else {
            clientWin = this.getClientGUI().createBatWindow( "Mobs", 300, 300, 820, 550 );
        }


        logPanel = new LogPanel();
        clientWin.newTab( "Mobs", logPanel );

        engine = new MobEngine(this);
        engine.setBatWindow( clientWin );
        engine.setBaseDir(BASEDIR);

        clientWin.setVisible( true );
        clientWin.removeTabAt( 0 );
        this.getPluginManager().addProtocolListener( this );
        clientWin.addComponentListener( engine );

    }

    @Override
    public String getName() {
        return "batMob";
    }

    //	ArrayList<BatClientPlugin> plugins=this.getPluginManager().getPlugins();
    @Override
    public ParsedResult trigger( ParsedResult input ) {
        engine.trigger(input);
        return null;
    }

    @Override
    public void actionPerformed( ActionEvent event ) {
        //cMapper;areaname;roomUID;exitUsed;indoor boolean;shortDesc;longDesc;exits
        
        String input = event.getActionCommand();
        String[] values = input.split( ";;", - 1 );

        if (values[PREFIX].equals( CHANNEL_PREFIX ) && values.length == MESSAGE_LENGTH) {
            String areaName = values[AREA_NAME];
            engine.setCurrentAreaName(areaName);
        } else if (values[PREFIX].equals( CHANNEL_PREFIX ) && values.length == EXIT_AREA_LENGTH) {
            this.engine.setCurrentAreaName(EXIT_AREA_MESSAGE);
            this.engine.saveMobs();
        }
    }

    @Override
    public void clientExit() {
        this.engine.saveMobs();
    }

    @Override
    public void process(Object input){
        if ( input == null){
            printConsoleMessage("Mob companion has following commands:");
        }
    }

    private void printConsoleError(String msg){
        this.getClientGUI().printText("general","[batMob error] "+msg+"\n", "F7856D");
    }

    private void printConsoleMessage(String msg){
        this.getClientGUI().printText("general","[batMob] "+msg+"\n", "6AFA63");
    }

    public void doCommand( String string ) {
        this.getClientGUI().doCommand( string );

    }

    public void log(Object obj) {
        try {
            File logFile = getFile( "logs", "batmob.txt" );
            FileWriter myWriter = new FileWriter(logFile, true);
            myWriter.write(obj.toString() + '\n');
            myWriter.close();
        } catch (IOException e) {
            //printConsoleError(e.getMessage());
            System.out.println(e.getMessage());
        }

        if (logPanel != null) {
            logPanel.appendText(obj.toString() + '\n');
        }
    }

    private File getFile( String subDir, String filename ) {
        File dirFile = new File( BASEDIR, subDir );
        File file = new File( dirFile, filename );
        return file;
    }
}