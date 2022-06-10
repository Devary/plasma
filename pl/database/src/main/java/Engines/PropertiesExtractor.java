package Engines;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class PropertiesExtractor {

    public static String getPropValue(String filename ,String val) throws IOException {
        InputStream inputStream = null;
        String result = "";

        try {
            Properties prop = new Properties();
            String propFileName = filename+".properties";

            inputStream = PropertiesExtractor.class.getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            //Date time = new Date(System.currentTimeMillis());

            // get the property value and print it out
            return prop.getProperty(val);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return result;
    }

    public static Properties getAllProperties(String filename) throws IOException {
        Properties prop = new Properties();
        String propFileName = filename+".properties";

        InputStream inputStream = PropertiesExtractor.class.getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
        return prop;
    }
    public static String getConnectionType() throws IOException {
        return PropertiesExtractor.getPropValue("params", "database.driver");

    }
}
