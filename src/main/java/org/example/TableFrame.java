package org.example;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

/**
 * A Swing frame that allows a user to view and interact with a database table.
 * The table is editable, and changes can be pushed back to the database.
 * Functionality includes adding/removing rows and columns, and switching tables.
 */
public class TableFrame extends JFrame {
    private JPanel panel = new JPanel();
    private Connection con;
    private String tableName;
    private DefaultTableModel model;
    private JTable gui_table;

    /**
     * Constructs a TableFrame instance with an existing database connection.
     * Prompts the user to input the name of the database table to interact with.
     *
     * @param con a valid SQL database connection
     */
    public TableFrame(Connection con) {
        this.con = con;
        this.tableName = JOptionPane.showInputDialog(null, "Enter the table name:", "Table Name", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Initializes all UI components: toolbar, table, buttons, and listeners.
     */
    private void ui_init(){
        JToolBar toolBar = new JToolBar();
        JButton switch_database_button = new JButton("Switch Database");
        TableFrame mainFrame = this;

        // Switch database (recreate the frame with the same connection)
        switch_database_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TableFrame frame = new TableFrame(con);
                frame.init();
                mainFrame.dispose();
            }
        });
        toolBar.add(switch_database_button);

        // Update database from GUI table
        JButton update_database_button = new JButton("Update Database");
        update_database_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update_database();
            }
        });
        toolBar.add(update_database_button);
        panel.add(toolBar);

        // Form table from database
        TableInfo table = null;
        SQLHandler handler = new SQLHandler(con, tableName);
        try {
            table = handler.form_table();
        } catch (SQLException e){
            e.printStackTrace();
        }

        gui_table = handler.getTable(table);
        model = (DefaultTableModel) gui_table.getModel();
        JScrollPane pane = new JScrollPane(gui_table);
        panel.add(pane);

        JPanel bot_panel = new JPanel();
        bot_panel.setLayout(new BoxLayout(bot_panel, BoxLayout.X_AXIS));

        // Track changes and update database on cell edit
        model.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if(e.getType() == TableModelEvent.UPDATE && !(e.getColumn() == -1)){
                    int row = e.getFirstRow();
                    int col = e.getColumn();
                    String str_row = model.getValueAt(row, col).toString();
                    String str_col = model.getColumnName(col).toString();
                    String ck_colomn = model.getColumnName(0).toString();
                    String ck_row = model.getValueAt(row, 0).toString();
                    try {
                        handler.save_value(str_col, str_row, ck_colomn, ck_row);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Add row button
        JButton add_row_button = new JButton("Add Row");
        add_row_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = return_id();
                model.addRow(new Object[]{});
                model.setValueAt(id, model.getRowCount()-1, 0);
                handler.insert_row(id, model.getColumnName(0));
                update_database();
            }
        });
        bot_panel.add(add_row_button);

        // Add column button
        JButton add_column_button = new JButton("Add Column");
        add_column_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String col_name = JOptionPane.showInputDialog(null, "Enter the column name:", "Column Name", JOptionPane.PLAIN_MESSAGE);
                String[] classes = {"String", "Integer", "Float"};
                int int_class = JOptionPane.showOptionDialog(null, "Choose colomn class", "Column class", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, classes, classes[0]);
                model.addColumn(col_name);
                handler.add_colomn(col_name, int_class);
            }
        });
        bot_panel.add(add_column_button);

        // Remove row button
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

        // Remove column button
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

    /**
     * Returns a new unique ID for insertion by checking existing ID values.
     * Assumes IDs are integers starting from 1 and gapless.
     *
     * @return a new unique ID
     */
    private int return_id(){
        int rows = model.getRowCount();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < rows; i++) {
            ids.add(Integer.parseInt((String) model.getValueAt(i, 0)));
        }

        ids.sort(null);
        int len = ids.size();
        int i = 0;
        while ((i<len) && ((i+1) == ids.get(i))){
            i++;
        }
        return i+1;
    }

    /**
     * Re-reads the table from the database and updates the GUI table.
     */
    private void update_database(){
        TableInfo table = null;
        SQLHandler handler = new SQLHandler(con, tableName);
        try {
            table = handler.form_table();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        JTable update_table = handler.getTable(table);
        gui_table.setModel(update_table.getModel());
    }

    /**
     * Initializes the frame and shows it.
     */
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
