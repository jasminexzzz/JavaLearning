package com.jasmine.设计模式.备忘录模式_Memento.例1;

/**
 * @author jasmineXz
 */
public class ZTest {

    public static void main(String[] args) {
        // 创建棋子,初始位置
        Chessman chessman = new Chessman("兵");
        Chessboard.init(chessman);

        chessman.setX(1);
        chessman.setY(1);
        Chessboard.move(chessman); // 移动棋子

        chessman.setX(1);
        chessman.setY(2);
        Chessboard.move(chessman); // 移动棋子

        Chessboard.undo(chessman); // 悔棋
        Chessboard.undo(chessman); // 悔棋
    }


}
