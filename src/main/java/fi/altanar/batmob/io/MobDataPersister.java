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

    public static void save( String baseDir, MobSaveObject saveData ) throws IOException {
        File dirFile = new File( baseDir, DIRNAME );
        if (! dirFile.exists()) {
            if (! dirFile.mkdir()) {
                throw new IOException( baseDir + " doesn't exist" );
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream( getFile( baseDir ) );
        ObjectOutputStream objectOutputStream = new ObjectOutputStream( fileOutputStream );
        objectOutputStream.writeObject( saveData );
        fileOutputStream.close();
    }

    public static MobSaveObject load( String basedir ) throws IOException, ClassNotFoundException {
        if (getFile( basedir ).exists() == true) {
            FileInputStream fileInputStream = new FileInputStream( getFile( basedir ) );
            ObjectInputStream objectInputStream = new ObjectInputStream( fileInputStream );
            MobSaveObject saveData = (MobSaveObject) objectInputStream.readObject();
            return saveData;
        }
        return null;
    }

    private static File getFile( String basedir ) {
        File dirFile = new File( basedir, DIRNAME );
        File saveFile = new File( dirFile, FILENAME );
        return saveFile;
    }

}
