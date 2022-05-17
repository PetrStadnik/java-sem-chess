package com.company.classes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{

    public Pawn(boolean color, Point position){

        super(color, position, "pawn.png", new Point[]{new Point(0, 1)}, 2);
    }

    @Override
    public List<Point> getLegalMoves(Piece[][] board) {

        List<Point> legalMoves = new ArrayList<Point>();

        if (isWhite()) {
            try {
                if (board[position.x][position.y+1] == null) {
                    legalMoves.add(new Point(position.x, position.y+1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (!alreadyMoved) {
                    if (board[position.x][position.y + 2] == null) {
                        legalMoves.add(new Point(position.x, position.y+2));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (board[position.x + 1][position.y + 1] != null && board[position.x+1][position.y+1].isWhite() != this.isWhite()) {
                    legalMoves.add(new Point(position.x + 1, position.y + 1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (board[position.x - 1][position.y + 1] != null && board[position.x-1][position.y+1].isWhite() != this.isWhite()) {
                    legalMoves.add(new Point(position.x - 1, position.y + 1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }

        }
        else{
            try {
                if (board[position.x][position.y-1] == null) {
                    legalMoves.add(new Point(position.x, position.y-1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (!alreadyMoved) {
                    if (board[position.x][position.y - 2] == null) {
                        legalMoves.add(new Point(position.x, position.y-2));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (board[position.x + 1][position.y - 1] != null && board[position.x+1][position.y-1].isWhite() != this.isWhite()) {
                    legalMoves.add(new Point(position.x + 1, position.y - 1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            try {
                if (board[position.x - 1][position.y - 1] != null && board[position.x-1][position.y-1].isWhite() != this.isWhite()) {
                    legalMoves.add(new Point(position.x - 1, position.y - 1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return legalMoves;
    }
}
