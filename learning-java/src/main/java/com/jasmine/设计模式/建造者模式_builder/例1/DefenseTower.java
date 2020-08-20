package com.jasmine.设计模式.建造者模式_builder.例1;

/**
 * @author : jasmineXz
 */
public class DefenseTower {
    private int size; //防御塔尺寸
    private int x;  //防御塔x坐标
    private int y; //防御塔y坐标
    private int aggressivity; //防御塔攻击力
    private int defenseValue; //防御塔防御值

    public DefenseTower(int size,int x, int y, int aggressivity,int defenseValue){
        this.size = size; //防御塔尺寸
        this.x = x;  //防御塔x坐标
        this.y = y; //防御塔y坐标
        this.aggressivity = aggressivity; //防御塔攻击力
        this.defenseValue = defenseValue; //防御塔防御值
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

    public int getAggressivity() {
        return aggressivity;
    }

    public void setAggressivity(int aggressivity) {
        this.aggressivity = aggressivity;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }
}
