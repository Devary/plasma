package services.processing.data;

import files.FileTypes;
import hierarchy.persistence.Persistent;
import hierarchy.persistence.types.Link;
import mappers.AbstractMapper;
import org.apache.log4j.Logger;
import projects.ProjectImpl;
import services.IServiceImpl;
import services.processing.AbstractProcess;
import services.processing.ProcessingTypes;
import services.processing.PropertyFileCreationProcess;
import services.reporting.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SeederService  extends IServiceImpl {
    private static String basePath = "C:/Sandboxes/solife_6_1_2_CLV23_FP";
    private static Report report = new Report();
    private static Logger logger = Logger.getLogger(PropertyFileCreationProcess.class);
    Connection conn;
    public SeederService() {
        this.conn = Connections.connect("", true);
    }

    public void processorEntities() {

        try {
            AbstractMapper abstractMapperPersistence = initAbstractMapper(ProcessingTypes.PERSISTENT, FileTypes.PERSISTENCE, basePath, report);
            ProcessPersistent(abstractMapperPersistence);

        } catch (Exception exception) {
            exception.printStackTrace();
        }


        try {
            AbstractMapper abstractMapperJava = initAbstractMapper(ProcessingTypes.JAVACLASS, FileTypes.JAVACLASS, basePath, report);
            ProcessJavaClasses(abstractMapperJava);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        try {
            AbstractMapper abstractMapperProperties = initAbstractMapper(ProcessingTypes.PROPERTY, FileTypes.CIDREG, basePath, report);
            ProcessProperties(abstractMapperProperties);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        //this.persistences = abstractMapperPersistence.getProcess().getPersistents();
    }

    private static AbstractMapper initAbstractMapper(String processingType, String fileType, String basePath, Report report) {
        AbstractProcess abstractProcess = new AbstractProcess(processingType, basePath, report);
        ProjectImpl project = abstractProcess.createProject(basePath);
        AbstractMapper abstractMapper = new AbstractMapper(project, fileType);
        abstractMapper.setProject(project);
        abstractMapper.setProcess(abstractProcess);
        return abstractMapper;
    }

    private static void ProcessJavaClasses(AbstractMapper abstractMapperJava) throws Exception {
        try {
            abstractMapperJava.getProcess().adaptProcess(abstractMapperJava.getProjectFiles(), ProcessingTypes.JAVACLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void ProcessPersistent(AbstractMapper abstractMapperPersistence) throws Exception {
        try {
            abstractMapperPersistence.getProcess().adaptProcess(abstractMapperPersistence.getProjectFiles(), ProcessingTypes.PERSISTENT);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void ProcessProperties(AbstractMapper abstractMapperProperties) throws Exception {
        try {
            abstractMapperProperties.getProcess().adaptProcess(abstractMapperProperties.getProjectFiles(), ProcessingTypes.PROPERTY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void seedVirtualLink(Persistent sourcePersistent, Link sourceLink, Link targetLink, Persistent targetPersistent) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO VIRTUAL_LINK (source_persistent_id,source_link_id,target_link_id,target_persistent_id) VALUES(?,?,?,?)");
            ps.setLong(1,sourcePersistent.getId());
            ps.setLong(2,sourceLink.getId());
            if (targetLink != null){
                ps.setLong(3,targetLink.getId());
            }else{
                ps.setNull(3,2);
            }
            ps.setLong(4,targetPersistent.getId());
            int update = ps.executeUpdate();
            if (update == -1){
                logger.error("No link are inserted");
            }
        } catch (SQLException exception) {

        }
    }
}
