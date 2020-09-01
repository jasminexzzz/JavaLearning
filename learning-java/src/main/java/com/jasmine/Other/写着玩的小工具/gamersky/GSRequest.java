package com.jasmine.Other.写着玩的小工具.gamersky;

/**
 * 请求对象
 */
public class GSRequest {

    private String articleId;
    private int minPraisesCount;
    private int repliesMaxCount;
    private int pageIndex;
    private int pageSize;
    private String order;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public int getMinPraisesCount() {
        return minPraisesCount;
    }

    public void setMinPraisesCount(int minPraisesCount) {
        this.minPraisesCount = minPraisesCount;
    }

    public int getRepliesMaxCount() {
        return repliesMaxCount;
    }

    public void setRepliesMaxCount(int repliesMaxCount) {
        this.repliesMaxCount = repliesMaxCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "GSRequest{" +
                "articleId='" + articleId + '\'' +
                ", minPraisesCount=" + minPraisesCount +
                ", repliesMaxCount=" + repliesMaxCount +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", order='" + order + '\'' +
                '}';
    }
}
