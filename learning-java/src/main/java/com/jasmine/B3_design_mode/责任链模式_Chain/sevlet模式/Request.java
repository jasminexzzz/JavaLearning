package com.jasmine.B3_design_mode.责任链模式_Chain.sevlet模式;

public class Request {

    String url;

    public Request(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
