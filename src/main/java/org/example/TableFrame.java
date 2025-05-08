package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.ArrayList;

public class TableFrame extends JFrame {
    JPanel panel = new JPanel();
    Connection con;
    String tableName;

    public TableFrame(Connection con) {
        this.con = con;
        this.tableName = JOptionPane.showInputDialog(null, "Enter the table name:", "Table Name", JOptionPane.PLAIN_MESSAGE);
    }

    public void ui_init(){
        JToolBar toolBar = new JToolBar();
        JButton switch_database_button = new JButton("Switch Database");
        TableFrame mainFrame = this;
        switch_database_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TableFrame frame = new TableFrame(con);
                frame.init();
                mainFrame.dispose();
            }
        });
        toolBar.add(switch_database_button);

        panel.add(toolBar);
        TableInfo table = null;
        SQLHandler handler = new SQLHandler(con);
        try {
            table = handler.form_table(tableName);
        } catch (SQLException e){
            e.printStackTrace();
        }

        JTable gui_table = handler.getTable(table);
        JScrollPane pane = new JScrollPane(gui_table);
        panel.add(pane);

        JButton save_button = new JButton("Save");
        save_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.add(save_button);

        JButton add_row_button = new JButton("Add Row");
        add_row_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.add(add_row_button);

        JButton add_column_button = new JButton("Add Column");
        add_column_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.add(add_column_button);
    }

    public void init(){
        ui_init();
        this.add(panel);
        this.setTitle("Table");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.show();
    }
}
