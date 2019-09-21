package com.jasmine.设计模式.策略模式_Strategy.通用实现;
//外部客户端
public class Client {
    public static void main(String[] args) {
        //1.创建具体测策略实现
        IStrategy strategy = new ConcreteStrategy2();
        //2.在创建策略上下文的同时，将具体的策略实现对象注入到策略上下文当中
        StrategyContext ctx = new StrategyContext(strategy);
        //3.调用上下文对象的方法来完成对具体策略实现的回调
        ctx.contextMethod();
    }
}
