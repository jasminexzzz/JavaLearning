package com.jasmine.JavaBase.D_IO.Stream;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IntStreamTest {

    static class User{
        private String name;
        private Integer age;

        public User (String name,Integer age){
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    public static void main(String[] args) {
        List<User> userList = new ArrayList<>();
        userList.add(new User("1",1));
        userList.add(new User("1",1));
        userList.add(new User("2",2));
        userList.add(new User("3",3));
        userList.add(new User("4",4));
        userList.add(new User("5",5));

//        userList.forEach(u -> System.out.println(u.getName()));

        // Collectors的用法
        // 排序
        System.out.println("倒序");
//        userList.stream().sorted(Comparator.comparing(User::getAge).reversed()).collect(Collectors.toList()).forEach(item -> {
//            System.out.println(item.getAge());
//        });

        //分组
        System.out.println("分组");
        userList.stream().collect(Collectors.groupingBy(User::getAge)).forEach((key, value) -> {
            System.out.println(key + "  ---" + value);
        });

    }
}
