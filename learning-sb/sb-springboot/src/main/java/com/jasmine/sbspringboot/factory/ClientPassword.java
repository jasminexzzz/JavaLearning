package com.jasmine.sbspringboot.factory;

import org.springframework.stereotype.Component;

/**
 * @author jasmineXz
 */
@Component
public class ClientPassword implements Client {

    @Override
    public boolean checkClient(String clientType) {
        return false;
    }
}
