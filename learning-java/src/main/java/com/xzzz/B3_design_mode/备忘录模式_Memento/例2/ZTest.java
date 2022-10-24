package com.xzzz.B3_design_mode.备忘录模式_Memento.例2;

/**
 * @author jasmineXz
 */
class ZTest {
    private static int index = -1;

    /**
     * 备忘录
     */
    private static MementoCaretaker mc = new MementoCaretaker();

    public static void main(String[] args) {
        Chessman chess = new Chessman("车", 1, 1);
        play(chess);
        chess.setY(2);
        play(chess);
        chess.setY(3);
        play(chess);
        undo(chess, index);
        undo(chess, index);
        redo(chess, index);
        redo(chess, index);
    }

    //下棋，同时保存备忘录
    public static void play(Chessman chess) {
        mc.addMemento(chess.save());
        index++;
        chess.show();
    }

    //悔棋，撤销到上一个备忘录
    public static void undo(Chessman chess, int i) {
        System.out.println("******悔棋******");
        index--;
        chess.restore(mc.getMemento(i - 1));
        chess.show();
    }

    //撤销悔棋，恢复到下一个备忘录
    public static void redo(Chessman chess, int i) {
        System.out.println("******撤销悔棋******");
        index++;
        chess.restore(mc.getMemento(i + 1));
        chess.show();
    }
}