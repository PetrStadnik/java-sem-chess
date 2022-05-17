package com.company;

import com.company.gui.StartMenu;

import javax.swing.*;

public class Main implements Runnable {

    public void run() {
        SwingUtilities.invokeLater(new StartMenu());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main());
    }
}
