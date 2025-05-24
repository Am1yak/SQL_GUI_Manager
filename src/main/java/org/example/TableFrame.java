package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
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
    DefaultTableModel model;

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
        SQLHandler handler = new SQLHandler(con, tableName);
        try {
            table = handler.form_table();
        } catch (SQLException e){
            e.printStackTrace();
        }

        JTable gui_table = handler.getTable(table);
        model = (DefaultTableModel) gui_table.getModel();
        JScrollPane pane = new JScrollPane(gui_table);
        panel.add(pane);

        JPanel bot_panel = new JPanel();
        bot_panel.setLayout(new BoxLayout(bot_panel, BoxLayout.X_AXIS));

        JButton save_button = new JButton("Save");
        save_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        bot_panel.add(save_button);

        JButton add_row_button = new JButton("Add Row");
        add_row_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.addRow(new Object[]{});
            }
        });
        bot_panel.add(add_row_button);

        JButton add_column_button = new JButton("Add Column");
        add_column_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String col_name = JOptionPane.showInputDialog(null, "Enter the column name:", "Column Name", JOptionPane.PLAIN_MESSAGE);
                model.addColumn(col_name);
                handler.add_colomn(col_name);
            }
        });
        bot_panel.add(add_column_button);

        JButton remove_row_button = new JButton("Remove Row");
        remove_row_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int sel_row = gui_table.getSelectedRow();
                String row_name = gui_table.getValueAt(sel_row, 0).toString();
                handler.remove_row(gui_table.getColumnName(0), row_name);
                model.removeRow(sel_row);
            }
        });
        bot_panel.add(remove_row_button);

        JButton remove_column_button = new JButton("Remove Column");
        remove_column_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int sel_coln = gui_table.getSelectedColumn();
                String col_name = gui_table.getColumnName(sel_coln);
                TableColumn column = gui_table.getColumnModel().getColumn(sel_coln);
                handler.remove_colomn(col_name);
                gui_table.removeColumn(column);
            }
        });
        bot_panel.add(remove_column_button);
        panel.add(bot_panel);
    }

    public void save_func(){

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
