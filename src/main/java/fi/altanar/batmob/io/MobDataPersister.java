package fi.altanar.batmob.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import fi.altanar.batmob.vo.MobSaveObject;

public class MobDataPersister {
    private final static String FILENAME = "mobData.conf";
    private final static String DIRNAME = "conf";

    public static void save( String baseDir, MobSaveObject saveData ) {
        File dirFile = new File( baseDir, DIRNAME );
        try {
            if (! dirFile.exists()) {
                if (! dirFile.mkdir()) {
                    throw new IOException( baseDir + " doesn't exist" );
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream( getFile( baseDir ) );
            ObjectOutputStream objectOutputStream = new ObjectOutputStream( fileOutputStream );
            objectOutputStream.writeObject( saveData );
            fileOutputStream.close();
        } catch (IOException e) {
            System.out.println( e );
        }
    }

    public static MobSaveObject load( String basedir ) {
        if (getFile( basedir ).exists() == true) {
            try {
                FileInputStream fileInputStream = new FileInputStream( getFile( basedir ) );
                ObjectInputStream objectInputStream = new ObjectInputStream( fileInputStream );
                MobSaveObject saveData = (MobSaveObject) objectInputStream.readObject();
                return saveData;
            } catch (IOException e) {
                System.out.println( e );
            } catch (ClassNotFoundException e) {
                System.out.println( e );
            }
        }
        return null;
    }

    private static File getFile( String basedir ) {
        File dirFile = new File( basedir, DIRNAME );
        File saveFile = new File( dirFile, FILENAME );
        return saveFile;
    }

}
