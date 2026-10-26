package fi.altanar.batmob.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

        // write to a temp file first so a failed save never corrupts the existing data
        File saveFile = getFile( baseDir );
        File tmpFile = new File( dirFile, FILENAME + ".tmp" );
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream( new FileOutputStream( tmpFile ) )) {
            objectOutputStream.writeObject( saveData );
        } catch (IOException | RuntimeException e) {
            tmpFile.delete();
            throw e;
        }

        try {
            Files.move( tmpFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE );
        } catch (AtomicMoveNotSupportedException e) {
            Files.move( tmpFile.toPath(), saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING );
        }
    }

    public static MobSaveObject load( String basedir ) throws IOException, ClassNotFoundException {
        if (getFile( basedir ).exists() == true) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream( new FileInputStream( getFile( basedir ) ) )) {
                return (MobSaveObject) objectInputStream.readObject();
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
