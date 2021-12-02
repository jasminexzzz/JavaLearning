package com.jasmine.java.high.JVM.字节码;

/**
 * @author : jasmineXz
 */
public class TestJVMStack {

    public static int a = 123;
    public int simpleMethod(){
        int x = 13;
        int y = 14;
        int z = x + y;

        z = new TestJVMStack1().rint();

        return z;
    }

    public static void main(String[] args) throws InterruptedException {
        TestJVMStack s = new TestJVMStack();
        s.simpleMethod();
        Thread.sleep(100000000);
//        System.out.println(s.simpleMethod());
    }
}


/**
 当前代码字节码文件内容
 使用javap -c xxx.class 来查看字节码文件

 public class com.jasmine.JVM.字节码.TestJVMStack {
 public com.jasmine.JVM.字节码.TestJVMStack();
 Code:
 0: aload_0
 1: invokespecial #8                  // Method java/lang/Object.""<init>"":()V
 4: return

 public int simpleMethod();
 Code:
 0: bipush        13                  // 常量13压入操作栈
 2: istore_1                          // 并保存到局部变量表的slot_1中
 3: bipush        14                  // 常量14压入操作栈
 5: istore_2                          // 并保存到局部变量表的slot_2中
 6: iload_1                           // 把局部变量表的slot_1元素(int x)压入操作栈
 7: iload_2                           // 把局部变量表的slot_1元素(int y)压入操作栈
 8: iadd                              // 把上面两个数取出来,在CPU里相加,并压回操作栈顶
 9: istore_3                          // 把栈顶的结果存储到局部变量表的slot_3中
 10: iload_3                          // 从局部变量3中装载int类型值
 11: ireturn                          // 返回栈顶元素

 public int simpleMethodHavaArgs(int);
 Code:
 0: bipush        13                  // 常量13压入操作栈
 2: istore_2                          // 并保存到局部变量表的slot_2中
 3: bipush        14                  // 常量13压入操作栈
 5: istore_3                          // 并保存到局部变量表的slot_3中
 6: iload_2                           // 把局部变量表的slot_2元素(int a)压入操作栈
 7: iload_3                           // 把局部变量表的slot_3元素(int b)压入操作栈
 8: iadd                              // 把上面两个数取出来,在CPU里相加,并压回操作栈顶
 9: iload_1                           // 把局部变量表的slot_1元素(int d)压入操作栈
 10: iadd                             // 把上面两个数取出来,在CPU里相加,并压回操作栈顶
 11: istore        4                  // 将int类型值 4 存入局部变量
 13: iload         4                  // 从局部变量中装载 int 类型值 4
 15: ireturn                          // 返回栈顶元素

 public static void main(java.lang.String[]);
 Code:
 0: new           #1                  // class com/jasmine/JVM/内存模型_JMM/TestJVMStack
 3: dup
 4: invokespecial #22                 // Method ""<init>"":()V
 7: astore_1
 8: getstatic     #23                 // Field java/lang/System.out:Ljava/io/PrintStream;
 11: aload_1
 12: invokevirtual #29                // Method simpleMethod:()I
 15: invokevirtual #31                // Method java/io/PrintStream.println:(I)V
 18: return
 }
 */
