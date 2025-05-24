package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

public class SQLHandler {
    Connection con;
    String tableName;

    public SQLHandler(Connection con, String tableName) {
        this.con = con;
        this.tableName = tableName;
    }

    public TableInfo form_table() throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from " + this.tableName);
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        String[] col_names = new String[cols];
        for (int i = 1; i <= cols; i++) {
            String col_name = rsmd.getColumnName(i);
            col_names[i-1] = col_name;
        }
        TableInfo tableInfo = new TableInfo(tableName, col_names);

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
        DefaultTableModel tableModel = new DefaultTableModel(contents, table.columns());
        JTable gui_table = new JTable(tableModel);
        return gui_table;
    }

    public void add_colomn(String colomn_name){
        String querry = "ALTER TABLE " + this.tableName + " ADD COLUMN " + colomn_name + " varchar(255)";
        try (Statement st = con.createStatement()){
            st.execute(querry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove_colomn(String colomn_name){
        String querry = "ALTER TABLE " + this.tableName + " DROP COLUMN " + colomn_name;
        try (Statement st = con.createStatement()){
            st.execute(querry);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void remove_row(String col, String row_start){
        String whr = col + "=" + "\'" + row_start + "\'";
        String querry = "DELETE FROM " + this.tableName + " WHERE " + whr;
        try (Statement st = con.createStatement()){
            st.execute(querry);
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}