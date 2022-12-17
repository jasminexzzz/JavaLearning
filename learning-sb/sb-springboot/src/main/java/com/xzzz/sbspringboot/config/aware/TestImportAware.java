package com.xzzz.sbspringboot.config.aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 实现 importAware 接口可以获取被该注解所注解的类
 *
 * @author : jasmineXz
 */
@Slf4j
@Component
public class TestImportAware implements ImportAware {

    private String value;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        log.trace(String.valueOf(importMetadata.getClass()));
        Map<String, Object> attributesMap = importMetadata.getAnnotationAttributes(EnableTestImportAware.class.getName());
        AnnotationAttributes attrs = AnnotationAttributes.fromMap(attributesMap);
        this.value = attrs.getString("value");
        log.trace(value);
    }

}
