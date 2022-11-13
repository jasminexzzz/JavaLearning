package com.xzzz.C1_framework.Properties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * <h2>获取配置文件</h2>
 * <p>支持格式如下:
 * <ol>
 * <li>({@link String})key: ({@link String})value</li>
 * <li>({@link String})key: ({@link ArrayList})value1,value2,value3</li>
 * <li>({@link String})key: ({@link HashMap})key1:value1,key2:value2,key3:value3</li>
 * </ol>
 *
 * <h2>注意</h2>
 * <ol>
 * <li>配置中的空格将完整的返回</li>
 * <li>配置文件名必须以 / 开头, 配置文件必须在 classpath 路径下</li>
 * </ol>
 */
public class PropertiesConfig {

    /**
     * 配置文件对象
     */
    private static Properties properties = null;

    /**
     * 配置文件路径
     */
    private final String resourceName;

    private PropertiesConfig(String resourceName) {
        this.resourceName = resourceName;
        try (InputStream in = PropertiesConfig.class.getResourceAsStream(resourceName)) {
            properties = new Properties();
            if (in == null) {
                throw new NullPointerException(String.format("未找到配置文件 [%s]", resourceName));
            }

            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 获取配置文件名
     */
    public String getResourceName() {
        return this.resourceName;
    }

    /**
     * 获取配置
     *
     * @param key 配置的键
     * @return 配置的值
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取配置对象
     *
     * @return
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * 获取集合配置, 格式需要满足 ({@link String})key: ({@link ArrayList})value1,value2,value3
     *
     * @param key 配置的键
     * @return 配置的值
     */
    public List<String> getPropertyAsList(String key) {
        String value = this.getProperty(key);
        if (StrUtil.isBlank(value)) {
            return new ArrayList<>();
        }
        if (value.contains(",")) {
            return new ArrayList<>(Arrays.asList(value.split(",")));
        }
        return CollUtil.newArrayList(value);
    }

    /**
     * 获取 map 配置, 格式需要满足 ({@link String})key: ({@link HashMap})key1:value1,key2:value2,key3:value3
     *
     * @param key 配置的键
     * @return 配置的值
     */
    public HashMap<String, String> getPropertyAsMap(String key) {
        String value = this.getProperty(key);
        HashMap<String, String> result = new HashMap<>();
        if (StrUtil.isBlank(value)) {
            return result;
        }
        if (value.contains(",")) {
            for (String keyValues : value.split(",")) {
                result.putAll(splitMap(keyValues));
            }
        } else {
            result.putAll(splitMap(value));
        }
        return result;
    }

    private Map<String, String> splitMap(String keyValue) {
        Map<String, String> result = new HashMap<>();
        if (keyValue.contains(":")) {
            String[] keyAndValue = keyValue.split(":");
            result.put(keyAndValue[0], keyAndValue[1]);
        } else {
            result.put(keyValue, null);
        }
        return result;
    }

    //region 建造者类

    /**
     * 创建建造者类
     */
    public static ConfigBuilder builder() {
        return new ConfigBuilder();
    }

    /**
     * PropertiesConfig 的建造者类
     */
    public static class ConfigBuilder {

        /**
         * 配置文件名称, 配置文件是大小写不敏感的, 但是不推荐使用大写
         * <p> 如果以 '/' 开头, 则配置文件需要在 ClassPath 根目录下
         */
        private String resourceName;

        public PropertiesConfig build() {
            return new PropertiesConfig(this.resourceName);
        }

        public ConfigBuilder resourceName(String resourceName) {
            if (StrUtil.isBlank(resourceName)) {
                throw new IllegalArgumentException("配置文件名不得为空");
            }
            if (!StrUtil.startWith(resourceName, '/')) {
                throw new IllegalArgumentException("配置文件名必须以 [/] 字符开头");
            }
            this.resourceName = resourceName;
            return this;
        }


    }
    //endregion

}
