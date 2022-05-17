package com.company.gui;

import com.company.classes.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GameWindow extends JPanel{
    private JFrame gameWindow;

    public StopWatch blackClock;
    public StopWatch whiteClock;


    private  Board bor;
    private GBoard gboard;

    public JLabel bTime;
    public JLabel wTime;

    public boolean withTime;



    public GameWindow(int hh, int mm, int ss, boolean onePlayer) {
        bTime = new JLabel();
        wTime = new JLabel();

        blackClock = new StopWatch(60*60000*(hh)+60000*mm+1000*ss, bTime, this, 0);
        whiteClock = new StopWatch(60*60000*(hh)+60000*mm+1000*ss, wTime, this, 1);

        gameWindow = new JFrame("Chess");


        gameWindow.setLocation(500, 500);


        gameWindow.setLayout(new BorderLayout(20,20));

        JPanel gameData = gameDataPanel(hh, mm, ss);
        gameData.setSize(gameData.getPreferredSize());
        gameWindow.add(gameData, BorderLayout.NORTH);

        this.bor = new Board(this, onePlayer);
        this.gboard = new GBoard(this, bor);


        gameWindow.add(gboard, BorderLayout.CENTER);

        gameWindow.add(buttons(), BorderLayout.SOUTH);

        gameWindow.setMinimumSize(gameWindow.getPreferredSize());
        gameWindow.setSize(gameWindow.getPreferredSize());
        gameWindow.setResizable(false);

        gameWindow.pack();
        gameWindow.setVisible(true);
        gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    private JPanel gameDataPanel(final int hh, final int mm, final int ss) {

        JPanel gameData = new JPanel();
        gameData.setLayout(new GridLayout(3,2,0,0));

        JLabel w = new JLabel("Bílý hráč");
        JLabel b = new JLabel("Černý hráč");

        w.setHorizontalAlignment(JLabel.CENTER);
        w.setVerticalAlignment(JLabel.CENTER);
        b.setHorizontalAlignment(JLabel.CENTER);
        b.setVerticalAlignment(JLabel.CENTER);

        w.setSize(w.getMinimumSize());
        b.setSize(b.getMinimumSize());

        gameData.add(w);
        gameData.add(b);

        bTime.setHorizontalAlignment(JLabel.CENTER);
        bTime.setVerticalAlignment(JLabel.CENTER);
        wTime.setHorizontalAlignment(JLabel.CENTER);
        wTime.setVerticalAlignment(JLabel.CENTER);

        if (!(hh == 0 && mm == 0 && ss == 0)) {
            boolean turn = true;
            withTime = true;
            wTime.setText(whiteClock.getTime());
            bTime.setText(blackClock.getTime());
            whiteClock.start();
        }
        else{
            withTime = false;
        }

        gameData.add(wTime);
        gameData.add(bTime);

        gameData.setPreferredSize(gameData.getMinimumSize());

        return gameData;
    }

    private JPanel buttons() {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3, 10, 0));

        final JButton quit = new JButton("Opustit hru");

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindow,
                        "Opravdu chcete hru opustit?",
                        "Opustit?", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    whiteClock.stop();
                    blackClock.stop();
                    gameWindow.dispose();
                }
            }
        });

        final JButton nGame = new JButton("Nová hra");

        nGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int n = JOptionPane.showConfirmDialog(
                        gameWindow,
                        "Jste si jisti, že chcete začít novou hru?",
                        "Nová hra?", JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    SwingUtilities.invokeLater(new StartMenu());
                    whiteClock.stop();
                    blackClock.stop();
                    gameWindow.dispose();
                }
            }
        });

        final JButton instr = new JButton("Nápověda");

        instr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(gameWindow,
                        "Stisněte ukazatel myši nad figurkou a táhněte \n"
                                + "s ní na pole, kam chcete figurku umístit.\n"
                                + "Pokud tah není legální, figurka se vrátí zpět\n",
                        "Nápověda",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });

        buttons.add(instr);
        buttons.add(nGame);
        buttons.add(quit);

        buttons.setPreferredSize(buttons.getMinimumSize());

        return buttons;
    }

    public void checkmateOccurred (int c) {

        whiteClock.stop();
        blackClock.stop();
        int n;
        if (c == 0) {
            n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "Bílý vyhrál!\n",
                    "Šach mat!",
                    JOptionPane.DEFAULT_OPTION);
        } else {
            n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "Černý vyhrál!\n",
                    "Šach mat!",
                    JOptionPane.DEFAULT_OPTION);
        }
        if (n == JOptionPane.OK_OPTION) {
            SwingUtilities.invokeLater(new StartMenu());
            gameWindow.dispose();
        }
    }

    public void timeEnded (int c) {

        whiteClock.stop();
        blackClock.stop();
        int n;
        if (c == 0) {
            n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "Čas vypršel, bílý vyhrál!\n",
                    "Bílý vyhrál!",
                    JOptionPane.DEFAULT_OPTION);
        } else {
            n = JOptionPane.showConfirmDialog(
                    gameWindow,
                    "Čas vypršel, černý vyhrál!\n",
                    "Černý vyhrál!",
                    JOptionPane.DEFAULT_OPTION);
        }
        if (n == JOptionPane.OK_OPTION) {
            SwingUtilities.invokeLater(new StartMenu());
            gameWindow.dispose();
        }
    }


    public Piece showPawnChange (Piece piece) {

        String[] options = {"Dáma", "Střelec", "Kůň", "Věž"};

        int x = JOptionPane.showOptionDialog(null, "Došli jste pěšákem na konec, vyberte na co jej chcete proměnit.",
                "Vyberte",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        //System.out.println(x);
        Piece newPiece;
        switch (x){
            case 0:
                newPiece = new Queen(piece.isWhite(), piece.position);
                break;
            case 1:
                newPiece = new Bishop(piece.isWhite(), piece.position);
                break;
            case 2:
                newPiece = new Knight(piece.isWhite(), piece.position);
                break;
            default:
                newPiece = new Rook(piece.isWhite(), piece.position);
                break;
        }

        if(piece.isWhite())
        {
            bor.Wpieces.remove(piece);
            bor.Wpieces.add(newPiece);
        }
        else {
            bor.Bpieces.remove(piece);
            bor.Bpieces.add(newPiece);
        }
        return newPiece;
    }
}

