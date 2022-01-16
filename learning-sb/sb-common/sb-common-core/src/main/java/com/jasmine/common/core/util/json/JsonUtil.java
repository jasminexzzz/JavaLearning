package com.jasmine.common.core.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON工具类
 *
 * @author : jasmineXz
 */
public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(myDateFormat);
        // 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许下换线和驼峰之间的转换
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        // 允许出现单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 解决：序列化 map 时，有 key 为 null 的情况，jackson序列化会报 Null key for a Map not allowed in JSON (use a converting NullKeySerializer?)
        mapper.getSerializerProvider().setNullKeySerializer(new NullKeySerializer());

        // 忽视为null的字段
//        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * Key为null是的序列化方式
     */
    static class NullKeySerializer extends StdSerializer<Object> {
        public NullKeySerializer() {
            this(null);
        }

        public NullKeySerializer(Class<Object> t) {
            super(t);
        }

        @Override
        public void serialize(Object nullKey, JsonGenerator jsonGenerator, SerializerProvider unused) throws IOException {
            jsonGenerator.writeFieldName("null");
        }
    }


    /**
     * 判断json格式是否合法
     *
     * @param str 字符串
     * @return true:为Json,false:不为Json
     */
    public static boolean isJson(String str) {
        try {
            mapper.readTree(str);
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    // region 字符串转其他


    /**
     * 字符串转对象
     */
    public static <T> T toObj(String str, Class<T> c) {
        if (!StringUtils.hasLength(str)) {
            return null;
        }
        T t = null;
        try {
            t = mapper.readValue(str, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * JSON字符串转为指定的类
     *
     * @param str 字符串
     * @param tr   指定类
     */
    public static <T> T toObj(String str, TypeReference<T> tr) {
        if (!StringUtils.hasLength(str)) {
            return null;
        }

        T t = null;
        try {
            t = (T) mapper.readValue(str, tr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) t;
    }

    /**
     * 将JSON转为MAP
     */
    public static Map toMap(String str) {
        try {
            return mapper.readValue(str, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("转换错误:" + e.getMessage());
        }
    }

    /**
     * json字符串转JsonNode
     */
    public static JsonNode toJsonNode(String jsonStr) {
        try {
            return mapper.readTree(jsonStr);
        } catch (Exception e) {
            throw new IllegalStateException("转换错误:" + e.getMessage());
        }
    }

    // endregion



    // region 对象转其他

    /**
     * 对象转为JSON
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        String s;
        try {
            s = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("对象无法转换为JSON:" + e.getMessage());
        }
        return s;
    }

    public static String toPrettyJson(Object obj) {
        if (obj == null) {
            return null;
        }
        String s;
        try {
            s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("对象无法转换为JSON:" + e.getMessage());
        }
        return s;
    }
    /**
     * 对象转JsonNode
     */
    public static JsonNode toJsonNode(Object obj) {
        try {
            return mapper.readTree(toJson(obj));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 对象转byte数组
     */
    public static byte[] toByte(Object obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    // endregion



    // region Map转其他

    /**
     * 将Map转成指定的Bean
     */
    public static <T> T toObj(Map map, Class<T> clazz) {
        try {
            return mapper.readValue(toJson(map), clazz);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // endregion



    // region Byte数组转其他


    /**
     * 数组转JsonNode
     */
    public static JsonNode byte2Node(byte[] bytes) {
        try {
            return mapper.readValue(bytes, JsonNode.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 数组转JsonNode
     */
    public static Object byte2Obj(byte[] bytes) {
        try {
            return mapper.readValue(bytes, Object.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * byte数组转Json
     */
    public static String byte2Json (byte[] bytes) {
        return new String(bytes);
    }
    // endregion



    // region JsonNode 转其他

    /**
     * JsonNode转byte[]
     */
    public static byte[] node2Byte(JsonNode node) {
        try {
            return mapper.writeValueAsBytes(node);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    // endregion

}
