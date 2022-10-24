package com.jasmine.A1_java.base.C_线程.练习.修改数字.sync;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 多线程把数字加到100
 */
@SuppressWarnings("all")
public class NumIncrement implements Runnable {
    private int num = 0;
    private CountDownLatch latch;
    private List<Integer> list;

    public NumIncrement (CountDownLatch latch,List<Integer> list) {
        this.latch = latch;
        this.list = list;
    }

    private synchronized void sync() {
        num++;
    }

    private void notSync(){
        list.add(1);
    }

    @Override
    public void run() {
        for (int i = 0 ; i < 50 ; i++ ) {
            notSync();
        }
        latch.countDown();

    }

    public List<Integer> getList() {
        return list;
    }

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(40);
        NumIncrement st = new NumIncrement(latch,new ArrayList<>());

        for(int i = 0 ; i < 40 ; i++) {
            Thread t = new Thread(st,"T" + i);
            t.start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(st.getList().size());

//        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,5,100, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(5));
//        executor.execute(st);
//        executor.execute(st);
//        executor.execute(st);
//        executor.execute(st);
//        executor.execute(st);
//
//        executor.shutdown();
//        while (true) {
//            if (executor.isTerminated()) {
//                System.out.println(st.getNum());
//                break;
//            }
//        }
    }
}

