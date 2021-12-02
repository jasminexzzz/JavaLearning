package com.jasmine.java.base.A_基础.类.枚举;

/**
 * FPS=first personal shooting第一人称射击游戏
 * RPG＝role playing game角色扮演游戏
 * AVG＝adventure game 冒险游戏
 * ACT= Action Game动作游戏
 * SLG= Strategy Game策略游戏
 * RTS＝real time game实时策略
 * FGT= Fighting Game格斗游戏
 * STG= SHOTING GAME射击游戏
 * PZL= Puzzle Game解谜游戏
 * RCG= Racing Game竞速游戏
 * SPT= Sports Game体育游戏
 */
public enum TypeEnum {
    MMORPG(1,"多人在线角色扮演"), RPG(2,"角色扮演"), RTS(3,"实时策略"), FPS(4,"第一人称射击游戏"),
    SPT(5,"体育游戏"), AVG(6,"冒险游戏");

    private int typeId;
    private String typeName;

    TypeEnum(int typeId,String typeName){
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }
}
