package com.jasmine.B3_design_mode.责任链模式_Chain.sevlet模式;

import java.util.ArrayList;
import java.util.List;

public class FilterConfig {

    public final List<Filter> filterList = new ArrayList<Filter>();

    public void addFilter(Filter filter){
        filterList.add(filter);
    }

    public List<Filter> getFilters(){
        return filterList;
    }
}
