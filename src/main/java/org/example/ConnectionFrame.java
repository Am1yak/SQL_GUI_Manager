package org.example;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFrame extends JFrame {
    JPanel panel = new JPanel();
    ConectionsManager conectionsManager = new ConectionsManager();
    Connection con;

    String userName = "root";
    String password = "9692";
    String serverName = "localhost";
    int portNumber = 3306;
    String databaseName = "new_schema";

    public void conectionTest(){
        String sql = "select Person, age, IQ from Persons";
        try(Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                String name = rs.getString("Person");
                int age = rs.getInt("age");
                int iq = rs.getInt("IQ");
                System.out.println("Name: " + name + ", Age: " + age + ", IQ: " + iq);
            }
        } catch (SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public void ui_init(){
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

        JButton connect_button = new JButton("Connect");
        connect_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                conectionsManager.databaseName = database_text.getText();
                conectionsManager.userName = user_text.getText();
                conectionsManager.password = password_text.getText();
                conectionsManager.serverName = server_text.getText();
                conectionsManager.portNumber = portNumber = Integer.parseInt(port_text.getText());

                try{
                    con = conectionsManager.getConnection();
                    TableFrame frame = new TableFrame(con);
                    frame.init();
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }

                conectionTest();
            }
        });
        panel.add(connect_button);
    }

    public void init(){
        ui_init();
        this.add(panel);
        this.setTitle("Connection Manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(200, 500);
        this.setVisible(true);
    }
}
