package com.jasmine.B3_design_mode.建造者模式_builder.例2;

/**
 * 静态内部类建造者模式
 * lombok的建造者模式@Builder就是基于此,反编译后源码一模一样
 *
 * @author jasmineXz
 */
public class UserBuilder {
    Integer userId;
    String userName;
    String pwd;
    Integer age;

    public UserBuilder() {
        throw new RuntimeException("The class could not be initialized, initialize the class with the builder");
    }

    private UserBuilder(Integer userId, String userName, String pwd, Integer age) {
        this.userId = userId;
        this.userName = userName;
        this.pwd = pwd;
        this.age = age;
    }

    public static Builder builder() {
        return new Builder();
    }


    @Override
    public String toString() {
        return "UserBuilder{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", age=" + age +
                '}';
    }

    public static final class Builder {
        Integer userId;
        String userName;
        String pwd;
        Integer age;

        public Builder() {

        }

        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder pwd(String pwd) {
            this.pwd = pwd;
            return this;
        }

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public UserBuilder build () {
            return new UserBuilder(this.userId,this.userName,this.pwd,this.age);
        }

    }
}
