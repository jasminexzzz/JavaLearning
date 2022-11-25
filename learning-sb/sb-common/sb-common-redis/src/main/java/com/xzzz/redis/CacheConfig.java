package com.xzzz.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * redis缓存设置了key缓存名后加 "::"
 * @see org.springframework.data.redis.cache.CacheKeyPrefix#simple
 *
 * @author : wangyf
 */
@EnableCaching
@Configuration
public class CacheConfig implements Serializable{
    private static final long serialVersionUID = -6434034692512290727L;

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT      = "yyyy-MM-dd";
    private static final String DEFAULT_TIME_FORMAT      = "HH:mm:ss";


    private static final Integer DEFAULT_TIME_OUT = 60 * 60 * 4;


    /**
     * 配置
     * @param redisConnectionFactory redis配置
     * @return redis管理器
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                // 默认策略
                this.getDefaultCacheConfiguration(DEFAULT_TIME_OUT),
                // 自定义Key缓存策略
                this.getInitCacheConfigurations()
        );
    }

    /**
     * redis缓存默认配置
     * 修改缓存序列化方式,修改过期时间
     * @param seconds 缓存时间(秒)
     * @return RedisCacheConfiguration Redis配置类
     */
    private RedisCacheConfiguration getDefaultCacheConfiguration(Integer seconds) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 修改序列化方式
        jackson2JsonRedisSerializer.setObjectMapper(customObjectMapper());
        return RedisCacheConfiguration
            .defaultCacheConfig()
            .computePrefixWith(name -> SbRedisConstant.CACHE_PREFIX_WITH + name + SbRedisConstant.CACHE_SUFFIX_WITH)
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
            .entryTtl(Duration.ofSeconds(seconds));
    }

    /**
     * 自定义配置, 不使用公共的缓存有效时间
     * @return 配置对象
     */
    private Map<String, RedisCacheConfiguration> getInitCacheConfigurations() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(1);
        // ========== 自定义设置缓存时间 ===========
        return redisCacheConfigurationMap;
    }

    /**
     * 自定义序列化方式
     * 使用缓存注解时反序列化LocalDateTime错误的问题,继承自jsr310
     *
     * @return ObjectMapper
     */
    private ObjectMapper customObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        //LocalDateTime系列序列化和反序列化模块，继承自jsr310
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }


}
