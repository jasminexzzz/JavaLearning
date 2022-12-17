package com.xzzz.sbspringboot.factory;

import org.springframework.stereotype.Component;

/**
 * @author jasmineXz
 */
@Component
public class ClientInner implements Client {

    @Override
    public boolean checkClient(String clientType) {
        return false;
    }
}
