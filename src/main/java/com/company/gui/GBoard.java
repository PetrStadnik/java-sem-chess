package com.company.gui;

import com.company.classes.Board;
import com.company.classes.Piece;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

@SuppressWarnings("serial")
public class GBoard extends JPanel implements MouseListener, MouseMotionListener {

    private static final Logger logger= Logger.getLogger(GBoard.class.getName());

    private final Square[][] board;
    private final GameWindow g;


    private boolean whiteTurn;

    private Piece currPiece;
    private int currX;
    private int currY;
    private Board bor;


    public GBoard(GameWindow g, Board bor) {
        logger.setLevel(Level.ALL);
        this.bor= bor;
        this.g = g;
        board = new Square[8][8];
        setLayout(new GridLayout(8, 8, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int xMod = x % 2;
                int yMod = y % 2;

                if ((xMod == 0 && yMod == 0) || (xMod == 1 && yMod == 1)) {
                    board[x][y] = new Square(this, 1, x, y);
                    this.add(board[x][y]);
                } else {
                    board[x][y] = new Square(this, 0, x, y);
                    this.add(board[x][y]);
                }
            }
        }

        initializePieces();

        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));

        whiteTurn = true;

    }

    private void initializePieces() {
        for(Piece p:bor.Wpieces) {
            board[p.position.x][p.position.y].put(p);
        }
        for(Piece p:bor.Bpieces) {
            board[p.position.x][p.position.y].put(p);
        }

    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return whiteTurn;
    }

    public void setCurrPiece(Piece p) {
        this.currPiece = p;
    }

    public Piece getCurrPiece() {
        return this.currPiece;
    }

    @Override
    public void paintComponent(Graphics g) {
        // super.paintComponent(g);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = board[y][x];
                sq.paintComponent(g);
            }
        }

        if (currPiece != null) {
            if ((currPiece.getColor() == 1 && whiteTurn)
                    || (currPiece.getColor() == 0 && !whiteTurn)) {
                final Image i = currPiece.getImage();
                g.drawImage(i, currX, currY, null);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currX = e.getX();
        currY = e.getY();

        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (sq.isOccupied()) {
            currPiece = sq.getOccupyingPiece();
            if (currPiece.getColor() == 0 && whiteTurn)
                return;
            if (currPiece.getColor() == 1 && !whiteTurn)
                return;
            sq.setDisplay(false);
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (currPiece != null) {
            if (currPiece.getColor() == 0 && whiteTurn)
                return;
            if (currPiece.getColor() == 1 && !whiteTurn)
                return;

            List<Point> legalMovesPoints = currPiece.getLegalMoves(bor.board);
            List<Square> legalMoves=new ArrayList<>();
            for(Point p:legalMovesPoints)
            {
                logger.info("Getting legalMoves");
                legalMoves.add(board[p.x][p.y]);
            }

            Point passantPoint = passant();
            if(passantPoint != null){legalMoves.add(board[passantPoint.x][passantPoint.y]);}

            if (legalMoves.contains(sq)) {
                logger.info("Can try move");
                bor.move(currPiece, new Point(sq.getXNum(), sq.getYNum()));
                if (bor.checkCheckmate.isInCheck(bor, currPiece.isWhite())) {
                    logger.severe("Is in check!");
                    bor.undoMove();
                    currPiece.getPosition().setDisplay(true);
                    currPiece = null;
                } else {
                    logger.info("Is not in check!");
                    sq.setDisplay(true);
                    currPiece.move(sq);
                    whiteTurn = !whiteTurn;
                    if (currPiece.getClass().getSimpleName().equals("Pawn")&& ((currPiece.isWhite() && currPiece.position.y == 7) || (!currPiece.isWhite() && currPiece.position.y == 0))){
                        sq.removePiece();
                        sq.put(g.showPawnChange(currPiece));
                        sq.setDisplay(true);
                    }
                    if (passantPoint !=null && currPiece.getClass().getSimpleName().equals("Pawn")){
                        if(sq.getXNum() == passantPoint.x && sq.getYNum() == passantPoint.y){
                            if (currPiece.isWhite()){
                                logger.info("White pasant");
                                board[currPiece.position.x][currPiece.position.y-1].removePiece();
                                bor.Bpieces.remove(bor.board[currPiece.position.x][currPiece.position.y-1]);
                                bor.board[currPiece.position.x][currPiece.position.y-1] = null;
                            }
                            else{
                                logger.info("Black passant");
                                board[currPiece.position.x][currPiece.position.y+1].removePiece();
                                bor.Bpieces.remove(bor.board[currPiece.position.x][currPiece.position.y+1]);
                                bor.board[currPiece.position.x][currPiece.position.y+1] = null;
                            }
                        }
                    }
                    currPiece = null;
                }
            }
            else if(currPiece.getClass().getSimpleName().equals("King")){
                if(currPiece != null && currPiece.isWhite() && sq.getXNum() == 2 && sq.getYNum() == 0){
                    logger.info("Try castling");
                    if(bor.castlingBig(currPiece.isWhite())){
                        logger.info("castling successfully done");
                        sq.setDisplay(true);
                        currPiece.move(sq);
                        board[0][0].getOccupyingPiece().move(board[3][0]);
                        board[3][0].setDisplay(true);
                        whiteTurn = !whiteTurn;
                        currPiece = null;
                    }
                    else {
                        currPiece.getPosition().setDisplay(true);
                        currPiece = null;
                    }
                }

                else if(currPiece != null && currPiece.isWhite() && sq.getXNum() == 6 && sq.getYNum() == 0){
                    logger.info("Try castling");
                    if(bor.castlingSmall(currPiece.isWhite())){
                        logger.info("castling successfully done");
                        sq.setDisplay(true);
                        currPiece.move(sq);
                        board[7][0].getOccupyingPiece().move(board[5][0]);
                        board[5][0].setDisplay(true);
                        whiteTurn = !whiteTurn;
                        currPiece = null;
                    }
                    else {
                        currPiece.getPosition().setDisplay(true);
                        currPiece = null;
                    }
                }

                else if(currPiece != null && !currPiece.isWhite() && sq.getXNum() == 2 && sq.getYNum() == 7){
                    logger.info("Try castling");
                    if(bor.castlingBig(currPiece.isWhite())){
                        logger.info("castling successfully done");
                        sq.setDisplay(true);
                        currPiece.move(sq);
                        board[0][7].getOccupyingPiece().move(board[3][7]);
                        board[3][7].setDisplay(true);
                        whiteTurn = !whiteTurn;
                        currPiece = null;
                    }
                    else {
                        currPiece.getPosition().setDisplay(true);
                        currPiece = null;
                    }
                }

                else if(currPiece != null && !currPiece.isWhite() && sq.getXNum() == 6 && sq.getYNum() == 7){
                    logger.info("Try castling");
                    if(bor.castlingSmall(currPiece.isWhite())){
                        logger.info("castling successfully done");
                        sq.setDisplay(true);
                        currPiece.move(sq);
                        board[7][7].getOccupyingPiece().move(board[5][7]);
                        board[5][7].setDisplay(true);
                        whiteTurn = !whiteTurn;
                        currPiece = null;
                    }
                    else {
                        currPiece.getPosition().setDisplay(true);
                        currPiece = null;
                    }
                }
                else {
                    currPiece.getPosition().setDisplay(true);
                    currPiece = null;
                }

            } else {
                currPiece.getPosition().setDisplay(true);
                currPiece = null;
            }
        }
        repaint();
        if(whiteTurn && g.withTime){
            g.whiteClock.start();
            g.blackClock.stop();
        }else if(g.withTime) {
            g.whiteClock.stop();
            g.blackClock.start();
        }

        if(bor.checkCheckmate.isCheckmated(bor, true)){
            g.checkmateOccurred(1);
        }
        if(bor.checkCheckmate.isCheckmated(bor, false)){
            g.checkmateOccurred(0);
            whiteTurn=true;
        }

        if(bor.onePlayer && !whiteTurn){
            logger.info("Computer is playing");
            Piece p = bor.computerPlay();
            Square sqc = board[p.position.x][p.position.y];
            sqc.setDisplay(true);
            p.move(sqc);
            whiteTurn = !whiteTurn;

            if(whiteTurn && g.withTime){
                g.whiteClock.start();
                g.blackClock.stop();
            }else if(g.withTime) {
                g.whiteClock.stop();
                g.blackClock.start();
            }
            if(bor.checkCheckmate.isCheckmated(bor, true)){
                g.checkmateOccurred(1);
            }
            if(bor.checkCheckmate.isCheckmated(bor, false)){
                g.checkmateOccurred(0);
            }
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currX = e.getX() - 24;
        currY = e.getY() - 24;

        repaint();
    }

    // Irrelevant methods, do nothing for these mouse behaviors
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public Point passant(){
        if (bor.lastMoved == null || !bor.lastMoved.getClass().getSimpleName().equals("Pawn") || 2 != Math.abs(bor.lastMoved.position.y - bor.lastMovedFrom.y)) {
            if (bor.lastMoved != null){logger.info("Passant null "+ bor.lastMoved.position.y +" --- "+ bor.lastMovedFrom.y);}
            return null;
        }
        if(currPiece.position.y == bor.lastMoved.position.y && 1 == Math.abs(bor.lastMoved.position.x - currPiece.position.x)){
            logger.info("Passant in first");
            if(currPiece.isWhite() && bor.board[bor.lastMoved.position.x][bor.lastMoved.position.y+1] == null){
                logger.info("Passant in SECOND");
                return new Point(bor.lastMoved.position.x,bor.lastMoved.position.y+1);
            }
            if(!currPiece.isWhite() && bor.board[bor.lastMoved.position.x][bor.lastMoved.position.y-1] == null){
                logger.info("Passant in SECOND");
                return new Point(bor.lastMoved.position.x,bor.lastMoved.position.y-1);
            }
        }
        return null;
    }

}