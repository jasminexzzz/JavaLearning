package com.jasmine.设计模式.建造者模式_builder.例1;

/**
 * @author : jasmineXz
 */
public class Tree {
    private String color;  //树颜色
    private int size;  //树尺寸
    private int x;  //树x坐标
    private int y;  //树y坐标

    public Tree(String color,int size,int x,int y){
        this.color = color;  //树颜色
        this.size = size;  //树尺寸
        this.x = x;  //树x坐标
        this.y = y;  //树y坐标

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
