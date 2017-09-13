package com.tuotiansudai.smswrapper.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.tuotiansudai.dto.sms.NeteaseSmsResponseDto;
import com.tuotiansudai.smswrapper.SmsChannel;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.client.CheckSumBuilder;
import com.tuotiansudai.smswrapper.exception.SmsSendingException;
import com.tuotiansudai.smswrapper.repository.mapper.BaseMapper;
import com.tuotiansudai.smswrapper.repository.mapper.NeteaseCallbackRequestMapper;
import com.tuotiansudai.smswrapper.repository.model.NeteaseCallbackRequestModel;
import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SmsProviderNetease extends SmsProviderBase {

    private static Logger logger = Logger.getLogger(SmsProviderNetease.class);

    private ObjectMapper objectMapper;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static HttpClient httpClient = HttpClientBuilder.create().build();

    private String url;
    private String appKey;
    private String appSecret;

    private final NeteaseCallbackRequestMapper neteaseCallbackRequestMapper;

    @Autowired
    public SmsProviderNetease(NeteaseCallbackRequestMapper neteaseCallbackRequestMapper) {
        this.neteaseCallbackRequestMapper = neteaseCallbackRequestMapper;
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Value("${sms.netease.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${sms.netease.appKey}")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Value("${sms.netease.appSecret}")
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public List<SmsHistoryModel> sendSMS(List<String> mobileList, SmsTemplate smsTemplate, List<String> paramList) {
        List<SmsHistoryModel> smsHistoryModels = this.createSmsHistory(mobileList, smsTemplate, paramList, SmsChannel.NETEASE);

        try {
            String templateId = smsTemplate.getTemplateId(SmsChannel.NETEASE);
            HttpPost httpPost = createHttpPostRequest();
            httpPost.setEntity(buildRequestEntity(mobileList, templateId, paramList));
            HttpResponse response = httpClient.execute(httpPost);
            String responseText = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8); // 执行结果  {"code":200,"msg":"sendid","obj":1}

            logger.info(String.format("send sms, mobileList: [%s], content: %s, channel: %s, response: %s",
                    String.join(",", mobileList),
                    smsTemplate.generateContent(paramList, SmsChannel.NETEASE),
                    SmsChannel.NETEASE,
                    responseText));

            NeteaseSmsResponseDto neteaseSmsResponseDto = objectMapper.readValue(responseText, NeteaseSmsResponseDto.class);

            redisWrapperClient.hset(MessageFormat.format("sendid:{0}", neteaseSmsResponseDto.getObj()),
                    "template", smsTemplate.name(), 300);

            redisWrapperClient.hset(MessageFormat.format("sendid:{0}", neteaseSmsResponseDto.getObj()),
                    "params", Joiner.on("|").join(paramList), 300);

            this.initSmsCallback(smsHistoryModels, neteaseSmsResponseDto);
            return this.updateSmsHistory(smsHistoryModels, neteaseSmsResponseDto.isSuccess(), responseText);
        } catch (IOException e) {
            logger.error(String.format("send sms, mobileList: [%s], content: %s, channel: %s",
                    String.join(",", mobileList),
                    smsTemplate.generateContent(paramList, SmsChannel.NETEASE),
                    SmsChannel.NETEASE), e);
        }

        return smsHistoryModels;
    }

    private void initSmsCallback(List<SmsHistoryModel> smsHistoryModels, NeteaseSmsResponseDto neteaseSmsResponse) {
        for (SmsHistoryModel smsHistoryModel : smsHistoryModels) {
            neteaseCallbackRequestMapper.create(new NeteaseCallbackRequestModel(smsHistoryModel.getId(), smsHistoryModel.getMobile(), neteaseSmsResponse.getObj()));
        }
    }

    private HttpPost createHttpPostRequest() {
        String nonce = String.valueOf((int) (Math.random() * 10000000));
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);

        HttpPost httpPost = new HttpPost(url);
        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        return httpPost;
    }

    private HttpEntity buildRequestEntity(List<String> mobileList, String templateId, List<String> paramList) throws JsonProcessingException {
        String mobileJson = objectMapper.writeValueAsString(mobileList);
        String paramJson = objectMapper.writeValueAsString(paramList);

        //设置请求的的参数
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("templateid", templateId));
        nvps.add(new BasicNameValuePair("mobiles", mobileJson));
        nvps.add(new BasicNameValuePair("params", paramJson));

        return new UrlEncodedFormEntity(nvps, Charset.forName("utf-8"));
    }
}
