package com.xzzz.A1_java.base.B_集合.collection.set.hashset.HashCode的计算方法;

import java.util.HashSet;

/**
 * 这种17和31散列码的想法来自经典的Java书籍——《Effective Java》第九条。
 *
 * 1.在 Java 应用程序执行期间，在对同一对象多次调用 hashCode 方法时，必须一致地返回相同的整数，
 *   前提是将对象进行 equals 比较时所用的信息没有被修改。
 *   从某一应用程序的一次执行到同一应用程序的另一次执行，该整数无需保持一致。
 *
 * 2.如果根据 equals(Object) 方法，两个对象是相等的，那么对这两个对象中的每个对象调用 hashCode 方法都必须生成相同的整数结果。
 *
 * 3.如果根据 equals(java.lang.Object) 方法，
 *   两个对象不相等，那么对这两个对象中的任一对象上调用 hashCode 方法不要求一定生成不同的整数结果。
 *   但是，程序员应该意识到，为不相等的对象生成不同整数结果可以提高哈希表的性能。
 *
 * 再简单的翻译一下第二三点就是：
 * hashCode()和equals()保持一致，如果equals方法返回true，那么两个对象的hasCode()返回值必须一样。
 * 如果equals方法返回false，hashcode可以一样，但是这样不利于哈希表的性能，一般我们也不要这样做。
 * 重写equals()方法就必须重写hashCode()方法的原因也就显而易见了。
 */
public class Old {

    public Old(String name,int age,String id){
        this.name = name;
        this.age = age;
        this.id = id;
    }

    private String name;
    private int age;
    private String id;


//    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Old)) {
            return false;
        }
        Old old = (Old) o;
//        System.out.println(Old.name.equals(this.name));
//        System.out.println(Old.age == this.age);
//        System.out.println(Old.id.equals(this.id));
        return old.name.equals(this.name) &&
                old.age == this.age &&
                old.id.equals(this.id);
    }

    @Override
    public int hashCode() {
//        return 2;
        int result = 17;
        result = 31 * result + name.hashCode();
        result = 31 * result + age;
        result = 31 * result + id.hashCode();
        return result;
    }

    public static void main(String[] args) {
        Old o1 = new Old("一",11,"111");
        Old o2 = new Old("一",11,"111");
        //==比较的是地址，对于未重写equals的类，equals比较的也是地址
        //但有些时候我们希望能比较内容,所以String类重写了equals
        //比较地址
        System.out.println("==号比较:"+(o1==o2));
        //比较值，因为已经重写equals方法
        System.out.println("equals比较:"+o1.equals(o2));
        HashSet<Old> o = new HashSet();
        o.add(o1);
        o.add(o2);
        System.out.println("地址："+o);
    }
}
