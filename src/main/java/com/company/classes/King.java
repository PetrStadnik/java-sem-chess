package com.company.classes;

import java.awt.*;

public class King extends Piece{

    public King(boolean color, Point position){

        super(color, position, "king.png", new Point[]{
                new Point(0, 1),
                new Point(1,1),
                new Point(1, 0),
                new Point(-1,-1),
                new Point(0, -1),
                new Point(-1,0),
                new Point(1, -1),
                new Point(-1,1)
        }, 1);
    }
}