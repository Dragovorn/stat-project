package com.dragovorn.statproject.render;

import javax.swing.*;
import java.awt.*;

public class ProjectWindow {

    private JFrame frame;

    public ProjectWindow() {
        Dimension dimension = new Dimension(500, 500);

        this.frame = new JFrame("Stat Project");
        this.frame.setSize(dimension);
        this.frame.setMinimumSize(dimension);
        this.frame.setMaximumSize(dimension);
        this.frame.setPreferredSize(dimension);
        this.frame.setResizable(false);
        this.frame.setContentPane(new RenderPane());
        this.frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - dimension.width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - dimension.height / 2);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setAlwaysOnTop(false);
        this.frame.setVisible(true);
    }

    public JFrame getFrame() {
        return this.frame;
    }
}