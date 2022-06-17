package Engines;

import java.sql.SQLException;

public interface AbstractEngine {
    public boolean create(Object object) throws SQLException;
    public boolean delete(Object object);

}
