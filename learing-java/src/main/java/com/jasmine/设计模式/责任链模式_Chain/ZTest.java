package com.jasmine.设计模式.责任链模式_Chain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jasmineXz
 */
public class ZTest {

    public static void main(String[] args) {
        initFilterChain().doFilter("wyf","123");
    }

    private static AbstractFilter initFilterChain() {
        List<AbstractFilter> filters = new ArrayList<>();
        filters.add(new Filter2());
        filters.add(new Filter1());
        filters.add(new Filter3());

        filters.sort((e, h) ->
                e.getOrder() > h.getOrder() ? 1 : -1
        );

        for(int i=0 ; i < filters.size() - 1 ; i++){
            AbstractFilter filter = filters.get(i);
            AbstractFilter nextFilter = filters.get(i + 1);
            filter.setNext(nextFilter);
        }
        return filters.get(0);
    }
}
