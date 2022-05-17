package com.company.classes;

import java.awt.*;

public class CheckCheckmate {

    public CheckCheckmate(){}


    public boolean isInCheck(Board boardClass, boolean color){
        if(color){
            for(Piece piece: boardClass.Bpieces)
            {
                //System.out.println(piece.getClass().getSimpleName());
                for (Point p: piece.getLegalMoves(boardClass.board)){
                    if (boardClass.wKing.position.equals(p)){
                        if (!piece.getClass().getSimpleName().equals("Pawn") || p.x != 0) {
                            return true;
                        }
                    }
                }
            }
        }
        else {
            for(Piece piece: boardClass.Wpieces)
            {
                for (Point p: piece.getLegalMoves(boardClass.board)){
                    if (boardClass.bKing.position.equals(p)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmated(Board boardClass, boolean color){
        Piece lm =null;
        Point lmf = null;
        if(boardClass.lastMovedFrom !=null) {
            lm = boardClass.lastMoved;
            lmf = new Point(boardClass.lastMovedFrom.x, boardClass.lastMovedFrom.y);
        }
        if(color){
            for(Piece piece: boardClass.Wpieces)
            {
                for (Point p: piece.getLegalMoves(boardClass.board)){
                    boardClass.move(piece, p);
                    if (!isInCheck(boardClass, color)){
                        boardClass.undoMove();
                        boardClass.lastMoved = lm;
                        boardClass.lastMovedFrom = lmf;
                        return false;
                    }
                    boardClass.undoMove();
                }
            }
        }
        else {
            for(Piece piece: boardClass.Bpieces)
            {
                for (Point p: piece.getLegalMoves(boardClass.board)){
                    boardClass.move(piece, p);
                    if (!isInCheck(boardClass, color)){
                        boardClass.undoMove();
                        boardClass.lastMoved = lm;
                        boardClass.lastMovedFrom = lmf;
                        return false;
                    }
                    boardClass.undoMove();
                }
            }
        }
        return true;
    }
}