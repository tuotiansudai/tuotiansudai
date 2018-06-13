package com.tuotiansudai.mq.config.setting;

import org.springframework.beans.factory.annotation.Value;

public class AliyunMnsSetting {

    @Value("${aliyun.mns.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.mns.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.mns.endpoint}")
    private String endpoint;

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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
