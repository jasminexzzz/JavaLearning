package com.jasmine.C1_framework.SpringMVC.手写SpringMVC.controller;

        import com.jasmine.C1_framework.SpringMVC.手写SpringMVC.annotation.MyAutoWired;
        import com.jasmine.C1_framework.SpringMVC.手写SpringMVC.annotation.MyController;
        import com.jasmine.C1_framework.SpringMVC.手写SpringMVC.annotation.MyRequestMapping;
        import com.jasmine.C1_framework.SpringMVC.手写SpringMVC.annotation.MyRequestParam;
        import com.jasmine.C1_framework.SpringMVC.手写SpringMVC.service.JasService;

        import javax.servlet.http.HttpServletRequest;
        import javax.servlet.http.HttpServletResponse;
        import java.io.IOException;
        import java.io.PrintWriter;

@MyController
@MyRequestMapping("/jas")
public class JasController {


    // 被该注解的引用无需通过new创建对象,会被MVC自动注入对象,这样代码中无需用new,可达到解耦的目的
    @MyAutoWired("JasServiceImpl")// iocMap.get("JasServiceImpl")
    private JasService jasService;

    @MyRequestMapping("/query.action")
    public void query(HttpServletRequest request, HttpServletResponse response,
                      @MyRequestParam("name") String name,
                      @MyRequestParam("age") String age) throws IOException {
        String result = jasService.query(name,age);

        PrintWriter pw = response.getWriter();
        pw.write(result);

    }
}
