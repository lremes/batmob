package fi.altanar.batmob.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import fi.altanar.batmob.vo.Mob;

public class MobDataPersister {
    private final static String FILENAME = "mobData.conf";
    private final static String DIRNAME = "conf";


    public static void save( String baseDir, Map<String, Mob> mobs ) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream( getFile( baseDir ) );
            ObjectOutputStream objectOutputStream = new ObjectOutputStream( fileOutputStream );
            objectOutputStream.writeObject( mobs );
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println( e );
        }
    }

    public static Map<String, Mob> load( String basedir ) {
        try {
            FileInputStream fileInputStream = new FileInputStream( getFile( basedir ) );
            ObjectInputStream objectInputStream = new ObjectInputStream( fileInputStream );

            Map<String, Mob> mobs = (Map<String, Mob>) objectInputStream.readObject();
            return mobs;
        } catch (IOException e) {
            System.out.println( e );
        } catch (ClassNotFoundException e) {
            System.out.println( e );
        }
        return null;

    }

    private static File getFile( String basedir ) {
        File dirFile = new File( basedir, DIRNAME );
        File saveFile = new File( dirFile, FILENAME );
        return saveFile;
    }

}
