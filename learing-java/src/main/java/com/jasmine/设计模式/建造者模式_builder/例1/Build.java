package com.jasmine.设计模式.建造者模式_builder.例1;

/**
 * @author : jasmineXz
 */
public interface Build {
    Build  buildDefenseTower(DefenseTower defenseTower); //构建防御塔
    Build  buildRoad(Road road); //构建道路
    Build  buildTree(Tree tree); //构建树
    Build  buildMonster(Monster monster); //构建怪兽
    Build  buildMusic(String music); //构建背景音乐
    Build  buildPx(String px); //构建清晰度
    Map getMap();  //得到构建的地图
}
