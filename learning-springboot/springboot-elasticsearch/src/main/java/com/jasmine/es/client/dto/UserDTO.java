package com.jasmine.es.client.dto;

import com.jasmine.es.repository.dto.PermDTO;
import com.jasmine.es.repository.dto.UserRoleDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author wangyf
 * @since 0.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends EsBaseDTO {
    private String userId;
    private String name;
    private String address;
    private UserRoleDTO role;
    private List<PermDTO> perms;
    private Integer age;
    private String birthday;
}
