package com.xzzz.C1_framework.Properties;

public class TestConfig {

    public static void main(String[] args) {
        // 获取 classpath/config 下的 custom_config_path 配置文件
        PropertiesConfig customConfigPath = PropertiesConfig.builder().resourceName("/config/custom_config_path.properties").build();
        System.out.println(customConfigPath.getProperty("location"));


        // 获取 classpath 下的 custom_config 配置文件
        PropertiesConfig customConfig = PropertiesConfig.builder().resourceName("/custom_config.properties").build();
        System.out.println(customConfig.getProperty("location"));
        System.out.println(customConfig.getProperty("arr"));
        System.out.println(customConfig.getPropertyAsList("arr"));
        System.out.println(customConfig.getPropertyAsMap("map"));
    }
}
