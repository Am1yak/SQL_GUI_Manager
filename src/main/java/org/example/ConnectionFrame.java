package org.example;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A Swing-based GUI frame that allows the user to input database connection parameters,
 * attempt a connection, and initialize the main table interface on success.
 */
public class ConnectionFrame extends JFrame {
    /** Panel that contains the UI components */
    private JPanel panel = new JPanel();
    private ConnectionsManager connectionsManager = new ConnectionsManager();
    private Connection con;

    /** Default database connection */
    String userName = "root";
    String password = "9692";
    String serverName = "localhost";
    int portNumber = 3306;
    String databaseName = "new_schema";

    /**
     * Initializes the UI components for inputting connection parameters and
     * adds the "Connect" button to establish a database connection.
     */
    private void ui_init(){
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel info_label = new JLabel("Connect to DB");
        panel.add(info_label);

        JLabel username_label = new JLabel("Username");
        panel.add(username_label);
        JTextArea user_text = new JTextArea();
        panel.add(user_text);
        user_text.setText(userName);

        JLabel password_label = new JLabel("Password");
        panel.add(password_label);
        JTextArea password_text = new JTextArea();
        panel.add(password_text);
        password_text.setText(password);

        JLabel server_label = new JLabel("Server Name");
        panel.add(server_label);
        JTextArea server_text = new JTextArea();
        panel.add(server_text);
        server_text.setText(serverName);

        JLabel port_label = new JLabel("Port Number");
        panel.add(port_label);
        JTextArea port_text = new JTextArea();
        panel.add(port_text);
        port_text.setText(String.valueOf(portNumber));

        JLabel database_label = new JLabel("Database Name");
        panel.add(database_label);
        JTextArea database_text = new JTextArea();
        panel.add(database_text);
        database_text.setText(databaseName);

        /**
         * Handles the click event for the "Connect" button.
         * Gathers user input, sets it into the ConectionsManager, attempts connection,
         * and opens a new TableFrame on success.
         *
         * @param e the event object
         */
        JButton connect_button = new JButton("Connect");
        connect_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectionsManager.databaseName = database_text.getText();
                connectionsManager.userName = user_text.getText();
                connectionsManager.password = password_text.getText();
                connectionsManager.serverName = server_text.getText();
                connectionsManager.portNumber = portNumber = Integer.parseInt(port_text.getText());

                try{
                    con = connectionsManager.getConnection();
                    TableFrame frame = new TableFrame(con);
                    frame.init();
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        panel.add(connect_button);
    }

    /**
     * Initializes the full JFrame UI and displays the window.
     */
    public void init(){
        ui_init();
        this.add(panel);
        this.setTitle("Connection Manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(200, 500);
        this.setVisible(true);
    }
}
