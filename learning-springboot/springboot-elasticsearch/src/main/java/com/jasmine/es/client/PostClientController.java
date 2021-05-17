package com.jasmine.es.client;

import com.jasmine.es.client.dto.UserDTO;
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
    public void addDoc (@RequestBody UserDTO user) {
        manager.add(user);
    }

    /**
     * 批量新增
     */
    @PostMapping("/batch")
    public void addBatch (@RequestBody List<UserDTO> users) {
        manager.addBatch(users);
    }

}
