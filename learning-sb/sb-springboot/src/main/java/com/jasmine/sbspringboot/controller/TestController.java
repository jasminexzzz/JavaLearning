package com.jasmine.sbspringboot.controller;

import com.jasmine.sbspringboot.config.listener.MyEvent;
import com.jasmine.sbspringboot.config.singleton.Singleton;
import com.jasmine.sbspringboot.factory.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author jasmineXz
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 上下文对象
     */
    @Resource
    private ApplicationContext applicationContext;
    /**
     * 存储已有参数，用于判断参数是否重复，从而判断线程是否安全
     */
    public static Set<String> set = new ConcurrentSkipListSet<>();

    @GetMapping("/hi")
    public String hi(@RequestParam("name") String name) {
        return "你好 : " + name;
    }

    @GetMapping("/listener/publish")
    public void listenerPublish(@RequestParam("msg") String msg) {
        //通过上下文对象发布监听
        applicationContext.publishEvent(new MyEvent(this, msg));
        System.out.println("controller =====> " + msg);
    }


    /* ==========================================================================================
     * 测试线程安全
     * =========================================================================================== */

    @Autowired
    private HttpServletRequest request;

    /**
     * 测试request是否线程安全
     * ────────────────────────────────────────────────────────────────────────────────
     * public void test(HttpServletRequest request)  : 线程安全
     * ────────────────────────────────────────────────────────────────────────────────
     *
     * @Autowired private HttpServletRequest request;           : 线程安全
     * ────────────────────────────────────────────────────────────────────────────────
     * ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest(); : 线程安全
     */
    @GetMapping("/request")
    public void test() throws InterruptedException {
        // 判断线程安全
        String value = request.getParameter("key");
        if (set.contains(value)) {
            System.out.println(value + "\t重复出现，request并发不安全！");
        } else {
            System.out.println(value);
            set.add(value);
        }

        // 模拟程序执行了一段时间
        Thread.sleep(1000);
    }


    /* ==========================================================================================
     * 自定义工厂类使用
     * =========================================================================================== */
    @Autowired
    private Client client;

    @GetMapping("/factory")
    public void factory() {
        System.out.println(client.toString());
//        System.out.println(clientFactory.getClientMap());
    }


    /* ==========================================================================================
     * 自定义工厂类使用
     * =========================================================================================== */
    @Autowired
    private Singleton singleton;

    @GetMapping("/sing")
    public void sing() {
        System.out.println(singleton.toString());
        System.out.println(Singleton.getInstance());
    }

    @GetMapping("/redirect")
    public void testRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.getHeader("Referer"));
        response.sendRedirect("http://localhost:12345/test/hi?name=wyf");
    }
}
