package com.jasmine.B3_design_mode.备忘录模式_Memento.例1;

/**
 * 下期类
 *
 * @author jasmineXz
 */
public class Chessman {
    private String piece; // 棋子
    private int x;        // x 坐标
    private int y;        // y 坐标

    public Chessman(String piece) {
        this.piece = piece;
        this.x = 0;
        this.y = 0;
    }

    /**
     * 创建记录
     */
    public ChessmanStep createStep () {
        return new ChessmanStep(piece,x,y);
    }

    /**
     * 回滚步骤
     */
    public void restore (ChessmanStep step) {
        this.piece = step.getPiece();
        this.x = step.getX();
        this.y = step.getY();
    }


    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String place() {
        return "piece='" + piece + '\'' +
                ", x=" + x +
                ", y=" + y;
    }
}
