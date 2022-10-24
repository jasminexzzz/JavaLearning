package com.jasmine.C1_framework.SpringMVC.手写SpringMVC.service.impl;

import com.jasmine.C1_framework.SpringMVC.手写SpringMVC.annotation.MyService;
import com.jasmine.C1_framework.SpringMVC.手写SpringMVC.service.JasService;
import org.springframework.stereotype.Service;

@MyService("JasServiceImpl")
@Service
public class JasServiceImpl implements JasService {

    @Override
    public String query(String name, String age) {

        return "name : " + name + "\r\n age : " + age;
    }
}
