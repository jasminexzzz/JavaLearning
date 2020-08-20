package com.jasmine.JavaBase.C_线程.线程安全;

/**
 * synchronized锁的是谁?
 *
 * @author jasmineXz
 */
public class SyncTest {

    /**
     * 同对象阻塞,不同对象不阻塞,锁的是对象
     */
    synchronized void test () {
        System.out.println(String.format("%s获得锁",Thread.currentThread().getName()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * this锁的是对象,同 {@link SyncTest#test()}
     * 锁this的方法无法被static修饰,否则编译不过
     */
    void testThis () {
        synchronized (this) {
            System.out.println(String.format("%s获得锁",Thread.currentThread().getName()));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 同对象不同对象都会阻塞,锁的是class
     */
    synchronized static void testStatic () {
        System.out.println(String.format("%s获得锁",Thread.currentThread().getName()));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同对象阻塞,不同对象不阻塞,锁的是对象中变量
     */
    private final Object object = new Object();
    void testParam () {
        synchronized (object) {
            System.out.println(String.format("%s获得锁",Thread.currentThread().getName()));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 同对象阻塞,不同对象不阻塞,锁的是class中的静态变量
     */
    private static final Object objectStatic = new Object();
    void testStaticParam () {
        synchronized (objectStatic) {
            System.out.println(String.format("%s获得锁",Thread.currentThread().getName()));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        // invokeTest();
        // invokeTestMultiObj();
        // invokeTestStatic();
        // invokeTestParam();
        // invokeTestStaticParam();
        invokeTestThis();
    }


    /**
     * 调用同步方法, 锁住的是对象, 一个对象不释放, 其他线程无法获取锁
     * 此处锁住的都是同一个对象,所以3个线程会阻塞调用, 只有等其他线程释放后才能尝试获取锁
     *
     * 输出 :
     * Thread-0获得锁 (sleep 5秒后)
     * Thread-2获得锁 (sleep 5秒后)
     * Thread-1获得锁
     */
    static void invokeTest () {
        SyncTest test = new SyncTest();
        for (int i = 0 ; i < 3 ; i ++) {
            new Thread(test::test).start();
        }
    }

    /**
     * 调用同步方法, 锁住的是对象, 一个对象不释放, 其他线程无法获取锁
     * 此处锁住的是不同对象, 互相不干扰
     *
     * 输出 :
     * Thread-0获得锁 (3个线程同时打印出)
     * Thread-2获得锁 (3个线程同时打印出)
     * Thread-1获得锁 (3个线程同时打印出)
     */
    static void invokeTestMultiObj () {
        for (int i = 0 ; i < 3 ; i ++) {
            SyncTest test = new SyncTest();
            new Thread(test::test).start();
        }
    }

    /**
     * 静态同步方法锁的是class,所以尽管是多个对象,仍然会阻塞
     *
     * 输出 :
     * Thread-0获得锁 (sleep 5秒后)
     * Thread-2获得锁 (sleep 5秒后)
     * Thread-1获得锁 (sleep 5秒后结束)
     */
    static void invokeTestStatic () {
        for (int i = 0 ; i < 3 ; i ++) {
            new Thread(SyncTest::testStatic).start();
        }
    }

    /**
     * 与 {@link SyncTest#invokeTest} 与 {@link SyncTest#invokeTestMultiObj} 相同
     */
    static void invokeTestParam () {
        // SyncTest test = new SyncTest(); 锁同一个对象,会阻塞
        for (int i = 0 ; i < 3 ; i ++) {
            SyncTest test = new SyncTest(); // 锁不同对象,不会阻塞
            new Thread(test::testParam).start();
        }
    }

    /**
     * 锁静态全局变量,尽管是不同对象,依旧会阻塞
     */
    static void invokeTestStaticParam () {
        // SyncTest test = new SyncTest(); 锁同一个对象
        for (int i = 0 ; i < 3 ; i ++) {
            SyncTest test = new SyncTest(); // 锁不同对象,会阻塞
            new Thread(test::testStaticParam).start();
        }
    }

    static void invokeTestThis () {
//         SyncTest test = new SyncTest(); // 锁同一个对象,会阻塞
        for (int i = 0 ; i < 3 ; i ++) {
            SyncTest test = new SyncTest(); // 锁不同对象,不会阻塞
            new Thread(test::testThis).start();
        }
    }

}
