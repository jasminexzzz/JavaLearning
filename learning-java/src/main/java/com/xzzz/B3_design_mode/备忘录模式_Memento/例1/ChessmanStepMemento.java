package com.xzzz.B3_design_mode.备忘录模式_Memento.例1;

import java.util.ArrayList;
import java.util.List;

/**
 * 下期备忘录
 *
 * @author jasmineXz
 */
public class ChessmanStepMemento {

    private static final List<ChessmanStep> chessRecords = new ArrayList<>();

    /**
     * 保存记录
     */
    public static void saveStep (ChessmanStep chessRecord) {
        chessRecords.add(chessRecord);
    }

    /**
     * 获取记录
     * @param i 下标
     */
    public static ChessmanStep getStep (int i) {
        ChessmanStep step = chessRecords.get(i);
        chessRecords.remove(i);
        return step;
    }
}
