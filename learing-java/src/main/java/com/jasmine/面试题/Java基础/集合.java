package Java基础;

/**
 * @author : jasmineXz
 */
public class 集合 {
    /**
     1. ArrayList、Vector的区别
        ArrayList,LinkedList不是线程安全的,Vector是线程安全的,主要是将方法增加synchronized修饰
        Java不推荐使用Vector,因为他们大部分功能都相同,若要使用线程安全的ArrayList可以使用Collections.synchronizedList来创建对象

     2. ArrayList和LinkedList的区别
        ArrayList是数组实现.LinkedList是链表实现.
        ArrayList查询块,增删慢,LinkedList查询慢,增删块.

        LinkedList在链表的某个位置插入也很慢,每次查询都会从链表中间开始向左或向右查询.
        两个都是线程不安全的,若要线程安全需要用Collections.synchronizedList来创建对象

     3. ArrayList遍历时正确删除元素
        删除元素请使用Iterator方式，如果并发操作，需要对Iterator对象加锁。

     4. Arrays.sort 实现原理和 Collection 实现原理
        jdk7以前 : 的版本中sort()的实现原理是:基本类型使用优化后的快速排序,其他类型使用优化后的归并排序
        jdk7以后 : 如果jdk7以后修改了排序策略:如果JVM启动参数配置了-Djava.util.Arrays.useLegacyMergeSort%3Dtrue+那么就会执行上面所说的排序策
                   略(优化的归并排序),否则将会执行TimSort排序。

        事实上Collections.sort方法底层就是调用的array.sort方法

     5.
     */
}
