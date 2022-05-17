package com.company.classes;

import com.company.classes.*;
import com.company.gui.GameWindow;
import com.company.gui.Square;

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
import java.util.Random;
import javax.swing.*;

public class Board{

    public Piece[][] board;
    private final GameWindow g;
    public Boolean onePlayer;

    public List<Piece> Bpieces;
    public List<Piece> Wpieces;

    public Piece wKing;
    public Piece bKing;

    private boolean whiteTurn;


    public Piece lastMoved;
    public Point lastMovedFrom;
    public boolean wasItsFirst;
    public Piece lastRemoved;

    private int currX;
    private int currY;

    public CheckCheckmate checkCheckmate;

    public Board(GameWindow g, boolean onePlayer) {
        checkCheckmate = new CheckCheckmate();
        this.onePlayer = onePlayer;
        whiteTurn = true;
        this.g = g;

        board = new Piece[8][8];
        Bpieces = new ArrayList<Piece>();
        Wpieces = new ArrayList<Piece>();
        newGame();



    }

    private void newGame() {
        for (int x = 0; x < 8; x++) {
            Wpieces.add(new Pawn(true, new Point(x, 1)));
            Bpieces.add(new Pawn(false, new Point(x, 6)));
        }
        Wpieces.add(new Rook(true, new Point(0, 0)));
        Wpieces.add(new Rook(true, new Point(7, 0)));
        Bpieces.add(new Rook(false, new Point(0, 7)));
        Bpieces.add(new Rook(false, new Point(7, 7)));

        Wpieces.add(new Knight(true, new Point(1, 0)));
        Wpieces.add(new Knight(true, new Point(6, 0)));
        Bpieces.add(new Knight(false, new Point(1, 7)));
        Bpieces.add(new Knight(false, new Point(6, 7)));

        Wpieces.add(new Bishop(true, new Point(2, 0)));
        Wpieces.add(new Bishop(true, new Point(5, 0)));
        Bpieces.add(new Bishop(false, new Point(2, 7)));
        Bpieces.add(new Bishop(false, new Point(5, 7)));

        Wpieces.add(new Queen(true, new Point(3, 0)));
        Bpieces.add(new Queen(false, new Point(3, 7)));

        wKing = new King(true, new Point(4, 0));
        bKing = new King(false, new Point(4, 7));
        Wpieces.add(wKing);
        Bpieces.add(bKing);

        for (Piece p:Wpieces){
            board[p.position.x][p.position.y] = p;
        }
        for (Piece p:Bpieces){
            board[p.position.x][p.position.y] = p;
        }
    }

    public void move(Piece piece, Point where){
        lastMoved = piece;
        lastMovedFrom = new Point(piece.position.x, piece.position.y);
        if(piece.alreadyMoved){
            wasItsFirst = false;
        }
        else {
            wasItsFirst = true;
            piece.alreadyMoved = true;
        }
        board[piece.position.x][piece.position.y] = null;
        if(board[where.x][where.y]!=null){
            lastRemoved = board[where.x][where.y];
            if(board[where.x][where.y].isWhite()){
                Wpieces.remove(board[where.x][where.y]);
            }
            else
                Bpieces.remove(board[where.x][where.y]);
        }
        else {
            lastRemoved = null;
        }
        lastMoved.position = where;
        piece.position = where;
        board[where.x][where.y] = piece;
        whiteTurn =!whiteTurn;
    }

    public void undoMove(){
        //System.out.println("UNDO");
        if (lastRemoved !=null) {
            board[lastRemoved.position.x][lastRemoved.position.y] = lastRemoved;
            if(lastRemoved.isWhite()) {
                Wpieces.add(lastRemoved);
            }
            else{
                Bpieces.add(lastRemoved);
            }
        }
        else {
            board[lastMoved.position.x][lastMoved.position.y] = null;
        }
        lastMoved.alreadyMoved = !wasItsFirst;
        lastMoved.position = lastMovedFrom;
        board[lastMovedFrom.x][lastMovedFrom.y] = lastMoved;

        whiteTurn =!whiteTurn;
    }

    public Piece computerPlay() {
        Random rand = new Random();
        int s = 0;
        Piece p = null;
        while(s < 1) {
            p = Bpieces.get(rand.nextInt(Bpieces.size()));
            s = p.getLegalMoves(board).size();
        }
        move(p, p.getLegalMoves(board).get(rand.nextInt(s)));

        while(checkCheckmate.isInCheck(this, false)){
            undoMove();
            s = 0;
            while(s < 1) {
                p = Bpieces.get(rand.nextInt(Bpieces.size()));
                s = p.getLegalMoves(board).size();
            }
            move(p, p.getLegalMoves(board).get(rand.nextInt(s)));
        }
        return p;
    }

    public boolean castlingBig(boolean white){
        if(white){
            if(!wKing.alreadyMoved && board[0][0]!= null && board[1][0]== null && board[2][0]== null && board[3][0]== null && !board[0][0].alreadyMoved){
                move(wKing, new Point(3,0));
                if(checkCheckmate.isInCheck(this, true)){
                    move(wKing, new Point(4,0));
                    whiteTurn = true;
                    wKing.alreadyMoved = false;
                    return false;
                }
                move(wKing, new Point(2,0));
                if(checkCheckmate.isInCheck(this, true)){
                    move(wKing, new Point(4,0));
                    whiteTurn = true;
                    wKing.alreadyMoved = false;
                    return false;
                }
                move(board[0][0], new Point(3,0));
                whiteTurn = false;
                return true;
            }
        }
       else {
            if(!bKing.alreadyMoved && board[0][7]!= null && board[1][7]== null && board[2][7]== null && board[3][7]== null && !board[0][7].alreadyMoved){
                move(bKing, new Point(3,7));
                if(checkCheckmate.isInCheck(this, false)){
                    move(bKing, new Point(4,7));
                    whiteTurn = false;
                    bKing.alreadyMoved = false;
                    return false;
                }
                move(bKing, new Point(2,7));
                if(checkCheckmate.isInCheck(this, false)){
                    move(bKing, new Point(4,7));
                    whiteTurn = false;
                    bKing.alreadyMoved = false;
                    return false;
                }
                move(board[0][7], new Point(3,7));
                whiteTurn = true;
                return true;
            }
        }
       return false;
    }

    public boolean castlingSmall(boolean white){
        if(white){
            if(!wKing.alreadyMoved && board[7][0] != null && board[6][0] == null && board[5][0] == null && !board[7][0].alreadyMoved){
                move(wKing, new Point(5,0));
                if(checkCheckmate.isInCheck(this, true)){
                    move(wKing, new Point(4,0));
                    whiteTurn = true;
                    wKing.alreadyMoved = false;
                    return false;
                }
                move(wKing, new Point(6,0));
                if(checkCheckmate.isInCheck(this, true)){
                    move(wKing, new Point(4,0));
                    whiteTurn = true;
                    wKing.alreadyMoved = false;
                    return false;
                }
                move(board[7][0], new Point(5,0));
                whiteTurn = false;
                return true;
            }
        }
        else {
            if(!bKing.alreadyMoved && board[7][7]!= null && board[6][7]== null && board[5][7]== null  && !board[7][7].alreadyMoved){
                move(bKing, new Point(5,7));
                if(checkCheckmate.isInCheck(this, false)){
                    move(bKing, new Point(4,7));
                    whiteTurn = false;
                    bKing.alreadyMoved = false;
                    return false;
                }
                move(bKing, new Point(6,7));
                if(checkCheckmate.isInCheck(this, false)){
                    move(bKing, new Point(4,7));
                    whiteTurn = false;
                    bKing.alreadyMoved = false;
                    return false;
                }
                move(board[7][7], new Point(5,7));
                whiteTurn = true;
                return true;
            }
        }
        return false;
    }





}



