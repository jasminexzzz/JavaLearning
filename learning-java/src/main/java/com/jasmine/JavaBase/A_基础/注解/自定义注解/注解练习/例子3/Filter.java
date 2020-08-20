package com.jasmine.JavaBase.A_基础.注解.自定义注解.注解练习.例子3;

public class Filter {
    int num;

    @FieldAnno(name = "id",data = "1234")
    private int id;

    @FieldAnno(name = "userName",data = "admin")
    private String userName;

    @FieldAnno(name = "age",data = "99")
    private int age;

    @FieldAnno(name = "city",data = "China")
    private  String city;


    @FieldAnno(name = "TestMethod",data = "Test11111111")
    public void TestMethod(){

    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
