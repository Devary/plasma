package services.processing;

import files.IAbstractFile;
import hierarchy.property.PropertiesFile;
import org.apache.log4j.Logger;
import projects.ProjectFile;
import projects.ProjectImpl;
import services.parsing.PropertyParsingService;
import services.reporting.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class PropertyFileCreationProcess extends AbstractProcess {
    private Connection conn = null;
    private static Logger logger = Logger.getLogger(PropertyFileCreationProcess.class);

    public PropertyFileCreationProcess(Report report) {
        super(report);
    }
    public PropertyFileCreationProcess() {

        super(new Report());
    }


    @Override
    public ProjectImpl createProject(String basePath) {
        return null;
    }

    @Override
    public IAbstractFile createAbstractFile() {
        return null;
    }

    @Override
    public ArrayList createObjectFiles(ArrayList<ProjectFile> projectJavaFiles, Report report) {
        logger.warn("Processing Object creation");
        int cnt = 0;
        ArrayList<PropertiesFile> properties = new ArrayList<>();
        for (ProjectFile projectFile:projectJavaFiles)
        {
            PropertyParsingService ps = new PropertyParsingService(projectFile);
            PropertiesFile prop = ps.getPropertiesFile();
                properties.add(prop);
            logger.warn(projectFile.getName()+" built successfully !");
            cnt++;
        }
        logger.warn("FINISHED :: "+cnt+" Objects created !");
        seedProperties(properties);
        return properties;
    }

    private void seedProperties(ArrayList<PropertiesFile> properties) {
        connect();
        PreparedStatement statement;
        for (PropertiesFile prop:properties){
            for (Map.Entry<Object, Object> entry : prop.getProperties().entrySet()) {
                Object key = entry.getKey();
                int value = Integer.parseInt((String) entry.getValue());
                try {
                    statement = conn.prepareStatement("INSERT INTO PROPERTIES (cid,class_full_name) VALUES (?,?)");
                    statement.setInt(1,value);
                    statement.setString(2,(String)key);
                    //statement.setInt(3,null); class_id
                    statement.executeUpdate();
                }catch (SQLException exception){
                    exception.printStackTrace();
                }
            }

        }
    }
    private final String url = "jdbc:postgresql://localhost:5432/test";
    private final String user = "postgres";
    private final String password = "Fakher15";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                logger.warn(e.getMessage());
            }
        }
        return conn;

    }
}
