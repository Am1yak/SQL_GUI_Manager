package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A utility class for managing connection to database.
 * Provides method to establish connection and returns it for further work
 */
public class ConnectionsManager {
    public String dbms="mysql";
    public String userName;
    public String password;
    public String serverName;
    public int portNumber;
    public String databaseName;

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

