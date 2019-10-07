package com.jasmine.框架学习.Log4J2;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : jasmineXz
 */
public class test {
    private static final Logger log = LoggerFactory.getLogger(test.class);
    public static void main(String[] args) {
        log.info("=====> info");
        log.debug("=====> debug");
        log.error("=====> error");
        log.warn("=====> warn");
        log.trace("=====> trace");
    }
}
