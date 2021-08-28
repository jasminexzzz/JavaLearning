package com.jasmine.es.client.biz;

import com.jasmine.es.client.dto.EsSearchItemDTO;
import com.jasmine.es.client.biz.dto.ItemDTO;
import com.jasmine.es.client.dto.EsSearchDTO;
import com.jasmine.es.client.manager.EsCurdManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/es/client/search")
public class SearchClientController {

    @Autowired
    private EsCurdManager manager;


    @GetMapping("/test")
    public EsSearchDTO<ItemDTO> search (@ModelAttribute EsSearchDTO<ItemDTO> search) {
        return search(search,ItemDTO.class);
    }

    /**
     * 搜索
     * @param searchDTO 基本条件
     * @param clazz 转换对象
     * @param <T> 结果泛型
     * @return 搜索结果
     */
    private <T extends EsSearchItemDTO> EsSearchDTO<T> search (EsSearchDTO<T> searchDTO,Class<T> clazz) {
        return manager.simpleSearch(searchDTO, clazz);
    }
}
