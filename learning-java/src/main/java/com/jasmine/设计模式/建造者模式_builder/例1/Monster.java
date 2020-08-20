package com.jasmine.设计模式.建造者模式_builder.例1;

/**
 * @author : jasmineXz
 */

public class Monster {
    private int size; //尺寸
    private int x;  //x坐标
    private int y; //y坐标
    private int aggressivity; //怪兽攻击力
    private int defenseValue;  //怪兽防御值

    public Monster(int size,int x, int y, int aggressivity,int defenseValue){
        this.size = size;
        this.x = x;
        this.y = y;
        this.aggressivity = aggressivity;
        this.defenseValue = defenseValue;
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
