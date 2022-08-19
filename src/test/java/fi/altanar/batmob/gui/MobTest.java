package fi.altanar.batmob.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import fi.altanar.batmob.controller.RegexTrigger;

public class MobTest {
 
    @Test
    void testPkills() {
        RegexTrigger tm = new RegexTrigger();

        try {
            URL testFileURL = ClassLoader.getSystemResource("pkills.txt");
            File myFile = new File(testFileURL.toURI());
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              Object obj = tm.process(data);
              assertNotNull(obj);
              System.out.println(obj);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

