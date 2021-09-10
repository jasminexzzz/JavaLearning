package com.jasmine.es.client.biz.dto;

import com.jasmine.es.client.dto.EsSearchItemDTO;
import com.jasmine.es.repository.dto.PermDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ItemDTO extends EsSearchItemDTO {

    private String itemId;
    private String name;
    private Integer amt;
    private Date upTime;
    private String desc;


    private List<PermDTO> perms;
}
