package com.jasmine.设计模式.备忘录模式_Memento.例1;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jasmineXz
 */
public class CareTaker {

    private List<Memento> mementoList = new ArrayList<>();

    public void add(Memento state){
        mementoList.add(state);
    }

    public Memento get(int index){
        return mementoList.get(index);
    }
}