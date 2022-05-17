package com.company.classes;

import java.awt.*;

public class Rook extends Piece{

    public Rook(boolean color, Point position){

        super(color, position, "rook.png", new Point[]{
                new Point(0, 1),
                new Point(1, 0),
                new Point(0, -1),
                new Point(-1,0),
        }, 8);
    }
}
