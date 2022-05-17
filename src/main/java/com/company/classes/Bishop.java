package com.company.classes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bishop extends Piece{

    public Bishop(boolean color, Point position){

        super(color, position, "bishop.png", new Point[]{
                new Point(1,1),
                new Point(-1,-1),
                new Point(1, -1),
                new Point(-1,1)
        }, 8);
    }

}
