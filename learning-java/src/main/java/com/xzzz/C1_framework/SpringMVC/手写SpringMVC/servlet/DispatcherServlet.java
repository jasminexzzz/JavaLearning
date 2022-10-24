package com.xzzz.C1_framework.SpringMVC.手写SpringMVC.servlet;


import com.xzzz.C1_framework.SpringMVC.手写SpringMVC.annotation.*;
import com.xzzz.C1_framework.SpringMVC.手写SpringMVC.controller.JasController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {
    /*
    存放类名
     */
    List<String> calssNames = new ArrayList<String>();
    /*
    存放的是所有被注解过的类的反射,所有的controller,service
    key:类名
    value:类的对象
     */
    Map<String,Object> beans = new HashMap<String,Object>();
    /*
    存放的是所有的方法路径和方法
    key:路径
    value:方法
     */
    Map<String,Object> handlerMaps = new HashMap<String,Object>();




    public void init(ServletConfig config){
        //1.实例化所有声明了注解的类  扫描
        doScan("com.jasmine.框架学习.springMVC.手写SpringMVC");
        //2.把扫描到的.class进行实例化
        doInstance();
        //3.依赖注入
        doAutoWired();
        //4.前台action请求如何找到controller
        UrlHanding();// /jas/query -->  method query()
    }


    /**
     * 扫描项目路径,获取所有.class文件名,放入classNames集合中
     * @param basePackage
     */
    public void doScan(String basePackage){
        URL url = this.getClass().getClassLoader().getResource("/"+basePackage.replaceAll("\\.","/"));
        ///E:/Work/WorkSpace/IdeaProjects/MyJava/target/MyJava/WEB-INF/classes/com/jasmine/
        String fileStr = url.getFile();
        File file = new File(fileStr);

        String[] filesStr = file.list();
        for(String path : filesStr){

            File filePath = new File(fileStr + path);
            if(filePath.isDirectory()){
                //递归
                doScan(basePackage + "." + path);
            }else{
                //.calss
                calssNames.add(basePackage + "." + filePath.getName());
            }

        }
    }

    /**
     * 用类名通过反射创建对象
     */
    public void doInstance(){
        //遍历类名集合
        for(String className:calssNames){
            //
            //将类名的.class取消掉 (com/jasmine/框架学习/springMVC/手写SpringMVC/JasController.class)
            String cn = className.replace(".class","");//com/jasmine/框架学习/springMVC/手写SpringMVC/JasController
            try {
                //1.获取class对象
                Class<?> clazz = Class.forName(cn);

                //2.如果是Controller注解过的类
                if(clazz.isAnnotationPresent(MyController.class)){
                    //3.创建对象
                    Object instance = clazz.newInstance();//iocMap.put(,"instance")
                    //4.获取请求路径名,实际SpringMVC中存放的是类名
                    MyRequestMapping map1 = clazz.getAnnotation(MyRequestMapping.class);
                    String key = map1.value();
                    //5.放入beans中 如("jas",JasController类对象)
                    beans.put(key,instance);

                //2.如果是Service注解过的类
                }else if(clazz.isAnnotationPresent(MyService.class)){
                    //3.创建对象
                    Object instance = clazz.newInstance();//iocMap.put(,"instance")
                    //4.获取请求路径名,实际SpringMVC中存放的是类名
                    MyService map2 = clazz.getAnnotation(MyService.class);
                    String key2 = map2.value();
                    //5.放入beans中 如("jas",JasController类对象)
                    beans.put(key2,instance);

                }else {
                    continue;
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 1. 获取到被AutoWired注解的引用
     * 2. 根据注解中的名称,到Beans中去寻找该名称对应的对象
     * 3. 将该对象注入到被注解修饰的引用中去
     *
     * 也就是说
     * JasService j = new JasServiceImpl();
     * 改为了
     * @AutoWired(JasServiceImpl)
     * JasService j;
     * @AutoWired(JasServiceImpl)会把new JasServiceImpl()交由Spring去创建和销毁,不需要用户去new对象.使代码解耦
     *
     */
    public void doAutoWired() {
        //1. 遍历beans中存放的所有Controller,service
        for(Map.Entry<String,Object> entry : beans.entrySet()){
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();

            //2. 如果是controller类则要额外处理,因为只有Controller中才存在MyAutoWired注解的需要注入的引用
            if(clazz.isAnnotationPresent(MyController.class)){
                //3. 获取类中所有的变量数组
                Field[] fields = clazz.getDeclaredFields();
                //4. 遍历变量数组
                for(Field field:fields){
                    //5. 如果变量引用被MyAutoWired注解,则需要注入
                    if(field.isAnnotationPresent(MyAutoWired.class)){
                        //6. 获取注解中的值 如@MyAutoWired("JasServiceImpl")中的JasServiceImpl
                        MyAutoWired auto = field.getAnnotation(MyAutoWired.class);
                        String key = auto.value();
                        /*
                        7. 通过值获取值对应的对象
                           beans中的值均为("key",key名称对应的类的对象)
                           例如:获取JasServiceImpl类的对象
                         */
                        Object obj = beans.get(key);

                        field.setAccessible(true);
                        try {
                            //8. 将对象赋值给该引用,也就是注入给该对象引用
                            field.set(instance,obj);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }else{
                        continue;
                    }
                }
            }else{
                continue;
            }
        }
    }


    /**
     * 将Controller中的所有的方法路径和方法存放到handlerMaps中
     * key:路径
     * value:方法
     *
     */
    public void UrlHanding() {
        //1. 遍历beans
        for(Map.Entry<String,Object> entry : beans.entrySet()){
            //2. 创建对象
            Object instance = entry.getValue();
            Class<?> clazz = instance.getClass();

            //3. 如果对象被Controller修饰
            if(clazz.isAnnotationPresent(MyController.class)){
                //4. 获取MyRequestMapping中的值,也就是请求路径,例如本例子中controller中的/jas
                MyRequestMapping mrm = clazz.getAnnotation(MyRequestMapping.class);
                // /jas
                String classPath = mrm.value();

                //5. 获取类的所有方法并遍历
                Method[] methods = clazz.getMethods();
                for(Method method:methods){
                    //获取方法的MyRequestMapping中的值,例如本例子中controller中的/query
                    if(method.isAnnotationPresent(MyRequestMapping.class)){
                        MyRequestMapping map = method.getAnnotation(MyRequestMapping.class);
                        // /query
                        String methodPath = map.value();
                        /*
                         用路径做key,用方法做value
                         /jas/query
                         Controller中的方法
                        */
                        handlerMaps.put(classPath+methodPath,method);
                    }else{
                        continue;
                    }
                }
            }
        }
    }








    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //拿到请求路径
        String uri = req.getRequestURI();
        //拿到项目名
        String context = req.getContextPath();
        //把路径中的项目名替换掉,因为在SpringMVC bean中不存放项目名称,只有请求路径
        String path = uri.replace(context,"");

        //根据请求路径获取方法
        Method method = (Method) handlerMaps.get(path);

        //path.split("/")[1]是根据/分割并取分割后的第一个值 (从0开始)
        //获取jas的类
        JasController instance = (JasController) beans.get("/"+path.split("/")[1]);

        Object args[] = hand(req,resp,method);

        try {
            method.invoke(instance,args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static Object[] hand (HttpServletRequest request,HttpServletResponse response,Method method){
        //拿到当前执行的方法的参数有哪些
        Class<?>[] paramClazzs = method.getParameterTypes();
        //根据参数的个数,new一个参数的数组,将方法里的所有参数赋值到args来
        Object[] args = new Object[paramClazzs.length];

        int args_i = 0;
        int index = 0;
        for(Class<?> paramClazz : paramClazzs){
            //class1.isAssignableFrom(class2) 判断class2是不是class1的子类或者子接口
            if(ServletRequest.class.isAssignableFrom(paramClazz)){
                args[args_i++] = request;
            }
            if(ServletResponse.class.isAssignableFrom(paramClazz)){
                args[args_i++] = response;
            }
            /*
            从0-3判断有没有requestparam注解,paramClazz为0和1时,为request和response
            当为2和3时为@requestparam,需要解析
             */
            //获取参数注解
            Annotation[] paramAns = method.getParameterAnnotations()[index];
            if(paramAns.length > 0){
                for(Annotation paramAn : paramAns){
                    if(MyRequestParam.class.isAssignableFrom(paramAn.getClass())){
                        MyRequestParam rp = (MyRequestParam)paramAn;
                        //找到注解里的name和age
                        args[args_i++] = request.getParameter(rp.value());
                    }
                }
            }
            index++;
        }
        return args;
    }
}
