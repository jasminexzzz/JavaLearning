package com.jasmine.A1_java.base.C_线程.练习;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 一个高性能缓存,支持并发读取和单一写入
 * @author : jasmineXz
 */
public class 高性能缓存 {

    //声明一个map,用来作为缓存模型
    private static Map<String, Object> map = new HashMap<String, Object>();
    //声明一个读写锁
    private static ReadWriteLock rwl = new ReentrantReadWriteLock();


    public static Object getValue(String key) {
        Object value = null;
        try {
            rwl.readLock().lock();//开启读锁
            value = map.get(key);
            if (value == null) {
                try {
                    rwl.readLock().unlock();//关闭读锁
                    rwl.writeLock().lock();//开启写锁
                    value = "abc";//这里是去数据库查询
                    map.put(key, value);//将数据放到缓存模型中
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    rwl.writeLock().unlock();//关闭写锁
                    rwl.readLock().lock();//开启读锁
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            rwl.readLock().unlock();//关闭读锁
        }
        return value;
    }
}
