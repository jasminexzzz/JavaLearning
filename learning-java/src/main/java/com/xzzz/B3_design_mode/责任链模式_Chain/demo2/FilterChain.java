package com.xzzz.B3_design_mode.责任链模式_Chain.demo2;

import java.util.ArrayList;
import java.util.List;


public class FilterChain implements Filter {

    private List<Filter> filterChain;

    public void setFilter (Filter filter) {
        if (filterChain == null) {
            filterChain = new ArrayList<>();
        }
        filterChain.add(filter);
    }


    @Override
    public int getOrder() {
        filterChain.sort((e, h) -> e.getOrder() > h.getOrder() ? 1 : -1);
        return 999;
    }

    @Override
    public void doFilter(Request request) {
        for (int i = 0; i < filterChain.size(); i++) {
            filterChain.get(i).doFilter(request);
        }
    }

}
