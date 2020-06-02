package com.jasmine.设计模式.备忘录模式_Memento.例1;

/**
 * @author jasmineXz
 */
public class Memento {
    private String state;

    public Memento(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }
}
