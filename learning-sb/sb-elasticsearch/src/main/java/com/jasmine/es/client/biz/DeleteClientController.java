package com.xzzz.es.client.biz;

import com.xzzz.es.client.dto.EsBaseDTO;
import com.xzzz.es.client.manager.EsCurdManager;
import com.xzzz.es.client.manager.EsSearchManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/es/client/del")
public class DeleteClientController {

    @Autowired
    private EsCurdManager manager;

    @DeleteMapping
    public void deleteById (String index,String id) throws IOException {
        manager.delete(index,id);
    }

    @DeleteMapping("/batch")
    public void deleteById (@RequestBody List<EsBaseDTO> bases) {
        manager.delBatch(bases,false);
    }
}
