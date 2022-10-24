package com.jasmine.B3_design_mode.建造者模式_builder.例1;


/**
 * @author : jasmineXz
 */
public class CreateMap implements Build {
    private Map map = new Map();

    @Override
    public Map getMap() {
        return map;
    }
    @Override
    public CreateMap  buildDefenseTower(DefenseTower defenseTower) {
        map.setDefenseTower(defenseTower);
        return this;
    }
    @Override
    public CreateMap  buildRoad(Road road) {
        map.setRoad(road);
        return this;
    }
    @Override
    public CreateMap buildTree(Tree tree) {
        map.setTree(tree);
        return this;
    }
    @Override
    public CreateMap buildMonster(Monster monster) {
        map.setMonster(monster);
        return this;
    }
    @Override
    public CreateMap buildMusic(String music) {
        map.setMusic(music);
        return this;
    }
    @Override
    public CreateMap buildPx(String px) {
        map.setPx(px);
        return this;
    }
}
