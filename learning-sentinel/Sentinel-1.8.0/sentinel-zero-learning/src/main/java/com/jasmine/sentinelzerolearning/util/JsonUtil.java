package com.jasmine.sentinelzerolearning.util;

import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangxw
 * @since 0.0.1
 */
public class JsonUtil {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 在序列化时日期格式默认为 yyyy-MM-dd'T'HH:mm:ss.SSSZ
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 在序列化时自定义时间日期格式
        MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 在序列化时忽略值为 null 的属性
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        // 解决：序列化 map 时，有 key 为 null 的情况，jackson序列化会报 Null key for a Map not allowed in JSON (use a converting NullKeySerializer?)
//        MAPPER.getSerializerProvider().setNullKeySerializer(new NullKeySerializer());
    }


//    static class NullKeySerializer extends StdSerializer<Object> {
//        public NullKeySerializer() {
//            this(null);
//        }
//
//        public NullKeySerializer(Class<Object> t) {
//            super(t);
//        }
//
//        @Override
//        public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused)
//                throws IOException, JsonProcessingException {
//            jsonGenerator.writeFieldName("null");
//        }
//    }

    /**
     * 将对象转成 json 字符串
     *
     * @param obj 对象
     * @return json 字符串
     */
    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("使用 jackson 序列化对象 " + obj + " 时报错", e);
        }
    }

    /**
     * 将对象转成 格式化的 json 字符串
     *
     * @param obj 对象
     * @return json 字符串
     */
    public static String toPrettyJson(Object obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("使用 jackson 序列化对象 " + obj + " 时报错", e);
        }
    }

    /**
     * 将对象字符串转成Object对象
     *
     * @param objStr 对象字符串
     * @return Object object对象
     */
    public static <T> T toObject(String objStr, Class<T> clazz) {
        try {
            return MAPPER.readValue(objStr, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("使用 jackson 反序列化对象 " + objStr + " 时报错", e);
        }
    }

    /**
     * byte数组转对象
     * @param bytes byte数组
     * @param clazz 对象
     * @param <T> 对象泛型
     * @return 对象
     */
    public static <T> T toObject(byte[] bytes, Class<T> clazz) {
        try {
            return MAPPER.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * 将对象字符串转成Object对象
     *
     * @param objStr        对象字符串
     * @param typeReference 1、转成 List<TestUser> 类型的 new TypeReference<List<User>>(){}
     *                      2、转成 Map<String, User> 类型的 new TypeReference<Map<String, User>>(){}
     * @return Object object对象
     */
    public static <T> T toObject(String objStr, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(objStr, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("使用 jackson 反序列化对象 " + objStr + " 时报错", e);
        }
    }

    /**
     * json字符串转JsonNode
     *
     * @param objStr 字符串对象
     * @return JsonNode
     */
    public static JsonNode toJsonNode(String objStr) {
        try {
            return MAPPER.readTree(objStr);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public static void main(String[] args) {
        String s = "[{\"id\":1,\"app\":\"learning-sentinel\",\"ip\":\"192.168.1.6\",\"port\":8720,\"limitApp\":\"default\",\"resource\":\"resource_fastfail\",\"grade\":1,\"count\":1.0,\"strategy\":0,\"controlBehavior\":0,\"clusterMode\":false,\"clusterConfig\":{\"thresholdType\":0,\"fallbackToLocalWhenFail\":true,\"strategy\":0,\"sampleCount\":10,\"windowIntervalMs\":1000},\"gmtCreate\":\"2022-01-01 16:09:06\",\"gmtModified\":\"2022-01-01 16:09:06\"}]";

        List<FlowRule> list = toObject(s, new TypeReference<List<FlowRule>>(){});
        System.out.println(Arrays.toString(list.toArray()));

    }
}
