package com.company.gui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.*;

public class StartMenu implements Runnable {
    public void run() {
        final JFrame startWindow = new JFrame("Chess");

        startWindow.setLocation(500,300);
        startWindow.setResizable(false);
        startWindow.setSize(260, 240);

        final String[] minSecInts = new String[60];
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minSecInts[i] = "0" + Integer.toString(i);
            } else {
                minSecInts[i] = Integer.toString(i);
            }
        }

        final JComboBox<String> seconds = new JComboBox<String>(minSecInts);
        final JComboBox<String> minutes = new JComboBox<String>(minSecInts);
        final JComboBox<String> hours =
                new JComboBox<String>(new String[] {"0","1","2","3"});

        Box timerSettings = Box.createHorizontalBox();

        hours.setMaximumSize(hours.getPreferredSize());
        minutes.setMaximumSize(minutes.getPreferredSize());
        seconds.setMaximumSize(seconds.getPreferredSize());
        timerSettings.add(Box.createHorizontalStrut(30));
        timerSettings.add(hours);
        timerSettings.add(Box.createHorizontalStrut(30));
        timerSettings.add(minutes);
        timerSettings.add(Box.createHorizontalStrut(30));
        timerSettings.add(seconds);

        timerSettings.add(Box.createVerticalGlue());



        JPanel buttons = new JPanel();
        GridLayout gL = new GridLayout(5,1);
        buttons.setLayout(gL);
        final JButton quit = new JButton("Quit");

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startWindow.dispose();
            }
        });


        final JButton startPc = new JButton("Hra proti počítači");

        startPc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int hh = Integer.parseInt((String) hours.getSelectedItem());
                int mm = Integer.parseInt((String) minutes.getSelectedItem());
                int ss = Integer.parseInt((String) seconds.getSelectedItem());

                new GameWindow(hh, mm, ss, true);
                startWindow.dispose();
            }
        });

        final JButton start = new JButton("Hra dva hráči");

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int hh = Integer.parseInt((String) hours.getSelectedItem());
                int mm = Integer.parseInt((String) minutes.getSelectedItem());
                int ss = Integer.parseInt((String) seconds.getSelectedItem());

                new GameWindow(hh, mm, ss, false);
                startWindow.dispose();
            }
        });

        buttons.add(startPc);
        buttons.add(start);
        JLabel text = new JLabel("<html>Chcete-li hrát s časovým <br>limitem, nastavte ho níže:</html>");
        buttons.add(text);
        buttons.add(timerSettings);
        buttons.add(quit);
        startWindow.add(buttons);



        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setVisible(true);
    }
}

