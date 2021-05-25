package com.jasmine.es.client;

import com.jasmine.es.client.dto.ItemDTO;
import com.jasmine.es.client.manager.EsHighLevelClientManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author wangyf
 * @since 0.0.1
 */

@Slf4j
@RestController
@RequestMapping("/es/client/add")
public class PostClientController {

    @Autowired
    private EsHighLevelClientManager manager;

    @PostMapping
    public void addDoc (@RequestBody ItemDTO user) {
        manager.add(user);
    }

    /**
     * 批量新增
     */
    @PostMapping("/batch")
    public void addBatch (@RequestBody List<ItemDTO> users) {
        manager.addBatch(users);
    }

}
