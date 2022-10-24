package com.xzzz.es.client.biz.dto;

import com.xzzz.es.client.dto.EsSearchItemDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author wangyf
 * @since 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ItemDTO extends EsSearchItemDTO {

    private Long id;

    private String itemName;

    private String subTitle;

    private Long retailPrice;

    private Long supplierId;

    private String supplierName;

    private String brandName;

    private Integer verifyStatus;

    private Integer salesNum;

    private Date gmtCreated;
}
