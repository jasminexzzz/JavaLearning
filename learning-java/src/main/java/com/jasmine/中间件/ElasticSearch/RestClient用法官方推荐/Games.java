package com.jasmine.中间件.ElasticSearch.RestClient用法官方推荐;

/**
 * @author : jasmineXz
 */
public class Games {

    private int id;
    private String name;
    private String content;

    public Games(int id,String name,String content){
        this.id = id;
        this.name = name;
        this.content = content;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
