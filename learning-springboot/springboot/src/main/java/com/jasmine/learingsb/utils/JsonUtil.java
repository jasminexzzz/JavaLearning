package com.jasmine.learingsb.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 * @author : jasmineXz
 */
@SuppressWarnings("ALL")
public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(myDateFormat);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);    // 允许出现特殊字符和转义符
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);             // 允许出现单引号
//        mapper.setSerializationInclusion(Include.NON_NULL);                             // 忽视为null的字段
    }

    /**
     * 判断json格式是否合法
     *
     * @param str 字符串
     * @return true:为Json,false:不为Json
     */
    public static boolean isJsonValid(String str) {
        try {
            mapper.readTree(str);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 对象转为JSON
     *
     * @param o 被转换的对象
     */
    public static String obj2Json(Object obj) {
        if (obj == null) {
            return null;
        }
        String s = null;

        try {
            s = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 将集合转换为JSON
     *
     * @param objects 被转换的集合
     */
    public static <T> List<String> listObject2ListJson(List<T> objects) {
        if (objects == null) {
            return null;
        }
        List<String> lists = new ArrayList<>();
        for (T t : objects) {
            lists.add(JsonUtil.obj2Json(t));
        }

        return lists;
    }

    /**
     * 将保存JSON的集合转换为保存指定对象的集合
     *
     * @param jsons 保存JSON的集合
     * @param c 转换后的对象
     */
    public static <T> List<T> listJson2ListObject(List<String> jsons, Class<T> c) {
        if (jsons == null) {
            return null;
        }
        List<T> ts = new ArrayList<>();
        for (String j : jsons) {
            ts.add(JsonUtil.json2Object(j, c));
        }

        return ts;
    }

    /**
     * 将集合转换为指定对象
     *
     * @param json JSON字符串
     * @param c 转换后的对象类型
     */
    public static <T> T json2Object(String json, Class<T> c) {
        if (!StringUtils.hasLength(json)) {
            return null;
        }
        T t = null;
        try {
            t = mapper.readValue(json, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * JSON字符串转为指定的类
     *
     * @param json 字符串
     * @param tr 指定类
     */
    @SuppressWarnings("unchecked")
    public static <T> T json2Object(String json, TypeReference<T> tr) {
        if (!StringUtils.hasLength(json)) {
            return null;
        }

        T t = null;
        try {
            t = (T) mapper.readValue(json, tr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) t;
    }

    /**
     * JSON字符串序列化
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String serialize(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * JSON字符串反序列化
     *
     * @param jsonStr JSON字符串
     * @return a Map
     */
    public static Map deserialize(String jsonStr) {
        try {
            return deserialize(jsonStr, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }
    }

    public static <T> T deserialize(String jsonStr, Class<T> classType) throws Exception {
        return mapper.readValue(jsonStr, classType);
    }

    /**
     * 将Map转成指定的Bean
     *
     * @param map    Map
     * @param clazz  指定类
     */
    public static Object map2Bean(Map map, Class clazz){
        try {
            return mapper.readValue(obj2Json(map), clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap();
        }
    }

    /**
     * 将Bean转成Map
     */
    public static Map bean2Map(Object obj){
        try {
            return mapper.readValue(obj2Json(obj), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap();
        }
    }

    /**
     * 将JSON转为MAP
     */
    public static Map json2Map(String jsonStr){
        try {
            return mapper.readValue(jsonStr, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap();
        }
    }

    /**
     * json字符串转JsonNode
     *
     * @param jsonStr
     * @return
     */
    public static JsonNode readNode(String jsonStr) {
        try {
            return mapper.readTree(jsonStr);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 对象转JsonNode
     *
     * @param jsonStr
     * @return
     */
    public static JsonNode obj2Node(Object o) {
        try {
            return mapper.readTree(obj2Json(o));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 数组转JsonNode
     * @param bytes
     * @param clazz
     * @return
     */
    public static JsonNode byte2Node (byte[] bytes) {
        try {
            return mapper.readValue(bytes, JsonNode.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * JsonNode转byte[]
     * @param node
     * @return
     */
    public static byte[] node2Byte (JsonNode node){
        try {
            return mapper.writeValueAsBytes(node);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 数组转JsonNode
     * @param bytes
     * @param clazz
     * @return
     */
    public static Object byte2Obj (byte[] bytes) {
        try {
            return mapper.readValue(bytes,Object.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * JsonNode转byte[]
     * @param node
     * @return
     */
    public static byte[] obj2Byte (Object obj){
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }



    public static void main(String[] args) {
        String s = "{\"wwww\":\"333333\"}";

        JsonNode j = JsonUtil.byte2Node(s.getBytes());

        System.out.println(j);
    }

}
