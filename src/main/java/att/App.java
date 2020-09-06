package att;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class App {

    private static String[] properties = new String[7];

    static {
        try {
            InputStream input = new FileInputStream(getPropertiesFilePath()+"zoomreporter.properties");
            Properties prop = new Properties();
            prop.load(input);

            if(prop.size()!=7) System.out.println("PropFile is not set correctly");

            properties[0] = prop.getProperty("schoology.roster.file.path");
            properties[1] = prop.getProperty("zoom.download.path");
            properties[2] = prop.getProperty("webdriver.chrome.driver");
            properties[3] = prop.getProperty("zoom.link");
            properties[4] = prop.getProperty("zoom.email");
            properties[5] = prop.getProperty("zoom.password");
            properties[6] = prop.getProperty("skyward.link");
    
            System.out.println(Arrays.toString(properties));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) throws InterruptedException {
        //calling zoom download..
        ZoomDownload.main(properties);
        Thread.sleep(1000);

        //calling main func..
        CsvViewGUI.main(properties);
    }

    private static String getPropertiesFilePath() {
        String rosterPath = System.getProperty("java.class.path").substring(0,
                System.getProperty("java.class.path").lastIndexOf("\\") + 1);
        String[] rosterPaths = rosterPath.split(";");
        rosterPath = rosterPaths[0].substring(0, rosterPaths[0].lastIndexOf("\\") + 1);
        return rosterPath;
    }
}
