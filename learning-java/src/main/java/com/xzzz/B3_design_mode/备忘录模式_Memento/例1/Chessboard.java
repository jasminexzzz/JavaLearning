package com.xzzz.B3_design_mode.备忘录模式_Memento.例1;

/**
 * 棋盘
 *
 * @author jasmineXz
 */
public class Chessboard {

    /**
     * 记录从 -1 开始
     * 因为记录从0开始,若是返回0,则回滚的本次步骤,并不是上次步骤
     */
    private static int stepNum = -1;

    public static void init (Chessman chessman) {
        stepNum ++;
        chessman.setX(0);
        chessman.setY(0);
        ChessmanStepMemento.saveStep(chessman.createStep());
        System.out.println("--- 创建, 当前位置 : " + chessman.place());
        System.out.println("===============================================");
    }

    /**
     * 移动棋子
     */
    public static void move (Chessman chessman) {
        stepNum ++;
        ChessmanStepMemento.saveStep(chessman.createStep());
        System.out.println("↓↓↓ 走棋, 当前位置 : " + chessman.place());
        System.out.println("───────────────────────────────────────────────");
    }

    /**
     * 悔棋
     */
    public static void undo (Chessman chessman) {
        if (stepNum == 0) {
            System.out.println("当前为初始位置,无法再悔棋");
            System.out.println("───────────────────────────────────────────────");
            return;
        }
        stepNum --;
        ChessmanStep undo = ChessmanStepMemento.getStep(stepNum);
        chessman.restore(undo);
        System.out.println("↑↑↑ 悔棋, 当前位置 : " + chessman.place());
        System.out.println("───────────────────────────────────────────────");
    }

    public static int getNum () {
        return stepNum + 1;
    }
}
