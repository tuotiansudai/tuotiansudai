package com.tuotiansudai.smswrapper.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import com.tuotiansudai.smswrapper.SmsChannel;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import org.apache.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SmsProviderAlidayu extends SmsProviderBase {

    private static Logger logger = Logger.getLogger(SmsProviderAlidayu.class);

    private final static String SIGN_NAME = "拓天速贷";

    private String url;
    private String appKey;
    private String appSecret;

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

    @Override
    public List<SmsHistoryModel> sendSMS(List<String> mobileList, SmsTemplate smsTemplate, List<String> paramList) {
        List<SmsHistoryModel> smsHistoryModels = this.createSmsHistory(mobileList, smsTemplate, paramList, SmsChannel.ALIDAYU);

        try {
            String templateId = smsTemplate.getTemplateId(SmsChannel.ALIDAYU);
            TaobaoClient client = new DefaultTaobaoClient(url, appKey, appSecret);
            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            req.setExtend(MDC.get("requestId"));
            req.setSmsType("normal");
            req.setSmsFreeSignName(SIGN_NAME);
            req.setRecNum(String.join(",", mobileList));
            req.setSmsTemplateCode(templateId);
            req.setSmsParamString(generateParamString(paramList));
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);

            logger.info(String.format("send sms, mobileList: [%s], content: %s, channel: %s, response: %s",
                    String.join(",", mobileList),
                    smsTemplate.generateContent(paramList, SmsChannel.ALIDAYU),
                    SmsChannel.ALIDAYU,
                    rsp.getBody()));

            boolean success = rsp.isSuccess() && rsp.getResult().getSuccess();

            return this.updateSmsHistory(smsHistoryModels, success, rsp.getBody());
        } catch (Exception e) {
            logger.error(String.format("send sms, mobileList: [%s], content: %s, channel: %s",
                    String.join(",", mobileList),
                    smsTemplate.generateContent(paramList, SmsChannel.ALIDAYU),
                    SmsChannel.ALIDAYU), e);
        }

        return smsHistoryModels;
    }

    private String generateParamString(List<String> paramList) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < paramList.size(); i++) {
            map.put("param" + i, paramList.get(i));
        }
        return objectMapper.writeValueAsString(map);
    }
}
