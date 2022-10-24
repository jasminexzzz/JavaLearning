package com.xzzz.B3_design_mode.工厂模式_Factory.抽象工厂.例子2.model.mainboard;

/**
 * @author : jasmineXz
 */
public class IntelMainBoard implements MainBoard {
    /**
     * CPU插槽的孔数
     */
    private int cpuHoles = 0;
    /**
     * 构造方法，传入CPU插槽的孔数
     * @param cpuHoles
     */
    public IntelMainBoard(int cpuHoles){
        this.cpuHoles = cpuHoles;
    }
    @Override
    public void installCPU() {
        // TODO Auto-generated method stub
        System.out.println("Intel主板的CPU插槽孔数是：" + cpuHoles);
    }

}
