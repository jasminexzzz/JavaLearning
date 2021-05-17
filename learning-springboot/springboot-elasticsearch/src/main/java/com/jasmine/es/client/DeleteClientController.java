package com.jasmine.es.client;

import com.jasmine.es.client.manager.EsHighLevelClientManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/es/client/del")
public class DeleteClientController {

    @Autowired
    private EsHighLevelClientManager manager;

    @DeleteMapping
    public void deleteById (String index,String id) throws IOException {
        manager.deleteById(index,id);
    }
}
