package com.company.classes;

import com.company.gui.GameWindow;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CheckCheckmateTest {

    private Board b;
    private Board b2;

    private void setUpClass() {
        this.b = new Board(new GameWindow(0,0,0,true), true);
        for (int x = 0; x < 8; x++) {
            b.Wpieces.add(new Pawn(true, new Point(x, 1)));
            b.Bpieces.add(new Pawn(false, new Point(x, 6)));
        }
        b.Wpieces.add(new Rook(true, new Point(0, 0)));
        b.Wpieces.add(new Rook(true, new Point(7, 0)));
        b.Bpieces.add(new Rook(false, new Point(0, 7)));
        b.Bpieces.add(new Rook(false, new Point(7, 7)));

        b.Wpieces.add(new Knight(true, new Point(1, 0)));
        b.Wpieces.add(new Knight(true, new Point(6, 0)));
        b.Bpieces.add(new Knight(false, new Point(1, 7)));
        b.Bpieces.add(new Knight(false, new Point(6, 7)));

        b.Wpieces.add(new Bishop(true, new Point(2, 0)));
        b.Wpieces.add(new Bishop(true, new Point(5, 0)));
        b.Bpieces.add(new Bishop(false, new Point(2, 7)));
        b.Bpieces.add(new Bishop(false, new Point(5, 7)));

        b.Wpieces.add(new Queen(true, new Point(3, 0)));
        b.Bpieces.add(new Queen(false, new Point(3, 7)));

        b.wKing = new King(true, new Point(4, 0));
        b.bKing = new King(false, new Point(4, 7));
        b.Wpieces.add(b.wKing);
        b.Bpieces.add(b.bKing);

        for (Piece p:b.Wpieces){
            b.board[p.position.x][p.position.y] = p;
        }
        for (Piece p:b.Bpieces){
            b.board[p.position.x][p.position.y] = p;
        }
    }

    private void setUpClass2() {
        this.b2 = new Board(new GameWindow(0,0,0,true), false);
        b2.board = new Piece[8][8];
        b2.Bpieces = new ArrayList<Piece>();
        b2.Wpieces = new ArrayList<Piece>();
        b2.Wpieces.add(new Rook(true, new Point(4, 3)));
        b2.Wpieces.add(new Rook(true, new Point(7, 0)));
        b2.Bpieces.add(new Rook(false, new Point(0, 7)));
        b2.Bpieces.add(new Rook(false, new Point(7, 7)));

        b2.Wpieces.add(new Knight(true, new Point(1, 0)));
        b2.Wpieces.add(new Knight(true, new Point(6, 0)));

        b2.Wpieces.add(new Queen(true, new Point(4, 6)));


        b2.wKing = new King(true, new Point(4, 0));
        b2.bKing = new King(false, new Point(4, 7));
        b2.Wpieces.add(b2.wKing);
        b2.Bpieces.add(b2.bKing);

        for (Piece p:b2.Wpieces){
            b2.board[p.position.x][p.position.y] = p;
        }
        for (Piece p:b2.Bpieces){
            b2.board[p.position.x][p.position.y] = p;
        }
    }

    @Test
    @DisplayName("Šach")
    void isInCheck() {
        setUpClass();
        CheckCheckmate checkCheckmate = new CheckCheckmate();
        assertAll(() -> assertEquals(false, checkCheckmate.isInCheck(this.b, true)),
                () -> assertEquals(false, checkCheckmate.isInCheck(this.b, false)));
        setUpClass2();
        assertAll(() -> assertEquals(false, checkCheckmate.isInCheck(this.b2, true)),
                () -> assertEquals(true, checkCheckmate.isInCheck(this.b2, false)));
    }

    @Test
    @DisplayName("Šachmat")
    void isCheckmated() {
        setUpClass();
        CheckCheckmate checkCheckmate = new CheckCheckmate();
        assertAll(() -> assertEquals(false, checkCheckmate.isCheckmated(this.b, true)),
                () -> assertEquals(false, checkCheckmate.isCheckmated(this.b, false)));
        setUpClass2();
        assertAll(() -> assertEquals(false, checkCheckmate.isCheckmated(this.b2, true)),
                () -> assertEquals(true, checkCheckmate.isCheckmated(this.b2, false)));
    }
}