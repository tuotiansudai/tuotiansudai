package com.tuotiansudai.smswrapper.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.client.CheckSumBuilder;
import com.tuotiansudai.smswrapper.exception.SmsSendingException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SmsProviderNetease implements SmsProvider {

    private static Logger logger = Logger.getLogger(SmsProviderNetease.class);

    private String url;
    private String appKey;
    private String appSecret;

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

    private ObjectMapper objectMapper = new ObjectMapper();

    private HttpClient httpClient = HttpClientBuilder.create().build();

    public void sendSMS(List<String> mobileList, SmsTemplate smsTemplate, List<String> paramList) throws SmsSendingException {
        String templateId = smsTemplate.getTemplateIdNetease();
        HttpPost httpPost = createHttpPostRequest();
        try {
            httpPost.setEntity(buildRequestEntity(mobileList, templateId, paramList));
        } catch (JsonProcessingException e) {
            throw new SmsSendingException(mobileList, smsTemplate, paramList, "build request entity failed: " + e.getMessage(), e);
        }
        try {
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            // 执行结果  {"code":200,"msg":"sendid","obj":1}
            String responseText = EntityUtils.toString(response.getEntity(), "utf-8");
            String responseCode = getRetCode(responseText);
            boolean success = HttpStatus.OK.value() == statusCode && "200".equals(responseCode);
            if (!success) {
                throw new SmsSendingException(mobileList, smsTemplate, paramList, String.format("send sms failed, http status: %d, response: %s", statusCode, responseText));
            }
        } catch (IOException e) {
            throw new SmsSendingException(mobileList, smsTemplate, paramList, "post sms request failed: " + e.getMessage(), e);
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

    private String getRetCode(String responseBody) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("code").asText();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}
