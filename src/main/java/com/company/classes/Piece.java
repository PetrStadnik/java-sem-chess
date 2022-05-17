package com.company.classes;

import com.company.gui.Square;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Piece {

    private boolean color;
    public Point position;
    public BufferedImage pieceImage;
    public Point[] moves;
    public int repeatMove;
    public boolean alreadyMoved;
    public Square currentSquare;

    public Piece(boolean color, Point position, String pieceImage, Point[] moves, int repeatMove){
        this.color =color;
        this.position = position;
        this.moves = moves;
        this.repeatMove = repeatMove;
        this.alreadyMoved = false;

        try {
            if(color) {
                this.pieceImage = ImageIO.read(new File("src/main/java/com/company/images/w"+pieceImage));
            }
            else {
                this.pieceImage = ImageIO.read(new File("src/main/java/com/company/images/b"+pieceImage));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public List<Point> getLegalMoves(Piece[][] board){
        List<Point> legalMoves = new ArrayList<>();
        for (Point m:moves) {
            int i = 0;
            while(i<repeatMove) {
                i++;
                try {
                    if (board[position.x + m.x*i][position.y + m.y*i] == null) {
                        legalMoves.add(new Point(position.x + m.x*i,position.y + m.y*i));
                    }
                    else if (board[position.x + m.x*i][position.y + m.y*i].isWhite() != this.color){
                        legalMoves.add(new Point(position.x + m.x*i,position.y + m.y*i));
                        i = repeatMove;
                    }
                    else{
                        i = repeatMove;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return legalMoves;
    }


    public boolean isWhite(){
        return color;
    }


    public Square getPosition() {
        return currentSquare;
    }

    public void setPosition(Square sq) {
        this.currentSquare = sq;
    }

    public int getColor() {
        if (color)
            return 1;
        else
            return 0;
    }

    public Image getImage() {
        return pieceImage;
    }

    public void draw(Graphics g) {
        int x = currentSquare.getX();
        int y = currentSquare.getY();

        g.drawImage(this.pieceImage, x, y, null);
    }

    public boolean move(Square fin) {
        Piece occup = fin.getOccupyingPiece();
        if (occup != null) {
            fin.capture(this);
        }
        currentSquare.removePiece();
        this.currentSquare = fin;
        currentSquare.put(this);
        return true;
    }
}
