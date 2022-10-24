package com.xzzz.Other.MyUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * JSON工具类
 * @author : jasmineXz
 */
public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(myDateFormat);
        //设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许出现特殊字符和转义符
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 允许出现单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 忽视为null的字段
        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * 判断json格式是否合法
     * @param str 字符串
     */
    public static boolean isJSONValid2(String str) {
        try {
            mapper.readTree(str);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 对象转为JSON
     * @param o 被转换的对象
     */
    public static String object2Json(Object o) {
        if (o == null) {
            return null;
        }

        String s = null;

        try {
            s = mapper.writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 将集合转换为JSON
     * @param objects 被转换的集合
     */
    public static <T> List<String> listObject2ListJson(List<T> objects) {
        if (objects == null) {
            return null;
        }

        List<String> lists = new ArrayList<>();
        for (T t : objects) {
            lists.add(JsonUtil.object2Json(t));
        }

        return lists;
    }

    /**
     * 将保存JSON的集合转换为保存指定对象的集合
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
     * @param object 对象
     * @return JSON字符串
     */
    public static String serialize(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * JSON字符串反序列化
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
     * @param map
     * @param clazz
     * @return
     * @throws Exception
     */
    public static Object map2Bean(Map map, Class clazz) throws Exception {
        try {
            return mapper.readValue(object2Json(map), clazz);
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
            return mapper.readValue(object2Json(obj), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap();
        }
    }

    public static JsonNode readNode(String in) {
        try {
            return mapper.readTree(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    public static <T> T toObj(byte[] bytes, Class<T> clazz) {
        try {
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args){
//        SysResponse s = new SysResponse();
//        s.setCode(1);
//        s.setMsg("信息");
//        Map m = JsonUtil.bean2Map(s);
//        System.out.println(m.toString());
    }
}
