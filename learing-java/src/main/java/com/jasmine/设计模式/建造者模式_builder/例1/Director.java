package com.jasmine.设计模式.建造者模式_builder.例1;

/**
 * @author : jasmineXz
 */

public class Director {
    //创建地图的接口具体实现类
    private Build build = new CreateMap();
    //模拟加载用户下载的地图
    private Resources resources = new Resources();
    //开始构建地图，这里有两个参数用于客户端传参数，px:为清晰度；ishasMusic：为是否加载背景音乐
    public Map createMap(String px,boolean ishasMusic){
        build.buildRoad(resources.getRoad())
                .buildDefenseTower(resources.getDefenseTower())
                .buildTree(resources.getTree())
                .buildMonster(resources.getMonster())
                .buildMusic(ishasMusic==true?"加载音乐":"无音乐")
                .buildPx(px);
        return build.getMap();
    }
}
