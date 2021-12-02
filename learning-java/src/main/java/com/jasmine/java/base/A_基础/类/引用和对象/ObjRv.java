package com.jasmine.java.base.A_基础.类.引用和对象;

/**
 * 对象及引用变量
 * @author wangyf
 */
public class ObjRv {
//    int A;
//    int B;
//    int C;
    public static void main(String[] args) {
	/*
	 * ObjRv orv：创建一个引用变量，基本类型的变量数据和对象的引用，存放在栈中。
	 * new ObjRv()：创建了一个对象，存放在堆（new 出来的对象）或者常量池中（字符串常量对象存放在常量池中）。 
	 * 
	 * 通常把这条语句的动作称之为创建一个对象，其实，它包含了四个动作。
	 * 	1）右边的"new ObjRv"，是以ObjRv类为模板，在堆空间里创建一个ObjRv类对象（也简称为ObjRv对象）。
	 * 	2）末尾的()意味着，在对象创建后，立即调用Vehicle类的构造函数，对刚生成的对象进行初始化。构造函数是肯定有的。如果你没写，Java会给你补上一个默认的构造函数。
	 * 	3）左边的"ObjRv orv"创建了一个Vehicle类引用变量。所谓Vehicle类引用，就是以后可以用来指向Vehicle对象的对象引用。
	 * 	4）"="操作符使对象引用指向刚创建的那个Vehicle对象。
	 */
	ObjRv orv1 = new ObjRv();
	/*
	 * 在栈中又创建了一个引用变量 orv2，此引用变量也指向了 orv1 对象在堆中的位置
	 */
	ObjRv orv2 = orv1;
	/*
	 * 在栈中又创建了一个引用变量 orv3，此引用变量也指向了一个新的地址
	 */
	ObjRv orv3 = new ObjRv();
	
	//执行下列输出可以清楚的查看对应地址
	System.out.println("***第一次赋值***");
	System.out.println("orv1的地址--->"+orv1);
	System.out.println("orv2的地址--->"+orv2);
	System.out.println("orv3的地址--->"+orv3);
	
	/*
	 * 执行下面的语句会怎么样呢？
	 * 	orv1 会重新指向 orv3的对象，这个没问题。
	 * 问题是orv1所指向的第一个对象呢？
	 * 	多数书里说，它被Java的垃圾回收机制回收了。
	 * 	这不确切。正确地说，它已成为垃圾回收机制的处理对象。至于什么时候真正被回收，那要看垃圾回收机制的心情了。
	 * 	需要注意的是，若我们不创建 orv2 ,或不把 orv2 指向 orv1 的对象，上述说法才成立，只有当一个对象的引用变量失效时，垃圾回收机制才会认为此对象需要被回收。
	 */
	orv1 = orv3;
	System.out.println("***将 orv1 重新赋值后***");
	System.out.println("orv1的地址--->"+orv1);
	System.out.println("orv2的地址--->"+orv2);
	System.out.println("orv3的地址--->"+orv3);
	
	/*
	 * 由上看来，下面的语句应该不合法吧？至少是没用的吧？
	 * 不对。它是合法的，而且可用的。譬如，如果我们仅仅为了打印而生成一个对象，就不需要用引用变量来保持它不会被垃圾回收机制回收。
	 */
	new ObjRv();
	
	/*
	 * 最常见的就是打印字符串：字符串对象“I am Java!”在打印后即被丢弃。有人把这种对象称之为临时对象。
	 */
	System.out.println("I am Java!");
	
    }
}












