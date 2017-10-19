package com.tuotiansudai.smswrapper.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public abstract class SmsProviderAlidayuBase extends SmsProviderBase {

    final static String SIGN_NAME = "拓天速贷";

    String url;
    String appKey;
    String appSecret;

    @Value("${sms.alidayu.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${sms.alidayu.appKey}")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Value("${sms.alidayu.appSecret}")
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    String generateParamString(List<String> paramList) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < paramList.size(); i++) {
            map.put("param" + i, paramList.get(i));
        }
        return objectMapper.writeValueAsString(map);
    }
}
