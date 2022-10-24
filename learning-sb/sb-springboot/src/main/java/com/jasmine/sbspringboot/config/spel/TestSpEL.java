package com.xzzz.sbspringboot.config.spel;

import com.xzzz.sbspringboot.config.singleton.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author : jasmineXz
 */
@Slf4j
@RestController
@RequestMapping("spel")
public class TestSpEL {

    @Autowired
    private Singleton singleton;
}
