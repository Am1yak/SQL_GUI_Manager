package org.example;

import javax.swing.*;
import java.awt.*;

public class TableFrame extends JFrame {
    JPanel panel = new JPanel();

    public void init(){
        this.setTitle("Table");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    }

}
