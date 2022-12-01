/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.processing;

import hierarchy.Classes.JavaClass;
import hierarchy.Classes.types.JavaField;
import hierarchy.persistence.Persistent;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;


public class JavaClassRecursiveUpdate {

    private final ArrayList<Properties> properties;
    private ArrayList<JavaClass> javaclasses;
    private ArrayList<Persistent> persistences;
    private static Logger logger = Logger.getLogger(JavaClassRecursiveUpdate.class);

    private Connection conn = null;
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

    public JavaClassRecursiveUpdate(ArrayList<JavaClass> javaclasses, ArrayList<Persistent> persistences, ArrayList<Properties> properties) {
        this.javaclasses = javaclasses;
        this.persistences = persistences;
        this.properties = properties;
        //update();
        updatev2();
    }

    private void update()
     {
         logger.warn("processing update...");
         for (JavaClass javaClass:javaclasses)
         {
             //updateImpl(javaClass);
             //updateExt(javaClass);
             updatePersistent(javaClass);
             logger.warn("Class: "+ javaClass.getClassName()+" Has been updated");
         }
         logger.warn("UPDATE DONE !");
     }

    private void updatePersistent(JavaClass javaClass) {
        for (Persistent persistent : persistences) {
            Persistent found = find(persistent,javaClass);

            if (found != null) {
                javaClass.setPersistent(found);
                seedAndUpdatePersistentTable(found,javaClass);
                logger.warn("updating persistent : "+persistent.getName());
                break;
            }
        }
    }
    private void updatev2()
    {
        logger.warn("processing update...");
        for (Persistent persistent:persistences)
        {
            //updateImpl(javaClass);
            //updateExt(javaClass);
            updatePersistentv2(persistent);
            logger.warn("Class: "+ persistent.getName()+" Has been updated");
        }
        logger.warn("UPDATE DONE !");
    }
    private void updatePersistentv2(Persistent persistent) {
            JavaClass found = find(persistent);

            if (found != null) {
                found.setPersistent(persistent);
                seedAndUpdatePersistentTable(persistent,found);
                logger.warn("updating persistent : "+persistent.getName());
            }
    }

    public void seedAndUpdatePersistentTable(Persistent found, JavaClass javaClass) {
        connect();
        try {
            connect();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM PERSISTENT WHERE NAME ='"+ found.getName()+"'");
            while (rs.next()){
                int persId = rs.getInt("id");
                Statement statement2 = conn.createStatement();
                ResultSet rs2 = statement2.executeQuery("SELECT * FROM LINKS WHERE PERSISTENT_ID =" + persId);
                while (rs2.next()){
                    int linkId = rs2.getInt("id");
                    String linkName = rs2.getString("name");
                    //Optional<Optional<JavaField>> target = Optional.of(javaClass.getJavaFields().stream().filter(f -> f.getName().equals(linkName)).findFirst());
                    JavaField target = javaClass.getJavaFields() != null ? javaClass.getJavaFields().stream().filter(javaField -> javaField.getName().equals(linkName)).findFirst().orElse(null):null;
                    if (target!=null){
                        StringBuilder sb = new StringBuilder("UPDATE links SET \"elementType\" = ? ");
                        if (target.isCollection()){
                            sb.append(", \"collectionType\" = ?");
                            sb.append(", is_collection = ?");
                        }
                        PreparedStatement statementupd = conn.prepareStatement(sb.toString()+" where id = ?");
                        if (target.isCollection()){
                            statementupd.setString(1,target.getType());
                            statementupd.setString(2,target.getCollectionType());
                            statementupd.setBoolean(3, target.isCollection());
                            statementupd.setInt(4,linkId);
                        }else{
                            statementupd.setString(1,target.getType());
                            statementupd.setInt(2,linkId);
                        }

                        int update = statementupd.executeUpdate();
                        if (update<1){
                            throw new NullPointerException("UPDATE FAILED");
                        }
                        logger.warn("Link "+linkName+" has been updated");
                        statementupd.close();
                    }else {
                        throw new NoSuchFieldException("Field "+linkName+" doesn't exists in class"+ found.getName());
                    }
                }
                statement2.close();
            }
            statement.close();
        } catch (SQLException | NoSuchFieldException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateCID(JavaClass javaClass) {
        for (Properties property : properties) {
            Properties found = find(property,javaClass);

            //TODO : complete impl
        }
    }

    private void updateImpl(JavaClass javaClass) {
        if (javaClass.getImplementations()==null)
        {
            return;
        }
        ArrayList<JavaClass> newList = new ArrayList<>();
        for (JavaClass jc : javaClass.getImplementations()) {
            JavaClass found = find(jc);

            if (found != null) {
                newList.add(found);
            } else {
                newList.add(jc);
            }
        }
        javaClass.setImplementations(newList);
    }
    private void updateExt(JavaClass javaClass) {
        if (javaClass.getHeritances()==null)
        {
            return;
        }
        ArrayList<JavaClass> newList = new ArrayList<>();
        for (JavaClass jc : javaClass.getHeritances()) {
            JavaClass found = find(jc);

            if (found != null) {
                newList.add(found);
            } else {
                newList.add(jc);
            }
        }
        javaClass.setHeritances(newList);
    }

    private JavaClass find(JavaClass jc) {
       if (jc.getClassName()!=null)
       {
           for (JavaClass javaClass:this.javaclasses)
           {
               if (javaClass.getClassName()!=null)
               {
                   if (javaClass.getClassName().equals(jc.getClassName()))
                   {
                       return javaClass;
                   }
               }
               else
               {
                   try {
                       throw  new JavaClassObjectNotFoundException("class with NULL classname");
                   } catch (JavaClassObjectNotFoundException e) {
                       e.printStackTrace();
                   }
               }

           }
       }
       else
       {
           try {
               throw  new JavaClassObjectNotFoundException("class with NULL classname");
           } catch (JavaClassObjectNotFoundException e) {
               e.printStackTrace();
           }
       }
       return null;
    }
    private Persistent find(Persistent persistent,JavaClass javaClass) {
        if (persistent.getClassName()!=null)
        {
            for (Persistent pers:this.persistences)
            {
                if (pers.getClassName()!=null)
                {
                    if (pers.getClassName().equals(javaClass.getClassName()))
                    {
                        return pers;
                    }
                }
                else
                {
                    try {
                        throw  new JavaClassObjectNotFoundException("class with NULL classname");
                    } catch (JavaClassObjectNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        else
        {
            try {
                throw  new JavaClassObjectNotFoundException("class with NULL classname");
            } catch (JavaClassObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Properties find(Properties properties,JavaClass javaClass) {
        if (javaClass.getClassName()!=null)
        {
            for (Properties props:this.properties)
            {

                //TODO: find class name in a loaded properties File
                if (props.keys()!=null)
                {
                    if (props.get(1).equals(javaClass.getClassName()))
                    {
                        return props;
                    }
                }
                else
                {
                    try {
                        throw  new JavaClassObjectNotFoundException("class with NULL classname");
                    } catch (JavaClassObjectNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        else
        {
            try {
                throw  new JavaClassObjectNotFoundException("class with NULL classname");
            } catch (JavaClassObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private JavaClass find(Persistent persistent) {
        int persistentFieldsSize = persistent.getLinks().size()+
                persistent.getFields().size()+
                persistent.getCodes().size();
        for (JavaClass clazz : javaclasses) {
            if (clazz.getClassName().equals(persistent.getName()) && clazz.getJavaFields().size()==persistentFieldsSize) {
                return clazz;
            }
        }
        return null;
    }

}
