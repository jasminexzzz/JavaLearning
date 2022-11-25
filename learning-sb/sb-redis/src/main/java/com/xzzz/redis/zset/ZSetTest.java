package com.xzzz.redis.zset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZSetTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping
    public void zSetAdd() {
        redisTemplate.opsForZSet().add("test_zset", "test_zset1", Double.MAX_VALUE);
        redisTemplate.opsForZSet().add("test_zset", "test_zset2", System.currentTimeMillis());
        redisTemplate.opsForZSet().add("test_zset", "test_zset3", 3);
        redisTemplate.opsForZSet().add("test_zset", "test_zset4", 4);

        System.out.println("===================");
        System.out.println(redisTemplate.opsForZSet().score("test_zset", "test_zset1"));


        System.out.println("===================");
        // 获取指定分数, 排名从0开始
        System.out.println(redisTemplate.opsForZSet().rank("test_zset", "test_zset"));
        System.out.println(redisTemplate.opsForZSet().rank("test_zset", "test_zset1"));
        System.out.println(redisTemplate.opsForZSet().rank("test_zset", "test_zset4"));

        System.out.println("===================");
        // 获取zset的个数
        System.out.println(redisTemplate.opsForZSet().size("test_zset"));

        System.out.println("===================");
        System.out.println(redisTemplate.opsForZSet().zCard("test_zset"));

        // 弹出分数最小的指定数量
        // redisTemplate.opsForZSet().popMin("test_zset", 3);


    }

}
