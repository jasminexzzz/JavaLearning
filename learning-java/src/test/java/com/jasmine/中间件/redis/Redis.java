package com.jasmine.中间件.redis;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : jasmineXz
 */

@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class})
@SpringBootTest
public class Redis {
    /**
     1. Redis数据淘汰策略
     redis 内存数据集大小上升到一定大小的时候，就会施行数据淘汰策略。redis 提供 6种数据淘汰策略:
     voltile-lru      : 从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰
     volatile-ttl     : 从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
     volatile-random  : 从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰
     allkeys-lru      : 从数据集（server.db[i].dict）中挑选最近最少使用的数据淘汰
     allkeys-random   : 从数据集（server.db[i].dict）中任意选择数据淘汰
     no-enviction（驱逐）: 禁止驱逐数据
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test() throws Exception {
        List<String> list =new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("v");
        stringRedisTemplate.opsForValue().set("abc", "测试");
        stringRedisTemplate.opsForList().leftPushAll("qq",list); // 向redis存入List
        stringRedisTemplate.opsForList().range("qwe",0,-1).forEach(value ->{
            System.out.println(value);
        });
    }
}
