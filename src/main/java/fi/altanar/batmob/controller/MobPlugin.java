package fi.altanar.batmob.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public void loadPlugin() {
        BASEDIR = this.getBaseDirectory();
        GuiData guiData = GuiDataPersister.load( BASEDIR );

        BatWindow clientWin;
        if (guiData != null) {
            clientWin = this.getClientGUI().createBatWindow( "Mobs", guiData.getX(), guiData.getY(), guiData.getWidth(), guiData.getHeight() );
        } else {
            clientWin = this.getClientGUI().createBatWindow( "Mobs", 300, 300, 820, 550 );
        }

        engine = new MobEngine(this);
        engine.setBatWindow( clientWin );

        clientWin.setVisible( true );
        clientWin.removeTabAt( 0 );
        logPanel = new LogPanel();
        clientWin.newTab( "log", logPanel );
        this.getPluginManager().addProtocolListener( this );
        engine.setBaseDir( BASEDIR );
        clientWin.addComponentListener( engine );
    }

    @Override
    public String getName() {
        return "batMob";
    }

    //	ArrayList<BatClientPlugin> plugins=this.getPluginManager().getPlugins();
    @Override
    public ParsedResult trigger( ParsedResult input ) {
/**
        if (input.getStrippedText().startsWith( "SAVED." )) {
            this.engine.save();
        }
        */


        System.out.println(input);
        return null;
    }

    @Override
    public void actionPerformed( ActionEvent event ) {
        //System.out.println();
        /**
         *
         received 99 protocol: cMapper;;sunderland;;$apr1$dF!!_X#W$v3dsdL2khaffFpj1BvVrD0;;road;;0;;The long road to Sunderland;;You see a long road stretching northward into the distance. As far as
         you can tell, the way ahead looks clear.
         ;;north,south;;

         event data amount: 9

         */

        //cMapper;areaname;roomUID;exitUsed;indoor boolean;shortDesc;longDesc;exits
        String input = event.getActionCommand();
        //String[] values = input.split( ";;", - 1 );
        //logPanel.appendText(input);
        System.out.println(input);
    }

    @Override
    public void clientExit() {
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
}