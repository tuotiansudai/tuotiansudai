package com.tuotiansudai.smswrapper.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.exception.SmsSendingException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SmsProviderAlidayu implements SmsProvider {

    private String url;
    private String appKey;
    private String appSecret;
    private String signName = "拓天速贷";

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

    public void setSignName(String signName) {
        this.signName = signName;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void sendSMS(List<String> mobileList, SmsTemplate smsTemplate, List<String> paramList) throws SmsSendingException {
        String templateId = smsTemplate.getTemplateIdAlidayu();
        TaobaoClient client = new DefaultTaobaoClient(url, appKey, appSecret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend(MDC.get("requestId"));
        req.setSmsType("normal");
        req.setSmsFreeSignName(signName);
        req.setRecNum(String.join(",", mobileList));
        req.setSmsTemplateCode(templateId);
        try {
            req.setSmsParamString(generateParamString(paramList));
        } catch (JsonProcessingException e) {
            throw new SmsSendingException(mobileList, smsTemplate, paramList, "build request entity failed: " + e.getMessage(), e);
        }
        try {
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            boolean success = rsp.isSuccess() && rsp.getResult().getSuccess();
            if (!success) {
                throw new SmsSendingException(mobileList, smsTemplate, paramList,
                        String.format("send sms failed, errorCode: %s, message: %s, response: %s",
                                rsp.getErrorCode(), rsp.getMsg(), rsp.getBody()));
            }
        } catch (ApiException e) {
            throw new SmsSendingException(mobileList, smsTemplate, paramList, "post sms request failed: " + e.getMessage(), e);
        }
    }

    private String generateParamString(List<String> paramList) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < paramList.size(); i++) {
            map.put("param" + i, paramList.get(i));
        }
        return objectMapper.writeValueAsString(map);
    }
}
