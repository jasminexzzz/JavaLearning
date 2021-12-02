package com.jasmine.java.high.工具类.阿里云.OSS;

/**
 * @author : jasmineXz
 */
public class AliTokenDTO {

    private String accessKeyId;

    private String accessKeySecret;

    private String accessToken;

    private Long durationSeconds;

    private String endpoint;

    private String bucket;

    private String filePath;

    public AliTokenDTO() {}

    public AliTokenDTO(String accessKeyId, String accessKeySecret, String accessToken, Long durationSeconds, String endpoint, String bucket, String filePath) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.accessToken = accessToken;
        this.durationSeconds = durationSeconds;
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.filePath = filePath;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
