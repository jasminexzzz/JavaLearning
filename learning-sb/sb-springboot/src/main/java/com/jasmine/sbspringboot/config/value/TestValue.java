package com.jasmine.sbspringboot.config.value;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author : jasmineXz
 */
@Slf4j
@Configuration
public class TestValue {

    List<ValueInterface> values;

//    @Autowired
//    public void setValue(@Value("#{ValueInterface.class}") List<ValueInterface> values){
//      values.forEach(item -> {
//          log.trace(item.getClass().getSimpleName());
//      });
//      this.values = values;
//    }
}
