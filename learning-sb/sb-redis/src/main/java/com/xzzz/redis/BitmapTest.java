package com.xzzz.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bitmap")
public class BitmapTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping
    public void test() {
        System.out.println(String.valueOf(Integer.MAX_VALUE));
        ValueOperations<String, String> value = redisTemplate.opsForValue();

        value.setBit("test_bitmap", 0, false);
        value.setBit("test_bitmap", 1, true);
        value.setBit("test_bitmap", 2, true);
        value.setBit("test_bitmap", 3, true);
        value.setBit("test_bitmap", 4, true);
        value.setBit("test_bitmap", 5, true);
        value.setBit("test_bitmap", 6, true);
        value.setBit("test_bitmap", 7, true);
        value.setBit("test_bitmap", 8, true);
        value.setBit("test_bitmap", 9, true);


        value.setBit("test_bitmap", Integer.MAX_VALUE, true);

        System.out.println("============");
        System.out.println(value.getBit("test_bitmap", 0));
        System.out.println(value.getBit("test_bitmap", 1));
        System.out.println(value.getBit("test_bitmap", Integer.MAX_VALUE));

        System.out.println(value.size("test_bitmap"));

        System.out.println("============");
        // ture 的数量
        long count = redisTemplate.execute((RedisCallback<Long>) conn -> conn.bitCount("test_bitmap".getBytes(), 0, 123123123));
        System.out.println(count);
    }
}

