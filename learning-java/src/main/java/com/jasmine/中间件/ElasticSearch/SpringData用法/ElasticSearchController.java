package com.jasmine.中间件.ElasticSearch.SpringData用法;

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
        Book employee = new Book();
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
        Book employee=new Book();
        employee.setId("1");
        er.delete(employee);
        return "success";
    }

    //局部更新
    @RequestMapping("/update")
    public String update(){
        Book employee = er.queryEmployeeById("1");
        employee.setBookName("西游记后传");
        er.save(employee);
        System.err.println("update a obj");
        return "success";
    }

    //查询
    @RequestMapping("/query/{id}")
    public Book query(@PathVariable("id")String id){
        Book accountInfo=er.queryEmployeeById(id);
//        System.err.println(new Gson().toJson(accountInfo));
        return accountInfo;
    }

}
