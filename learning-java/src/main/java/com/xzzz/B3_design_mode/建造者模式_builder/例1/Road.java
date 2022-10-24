package com.xzzz.B3_design_mode.建造者模式_builder.例1;

/**
 * @author : jasmineXz
 */
public class Road {
    private int width; //路的尺寸
    private int length; //路的长度
    private int x;  //路的x坐标
    private int y;  //路的y坐标

    public Road(int width,int length, int x, int y){
        this.width = width; //路的尺寸
        this.length = length; //路的长度
        this.x = x;  //路的x坐标
        this.y = y;  //路的y坐标

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
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
}
