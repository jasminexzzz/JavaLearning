package com.jasmine.java.high.函数式编程;

import java.util.*;

/**
 * @author wangyf
 * @since 2.0.1
 */
public class TestComparator implements Comparator<Integer> {

    /**
     * 如果返回  1, o2 会在 o1 前面, 即降序, 从大到小
     * 如果返回 -1, o2 会在 o1 后面, 即升序, 从小到大
     *
     * 如果永远返回-1, 相当于倒转
     * 如果永远返回 1, 相当于不变
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Integer o1, Integer o2) {
        System.out.println(String.format("o1:%s, o2:%s", o1, o2));
        return o2 > o1 ? -1 : 1; // 这种会将下面集合升序: 1,2,3,4,5
//        return o2 > o1 ?  1 :-1; // 这种会将下面集合降序: 5,4,3,2,1
    }


    public static void main(String[] args) {
        TestComparator testComparator = new TestComparator();

        List<Integer> list = new ArrayList<Integer>() {
            {
                this.add(1);
                this.add(3);
                this.add(5);
                this.add(2);
                this.add(4);
            }
        };

        list.sort(testComparator);
        System.out.println(Arrays.toString(list.toArray()));
    }

}
