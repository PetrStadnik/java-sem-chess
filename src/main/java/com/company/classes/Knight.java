
package com.company.classes;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Knight extends Piece{

    public Knight(boolean color, Point position){

        super(color, position, "knight.png", new Point[]{
                new Point(-1, 2),
                new Point(1,2),
                new Point(1, -2),
                new Point(-1,-2),
                new Point(2, -1),
                new Point(2,1),
                new Point(-2, -1),
                new Point(-2,1)
        }, 1);
    }


}
