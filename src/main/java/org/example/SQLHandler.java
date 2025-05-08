package org.example;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

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

    public JTable getTable(TableInfo table){
        ArrayList<String[]> rows = null;
        String query = "select * from Persons";
        try (Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(query);

            String[] cols = table.columns();
            rows = new ArrayList<>();
            while (rs.next()){
                String[] row = new String[cols.length];
                for (int i = 0; i < cols.length; i++){
                    row[i] = rs.getString(cols[i]);
                }
                rows.add(row);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        String[][] contents = rows.toArray(new String[rows.size()][]);
        JTable gui_table = new JTable(contents, table.columns());
        return gui_table;
    }
}