package com.jasmine.设计模式.建造者模式_builder;

/**
 * @author : jasmineXz
 */

public class Map {
    private DefenseTower defenseTower; //防御塔
    private Road road;  //道路
    private Tree tree;  // 树
    private Monster monster;  // 怪物
    private String music;  //音乐
    private String px;  //像素

    public DefenseTower getDefenseTower() {
        return defenseTower;
    }

    public void setDefenseTower(DefenseTower defenseTower) {
        this.defenseTower = defenseTower;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getPx() {
        return px;
    }

    public void setPx(String px) {
        this.px = px;
    }
}
