package com.jasmine.中间件.ElasticSearch;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : jasmineXz
 */
@RestController
@RequestMapping("/es")
public class ElasticSearchController {

    @Autowired
    private ESDao er;

    //增加
    @RequestMapping("/add/{id}")
    public String add(@PathVariable("id")String id){
        BookTest employee = new BookTest();
        employee.setId(id);
        employee.setBookName("西游记");
        employee.setAuthor("吴承恩");
        er.save(employee);
        System.err.println("add a obj");
        return "success";
    }

    //删除
    @RequestMapping("/delete")
    public String delete(){
        BookTest employee=new BookTest();
        employee.setId("1");
        er.delete(employee);
        return "success";
    }

    //局部更新
    @RequestMapping("/update")
    public String update(){
        BookTest employee = er.queryEmployeeById("1");
        employee.setBookName("西游记后传");
        er.save(employee);
        System.err.println("update a obj");
        return "success";
    }

    //查询
    @RequestMapping("/query/{id}")
    public BookTest query(@PathVariable("id")String id){
        BookTest accountInfo=er.queryEmployeeById(id);
        System.err.println(new Gson().toJson(accountInfo));
        return accountInfo;
    }

}
