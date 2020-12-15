package com.jasmine.moduledemo.autoconfig.clazz;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.QName;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import javax.annotation.PostConstruct;

/**
 * 需要module的父类包中引入了该类{@link QName}，此配置才会生效
 *
 * 因为module中引入的maven配置为 <optional>true</optional>
 * 所以该包只存在在module中，而不跟随module进入到父包中，若此配置要在
 * 父包生效，则父包需要显式引入该类所在的jar包
 */
@Slf4j
@ConditionalOnClass(QName.class)
public class TestConditionalOnClass {

    @PostConstruct
    public void init() {
        log.trace("==========> moduleDemo =>>> TestConditionalOnClass 生效");
    }
}
