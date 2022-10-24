package com.jasmine.A1_java.base.A_基础.类.等号和equals;

public class Test {
    public static void main(String[] args) {
        Integer a = new Integer(1);
        Integer b = new Integer(1);

        //等号比较的是地址，他们的地址是不同的，但内容是相同的，因为integer等封装类重写的equals方法
        System.out.println(a == b);
        System.out.println(a.equals(b));

        System.out.println();


        Test t1 = new Test();
        Test t2 = new Test();
        //等号比较的是地址，他们的地址是不同的，此处虽然内容相同，但没有重写equals方法，所以equals比较的仍然是地址
        System.out.println(t1 == t2);
        System.out.println(t1.equals(t2));
    }

    /**
     * 此处重写的上面t1.equals(t2)就会返回true
     * 此处的重写后只要两个对象为同一类型则返回true
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Test) {
            return true;
        }
        return false;
    }
}
