package org.example;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * Entry point for the application.
 * Initializes and displays the database connection UI.
 */
public class Main {
    public static void main(String[] args) {
        ConnectionFrame con_frame = new ConnectionFrame();
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        // Initialize and show the connection manager frame
        con_frame.init();
    }
}