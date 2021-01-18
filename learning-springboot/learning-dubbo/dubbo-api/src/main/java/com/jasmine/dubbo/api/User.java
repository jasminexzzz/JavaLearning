package com.jasmine.dubbo.api;

import java.io.Serializable;

/**
 * @author wangyf
 * @since 0.0.1
 */
public class User implements Serializable {

    private Integer id;
    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
