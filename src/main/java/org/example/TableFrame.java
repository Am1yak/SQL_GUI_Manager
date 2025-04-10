package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class TableFrame extends JFrame {
    JPanel panel = new JPanel();
    Connection con;

    public TableFrame(Connection con) {
        this.con = con;
    }

    public void ui_init(){
        TableInfo table = null;
        SQLHandler handler = new SQLHandler(con);
        try {
            table = handler.form_table("Persons");
        } catch (SQLException e){
            e.printStackTrace();
        }

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
        JScrollPane pane = new JScrollPane(gui_table);
        panel.add(pane);
    }

    public void init(){
        ui_init();
        this.add(panel);
        this.setTitle("Table");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.show();
    }
}
