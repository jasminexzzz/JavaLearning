package com.jasmine.设计模式.责任链模式_Chain.demo1;


import java.util.ArrayList;
import java.util.List;

/**
 * 业务说明
 * 这个责任链包含返回值, 链式调用中会判断 {@link FlowRequest#age} 的大小
 * 如果  0 < age < 10 : filter1 结束
 * 如果 10 < age < 20 : filter2 结束
 * 如果 20 < age < 30 : filter3 结束
 *
 * 责任链
 * 该种责任链不包含一个list的概念 (尽管本例中放入到了一个list来进行排序, 但没有该list也可以进行排序)
 * 而是每个对象保存自己下次调用的对象, 从而形成链式
 *
 * 开始调用 >
 *  ↑      filter1.next = filter2
 *  |                     filter2.next = filter3.next
 *  |                                    filter3.next = null ┐
 *  └────────────────────────────────────────────────────────┘
 * @author : jasmineXz
 */
public class ZTest {

    public static void main(String[] args) {
        AbstractFilter firstFilter = initFilterChain();

        FlowRequest request = new FlowRequest(15);
        request = firstFilter.doFilter(request);

        System.out.println("年龄为 : " + request.getAge());
    }

    private static AbstractFilter initFilterChain() {

        // 创建filter
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
