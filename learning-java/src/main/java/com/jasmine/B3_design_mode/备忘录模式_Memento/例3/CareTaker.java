package com.jasmine.B3_design_mode.备忘录模式_Memento.例3;

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