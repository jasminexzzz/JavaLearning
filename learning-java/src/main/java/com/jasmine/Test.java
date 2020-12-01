package com.jasmine;


import com.jasmine.Other.MyUtils.JsonUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {

    private static final int COUNT_BITS = Integer.SIZE - 3;

    static final String json = "{\"client\":{\"clientId\":\"1000\",\"clientType\":\"PASSWORD\",\"clientName\":\"web\"},\"detail\":{\"userId\":2,\"username\":\"haijiaobaifan\",\"perms\":[]},\"token\":{\"accessToken\":\"5d4ff0ab2eee4f2ae97bd90be12bef8e\",\"tokenType\":\"Bearer\",\"expire\":7200,\"userId\":2},\"name\":\"PASSWORD5d4ff0ab2eee4f2ae97bd90be12bef8e\"}";

    public static void main(String[] args) {
        Auth c = JsonUtil.json2Object(json,Auth.class);
        System.out.println(c.getClient().getClientId());
    }


}
