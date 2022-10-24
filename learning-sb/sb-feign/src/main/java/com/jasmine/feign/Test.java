package com.xzzz.feign;

import com.xzzz.feign.test.User;

import java.util.concurrent.CompletableFuture;

/**
 * @author wangyf
 */
public class Test {

    public static void main(String[] args) {

        User u = new User();
        u.setName("123");
        u.setAge(1);

        CompletableFuture<Object> completableFuture = new CompletableFuture<Object>();
        completableFuture.complete(u);
        System.out.println(completableFuture.join());
    }
}
