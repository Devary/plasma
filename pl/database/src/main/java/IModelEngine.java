import java.sql.SQLException;

public interface IModelEngine {
    public boolean store(Object obj) throws SQLException;
}
