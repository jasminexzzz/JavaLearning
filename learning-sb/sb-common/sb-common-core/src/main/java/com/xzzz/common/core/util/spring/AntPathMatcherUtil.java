package com.xzzz.common.core.util.spring;

import org.springframework.util.AntPathMatcher;

/**
 * @author : jasmineXz
 */
public class AntPathMatcherUtil {
    private static AntPathMatcher antPathMatcher = new AntPathMatcher();


    public static void main(String[] args) {
        System.out.println(antPathMatcher.match("/a/b/*","/a/b"));
    }
}
