package com.jasmine.java.high.spi;

import java.util.ServiceLoader;

/**
 * @author wangyf
 * @since 2.0.0
 */
public class ZTest {

    public static void main(String[] args) {
        ServiceLoader<InterfaceSPI> spiImplList = ServiceLoaderUtil.getServiceLoader(InterfaceSPI.class);
        for (InterfaceSPI impl : spiImplList) {
            System.out.println(impl.getClass().getSimpleName());
        }
    }
}
