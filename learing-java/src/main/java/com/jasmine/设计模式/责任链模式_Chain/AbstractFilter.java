package com.jasmine.设计模式.责任链模式_Chain;

/**
 * 过滤器抽象类,实现ordered接口,实现类必须实现此接口以实现排序
 * @author : jasmineXz
 */
public abstract class AbstractFilter implements Ordered{

    protected AbstractFilter filter;

    public abstract void doFilter(String request,String resp);

    public void setNext(AbstractFilter filter) {
        this.filter=filter;
    }

    public void next(String request,String resp){
        if (null != this.filter){
            filter.doFilter(request, resp);
        }
    }
}
