package org.example;

import java.sql.*;

public class SQLHandler {
    Connection con;

    public SQLHandler(Connection con) {
        this.con = con;
    }

    public TableInfo form_table(String table_name) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from " + table_name);
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        String[] col_names = new String[cols];
        for (int i = 1; i <= cols; i++) {
            String col_name = rsmd.getColumnName(i);
            col_names[i-1] = col_name;
        }
        TableInfo tableInfo = new TableInfo(table_name, col_names);

        return tableInfo;
    }
}