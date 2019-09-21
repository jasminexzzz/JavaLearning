package com.jasmine.Java基础;


import org.junit.Test;

import java.math.BigDecimal;

@SuppressWarnings("all")
public class 基础 {
    /**
     1. 面向对象的三大特征
        封装,继承,多态. 也可以加个抽象

     2. 多态的好处
        同一个对象,在不同时刻有不同的状态.

     3. 如何实现多态
        1. 结构实现
        2. 继承父类重写方法
        3. 同一类中进行方法重载

     4. 虚拟机是如何实现多态的
        动态绑定技术(dynamic binding),执行期间判断所引用对象的实际类型,根据实际类型调用对应的方法.

     5. 接口的意义
        规范,扩展,回调.

     6. 抽象类的意义
        1. 为其他子类提供一个公共的类型
        2. 封装子类中重复定义的内容
        3. 定义抽象方法,子类虽然有不同的实现,但是定义时一致的

     7. 抽象类和接口的区别
         默认方法
            抽象 : 可以有默认的方法实现
            接口 : java 8之前,接口中不存在方法的实现.
         实现方式
            抽象 : 子类使用extends关键字来继承抽象类,如果子类不是抽象类子类,需要提供抽象类中所声明方法的实现.
            接口 : 子类使用implements来实现接口,需要提供接口中所有声明的实现
         构造器
            抽象 : 可以
            接口 : 不能
         和正常类区别
            抽象 : 不能被实例化
            接口 : 不能被实例化
         访问修饰符
            抽象 : 可以有public,protected和default等修饰
            接口 : 接口默认是public,不能使用其他修饰符
         多继承
            抽象 : 一个子类只能存在一个父类
            接口 : 一个子类可以存在多个接口
         添加新方法
            抽象 : 想抽象类中添加新方法,可以提供默认的实现,因此可以不修改子类现有的代码
            接口 : 如果往接口中添加新方法,则子类中需要实现该方法.

     8. 父类的静态方法能否被子类重写
        不能.重写只适用于实例方法,不能用于静态方法,而子类当中含有和父类相同签名的静态方法,我们一般称之为隐藏.

     9. 什么是不可变对象
        不可变对象指对象一旦被创建,状态就不能再改变.任何修改都会创建一个新的对象,如 String、Integer及其它包装类.

     10. 静态变量和实例变量的区别?
        静态变量在编译后存在于class常量池,在类被加载后存储在方法区(元空间),属于类所有.实例变量存储在堆当中,其引用存在当前线程栈.
        @see com.jasmine.Java高级.JVM.内存模型_JMM.概念

     11. 创建对象的几种方式
        1. 采用new
        2. 通过反射
        3. 采用clone
        4. 通过序列化机制

     12. switch中能否使用string做参数
        1.7之后switch开始支持String.

     13. switch能否作用在byte, long上?
        switch原则上只能作用于int型上,但是char、float、char等可以隐式的转换为int型,而long不可以.
        可以用在byte上,但是不能用在long上.

     14. String s1="ab", String s2="a"+"b", String s3="a", String s4="b", s5=s3+s4请问s5==s2返回什么?
     */
    @Test
    public void StringPlus(){
        String s1 = "ab";
        String s2 = "a" + "b";//此代码会被编译器解释为String s2 = "ab";
        System.out.println(s1 == s2);//true

        String s3 = "a";
        String s4 = "b";
        String s5 = s3 + s4;//此代码相当于new String("ab");
        System.out.println(s2 == s5);//false
    }
    /**
     15. 你对String对象的intern()熟悉么?
        intern()方法会首先从常量池中查找是否存在该常量值,如果常量池中不存在则现在常量池中创建,如果已经存在则直接返回.

     16. Object中有哪些公共方法?
        @see Object
        equals()    : 比较地址
        hashCode()  : 获取hash值,是个native方法
        clone()     : 默认是浅拷贝
        getClass()  : 获取class对象
        notify()    : 唤醒一个等待(对象的)线程并使该线程开始执行.所以如果有多个线程等待一个对象,这个方法只会唤醒其中一个线程,选择哪个线程取决
                      于操作系统对多线程管理的实现.
        notifyAll() : notifyAll 会唤醒所有等待(对象的)线程,尽管哪一个线程将会第一个处理取决于操作系统的实现.
        wait()      : 使当前线程阻塞,前提是必须先获得锁,一般配合synchronized关键字使用,即,一般在synchronized同步代码块里使用 wait()、notify
                      /notifyAll() 方法.
        toString()  : return getClass().getName() + "@" + Integer.toHexString(hashCode());

     17. 深拷贝和浅拷贝
        浅拷贝 : 只复制基本类型,引用类型还是指向原来的.
        深拷贝 : 在浅拷贝的基础上,所有引用变量也进行了clone,并指向被复制过的新对象.
        @link https://blog.csdn.net/qq_33314107/article/details/80271963

     18. java当中的四种引用
        @link https://blog.csdn.net/dd864140130/article/details/49885811
        强引用,软引用,弱引用,虚引用.不同的引用类型主要体现在GC上:

        1. 强引用(Strong Reference) :
            如果一个对象具有强引用,它就不会被垃圾回收器回收.即使当前内存空间不足,JVM也不会回收它,而是抛出 OutOfMemoryError 错误,使程序异常终
            止.如果想中断强引用和某个对象之间的关联,可以显式地将引用赋值为null,这样一来的话,JVM在合适的时间就会回收该对象.
            如 : Object object = new Object();
                 String str ="hello";
                 User user = new User();

        2. 软引用(Soft Reference) :
            内存足够,继续使用,内存不足,被垃圾回收器回收.
            如 : User user = new User();
                 SoftReference sr = new SoftReference(user);

        3. 弱引用(Weak Reference):
            具有弱引用的对象拥有的生命周期更短暂.因为当 JVM 进行垃圾回收,一旦发现弱引用对象,无论当前内存空间是否充足,都会将弱引用回收.不过由于
            垃圾回收器是一个优先级较低的线程,所以并不一定能迅速发现弱引用对象.
            如 : User user = new User();
                 WeakReference wr = new WeakReference(user);

        4. 虚引用(Phantom Reference):
            顾名思义,就是形同虚设,如果一个对象仅持有虚引用,那么它相当于没有引用,在任何时候都可能被垃圾回收器回收.
            如 : ReferenceQueue queue=new ReferenceQueue();
                 PhantomReference pr = new PhantomReference(object.queue);

     19. 为什么要有不同的引用类型
        不像C语言,我们可以控制内存的申请和释放,在Java中有时候我们需要适当的控制对象被回收的时机,因此就诞生了不同的引用类型,可以说不同的引用
        类型实则是对GC回收时机不可控的妥协。有以下几个使用场景可以充分的说明 : 
        1. 利用软引用和弱引用解决OOM问题 : 用一个HashMap来保存图片的路径和相应图片对象关联的软引用之间的映射关系,在内存不足时,JVM会自动回收这
           些缓存图片对象所占用的空间,从而有效地避免了OOM的问题.
        2. 通过软引用实现Java对象的高速缓存:比如我们创建了一Person的类,如果每次需要查询一个人的信息,哪怕是几秒中之前刚刚查询过的,都要重新构建
           一个实例,这将引起大量Person对象的消耗,并且由于这些对象的生命周期相对较短,会引起多次GC影响性能。此时,通过软引用和 HashMap 的结合
           可以构建高速缓存,提供性能。

     20. java中==和eqauls()的区别,equals()和`hashcode的区别
        ==是运算符,用于比较两个变量是否相等,而equals是Object类的方法,用于比较两个对象是否相等。默认Object类的equals方法是比较两个对象的地址
        ,此时和==的结果一样。换句话说 : 基本类型比较用==,比较的是他们的值。默认下,对象用==比较时,比较的是内存地址,如果需要比较对象内容,需要
        重写equal方法。

     21. a.hashCode()有什么用?与a.equals(b)有什么关系
        hashCode() 方法是相应对象整型的 hash 值。它常用于基于 hash 的集合类,如 Hashtable、HashMap、LinkedHashMap等等。它与 equals() 方法关
        系特别紧密。根据 Java 规范,使用 equal() 方法来判断两个相等的对象,必须具有相同的 hashcode。

        将对象放入到集合中时,首先判断要放入对象的hashcode是否已经在集合中存在,不存在则直接放入集合。如果hashcode相等,然后通过equal()方法判断
        要放入对象与集合中的任意对象是否相等 : 如果equal()判断不相等,直接将该元素放入集合中,否则不放入。

     22. 有没有可能两个不相等的对象有相同的hashcode
        有可能,两个不相等的对象可能会有相同的 hashcode 值,这就是为什么在 hashmap 中会有冲突。如果两个对象相等,必须有相同的hashcode 值,反之
        不成立。

     22. 可以在hashcode中使用随机数字吗?
        不行,因为同一对象的 hashcode 值必须是相同的

     23. a==b与a.equals(b)有什么区别
        如果a 和b 都是对象,则 a==b 是比较两个对象的引用,只有当 a 和 b 指向的是堆中的同一个对象才会返回 true,而 a.equals(b) 是进行逻辑比较
        ,所以通常需要重写该方法来提供逻辑一致性的比较。例如,String 类重写 equals() 方法,所以可以用于两个不同对象,但是包含的字母相同的比较。

     24. 3*0.1==0.3返回值是什么
        false,因为有些浮点数不能完全精确的表示出来。
        通常使用BigDecimal,常用来计算金额,且用public BigDecimal(String val)构造方法
        BigDecimal的加减乘除方法:
        //加法
        bignum3 =  bignum1.add(bignum2);
        //减法
        bignum3 = bignum1.subtract(bignum2);
        //乘法
        bignum3 = bignum1.multiply(bignum2);
        //除法
        bignum3 = bignum1.divide(bignum2);
     */
    @Test
    public void floatTest(){
        System.out.println(3 * 0.1 == 0.3);
        double a = 3;
        double b = 0.1;
        System.out.println(a * b == 0.3);//a * b = 0.30000000000000004

        BigDecimal a1 = new BigDecimal("3");
        BigDecimal b1 = new BigDecimal("0.1");
        System.out.println((a1.multiply(b1)).toString().equals("0.3"));

    }

    /**
     25. a=a+b与a+=b有什么区别吗?
        +=操作符会进行隐式自动类型转换,此处a+=b隐式的将加操作的结果类型强制转换为持有结果的类型,而a=a+b则不会自动进行类型转换.

     26. short s1= 1; s1 = s1 + 1; 该段代码是否有错,有的话怎么改？
        有错误,short类型在进行运算时会自动提升为int类型,也就是说s1+1的运算结果是int类型。

     27. short s1= 1; s1 += 1; 该段代码是否有错,有的话怎么改？
        +=操作符会自动对右边的表达式结果强转匹配左边的数据类型,所以没错。
     */
    @Test
    public void shortTest(){
        short s1= 1;
        // s1 = s1 + 1;// 编译错误
        s1 += 1;
    }

     /**
      28. & 和 &&的区别
        当&操作符两边的表达式不是boolean类型时,&标识按位与操作,而&&是逻辑运算符。
        && 具有短路特性,也就是&&左边为false,则不计算后面的表达式,而&不具备短路特性。

      29. 一个java文件内部可以有类？(非内部类)
        只能有一个public公共类,但是可以有多个default修饰的类。

      30. String, StringBuilder, StringBuffer的区别
        String是普通版字符串,也是线程安全的
        StringBuilder是更高效的String
        StringBuffer是线程安全版的StringBuilder

        如果不是多线程环境,还是要使用StringBuilder

      31. Collection和Collections的区别？
        Collection是一个接口,它是Set、List等容器的父接口;Collections是个一个工具类,提供了一系列的静态方法来辅助容器操作,这些方法包括对容器
        的搜索、排序、线程安全化等等。

      32. cookie和session的区别,分布式环境怎么保存用户状态
        1. cookie放在浏览器,session放在服务器。
        2. cookie不是很安全,别人可以分析存放在本地的COOKIE并进行COOKIE欺骗,考虑到安全应当使用session。
        3. session会在一定时间内保存在服务器上。当访问增多,会比较占用你服务器的性能,考虑到减轻服务器性能方面,应当使用COOKIE。
        4. 单个cookie保存的数据不能超过4K,很多浏览器都限制一个站点最多保存20个cookie。

      33. Error和Exception有什么区别？
        1. Error 表示系统级的错误和程序不必处理的异常,是一种几乎无法恢复的严重问题;比如内存溢出,不能指望程序能处理这样的情况;
        2. Exception表示需要捕捉或者需要程序进行处理的异常,是一种设计或实现问题;也就是说,它表示如果程序运行正常,从不会发生的情况。

      34. final、finally和finalized的区别？
        1. final : 被final修饰的类,不被能继承;被final修饰的方法,不能被重写;被fianl修饰的量,为常量,只能被赋值一次;
        2. finally : 异常处理,和try、catch结合使用,可加可不加,用于执行一些必须执行的代码,如释放资源等;
        3. finalized : Object类中的方法,其中定义了对象要被垃圾回收器回收之前,要做的一些清理工作。

      35. float f=3.4;是否正确？
        不正确。3.4是双精度数，将双精度型(double)赋值给浮点型(float)属于下转型(down-casting，也称为窄化)会造成精度损失，因此需要强制类
        型转换float f =(float)3.4; 或者写成float f =3.4F;

      36. Java基本数据类型有哪些,占多少字节,范围是多少?


           ┌───── byte     1字节    -128 ~ 127                 (-2^7  ~ 2^7-1)     ─┐
           ├───── short    2字节    -32768 ~ 32767             (-2^15 ~ 2^15-1)     | 整型
           ├───── int      4字节    -2147483648 ~ 2147483647   (-2^31 ~ 2^31-1)     |
           ├───── long     8字节                               (-2^63 ~ 2^63-1)    ─┘ -9223372036854775808 ~ 9223372036854775807
          ─┤
           ├───── float    4字节                                                   ─┐ 浮点型
           ├───── double   8字节                                                   ─┘
           |
           ├───── char     2字节                                                   ── 字符型
           └───── boolean  1字节                                                   ── boolean型

      37. short s1 = 1; s1 = s1 + 1;有错吗?
          short s1 = 1; s1 += 1;有错吗？
      */
     @Test
     public void testShort(){
         short s1 = 1;
//         s1 = s1 + 1;//有错 类型不同需要强转
         s1 = (short) (s1 + 123);
         System.out.println(s1);

     }




































}
