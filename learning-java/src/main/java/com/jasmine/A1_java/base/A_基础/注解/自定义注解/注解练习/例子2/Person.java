package com.jasmine.A1_java.base.A_基础.注解.自定义注解.注解练习.例子2;

@Persistent(table="person_inf")
public class Person
{
    @Id(column="person_id",type="integer",generator="identity")
    private int id;
    @Property(column="person_name",type="string")
    private String name;
    @Property(column="person_age",type="integer")
    private int age;

    public Person()
    {
    }
    public Person(int id , String name , int age)
    {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    public int getId()
    {
        return this.id;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return this.name;
    }

    public void setAge(int age)
    {
        this.age = age;
    }
    public int getAge()
    {
        return this.age;
    }

}