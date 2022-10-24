package com.xzzz.B3_design_mode.备忘录模式_Memento.例1;

/**
 * 下期记录类
 *
 * @author jasmineXz
 */
public class ChessmanStep {

    private String piece; // 棋子
    private int x;        // x 坐标
    private int y;        // y 坐标

    public ChessmanStep(String piece, int x, int y) {
        this.piece = piece;
        this.x = x;
        this.y = y;
    }

    public String getPiece() {
        return piece;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
