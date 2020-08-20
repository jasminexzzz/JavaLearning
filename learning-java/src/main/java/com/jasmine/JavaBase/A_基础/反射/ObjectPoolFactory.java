package com.jasmine.JavaBase.A_基础.反射;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ObjectPoolFactory {
    // 定义一个对象池,前面是对象名，后面是实际对象
    private Map<String ,Object> objectPool = new HashMap<>();
    // 定义一个创建对象的方法，
    // 该方法只要传入一个字符串类名，程序可以根据该类名生成Java对象

    private Object createObject(String clazzName) throws InstantiationException, IllegalAccessException , ClassNotFoundException {
        // 根据字符串来获取对应的Class对象
        Class<?> clazz = Class.forName(clazzName);
        // 使用clazz对应类的默认构造器创建实例
        return clazz.newInstance();
    }

    // 该方法根据指定文件来初始化对象池，
    // 它会根据配置文件来创建对象
    public void initPool(String fileName) throws InstantiationException, IllegalAccessException ,ClassNotFoundException {
        try(FileInputStream fis = new FileInputStream(fileName)) {

            Properties props = new Properties();

            props.load(fis);

            for (String name : props.stringPropertyNames()) {
                // 每取出一对key-value对，就根据value创建一个对象
                // 调用createObject()创建对象，并将对象添加到对象池中
                objectPool.put(name , createObject(props.getProperty(name)));
            }
        } catch (IOException ex) {
            System.out.println("读取" + fileName + "异常");
        }

    }

    public Object getObject(String name) {
        // 从objectPool中取出指定name对应的对象。
        return objectPool.get(name);
    }

    public static void main(String[] args) throws Exception {
        ObjectPoolFactory pf = new ObjectPoolFactory();
        pf.initPool("E:\\Work\\WorkSpace\\IdeaProjects\\MyJava\\src\\com\\jasmine\\JavaBase\\反射\\obj.txt");
        System.out.println("a:"+pf.getObject("a"));      // ①
        System.out.println(pf.getObject("b"));      // ②
    }
}
