package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

/**
 * A utility class for handling SQL operations on a specific table.
 * Provides methods for modifying the table structure, inserting and updating rows,
 * and converting table data into a JTable for GUI display.
 */
public class SQLHandler {
    private Connection con;
    private String tableName;
    private String insert_st = null;
    private PreparedStatement pst;
    /** Column names that are NOT NULL and have no default value */
    private String[] no_default_col_names;

    /**
     * Constructs a SQLHandler for a given table and connection.
     *
     * @param con the JDBC connection to the database
     * @param tableName the name of the table to operate on
     */
    public SQLHandler(Connection con, String tableName) {
        this.con = con;
        this.tableName = tableName;
        no_default_colomns_return();
    }

    /**
     * Retrieves column names from the table and returns them wrapped in a TableInfo object.
     *
     * @return a TableInfo object containing the table name and column names
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Fetches all rows from the table and returns them as a JTable for display in a GUI.
     *
     * @param table the TableInfo object containing column names
     * @return a JTable containing all data from the table
     */
    public JTable getTable(TableInfo table){
        ArrayList<String[]> rows = null;
        String query = "select * from " + this.tableName;
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

    /**
     * Adds a new column to the table with a specific SQL type and default value.
     *
     * @param colomn_name the name of the new column
     * @param col_class the SQL type: 0 for varchar, 1 for int, 2 for float
     */
    public void add_colomn(String colomn_name, int col_class){
        String col = null;
        switch (col_class){
            case 0:
                col = " varchar(255) set default \'\'";
                break;
            case 1:
                col = " int set default \'\'";
                break;
            case 2:
                col = " float set default \'\'";
                break;
        }
        String querry = "ALTER TABLE " + this.tableName + " ADD COLUMN " + colomn_name + col;
        try (Statement st = con.createStatement()){
            st.execute(querry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        no_default_colomns_return();
    }

    /**
     * Removes a column from the table.
     *
     * @param colomn_name the name of the column to remove
     */
    public void remove_colomn(String colomn_name){
        String querry = "ALTER TABLE " + this.tableName + " DROP COLUMN " + colomn_name;
        try (Statement st = con.createStatement()){
            st.execute(querry);
        } catch (SQLException e){
            e.printStackTrace();
        }
        no_default_colomns_return();
    }

    /**
     * Deletes a row from the table based on a column match.
     *
     * @param col the name of the column used in the WHERE clause
     * @param row_start the value to match for deletion
     */
    public void remove_row(String col, String row_start){
        String whr = col + "=" + "\'" + row_start + "\'";
        String querry = "DELETE FROM " + this.tableName + " WHERE " + whr;
        try (Statement st = con.createStatement()){
            st.execute(querry);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Updates a single value in the table using a prepared statement.
     *
     * @param colomn the name of the column to update
     * @param row the new value to set
     * @param check_colomn the column used for matching rows
     * @param check_row the value in the check column to match
     * @throws SQLException if a database access error occurs
     */
    public void save_value(String colomn, String row, String check_colomn, String check_row) throws SQLException {
        String prep_st = "UPDATE " + tableName + " SET " + colomn + "=? WHERE " + check_colomn + "=?";
        PreparedStatement prst = con.prepareStatement(prep_st);
        prst.setString(1, row);
        prst.setString(2, check_row);
        prst.executeUpdate();
    }

    /**
     * Inserts a new row into the table with a given ID and user-input values for required columns.
     *
     * @param id the ID value for the row
     * @param id_colomn the name of the ID column
     */
    public void insert_row(int id, String id_colomn){
        try {
            if(insert_st == null){
                this.insert_st = "insert into " + tableName +"(" + id_colomn;
                String str_values = ") value(?";
                for (String col : no_default_col_names){
                    this.insert_st += ", " + col;
                    str_values += ",? ";
                }
                this.insert_st += str_values + ")";
                try{
                    pst = con.prepareStatement(insert_st);
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            pst.setString(1, String.valueOf(id));
            for (int i = 0; i < no_default_col_names.length; i++){
                String col_data = JOptionPane.showInputDialog(null, ("Colomn "+ no_default_col_names[i] + " must be filled. Please enter colomns value."));
                pst.setString(i+2, col_data);
            }
            pst.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieves and stores column names from the table that are NOT NULL and have no default values.
     * These columns are required for manual input during row insertion.
     */
    private void no_default_colomns_return(){
        String query = "SELECT COLUMN_NAME, IS_NULLABLE, COLUMN_DEFAULT " +
        "FROM INFORMATION_SCHEMA.COLUMNS " +
        "WHERE TABLE_NAME = \'" + tableName + "\' " +
        "AND IS_NULLABLE = 'NO' " +
        "AND COLUMN_DEFAULT IS NULL";

        try(Statement statement = con.createStatement()){
            ResultSet rs = statement.executeQuery(query);

            ArrayList<String> no_def_col_names = new ArrayList<>();
            while (rs.next()) {
                no_def_col_names.add(rs.getString(1));
            }
            no_default_col_names = no_def_col_names.toArray(new String[no_def_col_names.size()]);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}