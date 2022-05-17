package com.company.classes;

import java.awt.*;

public class Queen extends Piece{

    public Queen(boolean color, Point position){

        super(color, position, "queen.png", new Point[]{
                new Point(0, 1),
                new Point(1,1),
                new Point(1, 0),
                new Point(-1,-1),
                new Point(0, -1),
                new Point(-1,0),
                new Point(1, -1),
                new Point(-1,1)
        }, 8);
    }
}
