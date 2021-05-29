package com.jasmine.sbspringboot.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author jasmineXz
 */
public class TestMain {
    public static void main(String[] args) {
        testRequest();
    }

    private static void testRequest() {
        String prefix = UUID.randomUUID().toString().replaceAll("-", "") + "::";
        for (int i = 0; i < 1000; i++) {
            final String value = prefix + i;
            new Thread(() -> {
                Map<String, Object> map = new HashMap<>(1);
                map.put("key", value);

                HttpResponse response = HttpRequest.get("http://localhost:8080/test/request")
                        .form(map)
                        .execute();
            }).start();
        }
    }
}
