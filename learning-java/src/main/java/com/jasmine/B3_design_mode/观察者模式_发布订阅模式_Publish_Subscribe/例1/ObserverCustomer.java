package com.jasmine.B3_design_mode.观察者模式_发布订阅模式_Publish_Subscribe.例1;

/**
 * 观察者
 * @author : jasmineXz
 */
public class ObserverCustomer implements Observer{
    //订阅者名字
    private String name;
    private int version;

    public ObserverCustomer(String name){
        this.name = name;
    }

    @Override
    public void update (int version) {
        this.version = version;
//        System.out.println("该杂志出新版本了");
        this.buy();
    }

    public void buy(){
        System.out.println(name + "购买了第" + version + "期的杂志!");
    }

}
