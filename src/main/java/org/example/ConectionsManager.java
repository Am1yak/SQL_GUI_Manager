package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConectionsManager {
    String dbms = "mysql";
    String userName = "root";
    String password = "9692";
    String serverName = "localhost";
    int portNumber = 3306;
    String databaseName = "new_schema";

    public Connection getConnection() throws SQLException {

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);

        if (this.dbms.equals("mysql")) {
            conn = DriverManager.getConnection(
                    "jdbc:" + this.dbms + "://" +
                            this.serverName +
                            ":" + this.portNumber + "/"
                    + this.databaseName,
                    connectionProps);
            System.out.println("Connected to database");
            return conn;
        }
    return conn;
    }
}

