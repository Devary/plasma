package Engines;

import Engines.connections.MysqlConnection;

import java.sql.Statement;

public class Engine {
    private Statement statement;

    public Engine() {
        MysqlConnection.getInstance().getConnection();
        this.statement = MysqlConnection.getInstance().getStatement();
    }

    public Statement getStatement() {
        return statement;
    }
}
