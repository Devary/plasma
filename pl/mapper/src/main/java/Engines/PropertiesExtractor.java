package Engines;

import org.apache.log4j.Logger;
import services.processing.VirtualLinkCreationProcess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesExtractor {
    private static Logger logger = Logger.getLogger(PropertiesExtractor.class);


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
            logger.warn("Exception: " + e);
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
