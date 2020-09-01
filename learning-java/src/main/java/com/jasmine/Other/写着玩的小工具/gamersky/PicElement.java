package com.jasmine.Other.写着玩的小工具.gamersky;

public class PicElement {
    private String path;
    private String url;

    public PicElement(String articleId,String url) {
        this.url = url;
        this.path = GSProperties.DOWNLOAD_PATH + "\\" + articleId + "\\";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
