package com.dragovorn.statproject.render;

import com.dragovorn.statproject.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RenderPane extends JPanel implements ActionListener, KeyListener {

    RenderPane() {
        Timer timer = new Timer(1000 / 60, this);
        timer.start();

        this.setFocusable(true);
        this.addKeyListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        repaint();

        Game.getInstance().tick();

        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Game.getInstance().draw(g);
    }

    @Override
    public void keyTyped(KeyEvent event) { /* UNUSED */ }

    @Override
    public void keyPressed(KeyEvent event) {
        Game.getInstance().keyPressed(event);
    }

    @Override
    public void keyReleased(KeyEvent event) { /* UNUSED */ }
}