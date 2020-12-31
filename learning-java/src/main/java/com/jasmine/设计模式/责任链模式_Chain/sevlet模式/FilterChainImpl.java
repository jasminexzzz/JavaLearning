package com.jasmine.设计模式.责任链模式_Chain.sevlet模式;

import java.util.List;

public class FilterChainImpl implements FilterChain {

    private FilterConfig filterConfig;

    private int index = 1;

    public void doFilter(Request request, Respone response) throws Exception {
        List<Filter> filters = filterConfig.getFilters();
        if (index == filters.size()){
            return;
        }else{
            Filter f = filters.get(index);
            index ++;
            f.doFilter(request, response, this);
        }
    }


    public FilterChainImpl(FilterConfig applicationFilterConfig) {
        super();
        this.filterConfig = applicationFilterConfig;
    }

}
