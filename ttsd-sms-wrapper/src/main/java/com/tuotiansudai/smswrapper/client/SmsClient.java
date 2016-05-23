package com.tuotiansudai.smswrapper.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.repository.mapper.BaseMapper;
import com.tuotiansudai.smswrapper.repository.model.SmsModel;
import com.tuotiansudai.util.SpringContextUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Introspector;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SmsClient {

    static Logger logger = Logger.getLogger(SmsClient.class);

    private static String SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE = "sms_ip_restricted:{0}";

    @Value("${sms.netease.url}")
    private String url;

    @Value("${sms.netease.appKey}")
    private String appKey;

    @Value("${sms.netease.appSecret}")
    private String appSecret;

    @Value("${sms.interval.seconds}")
    private int second;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static HttpClient httpClient;

    private HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClientBuilder.create().build();
        }
        return httpClient;
    }


    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, String mobile, SmsTemplate template, String param, String restrictedIP) {
        List<String> mobileList = Lists.newArrayList(mobile);
        List<String> paramList = Lists.newArrayList(param);
        return sendSMS(baseMapperClass, mobileList, template, paramList, restrictedIP);
    }

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, String mobile, SmsTemplate template, List<String> paramList, String restrictedIP) {
        List<String> mobileList = Lists.newArrayList(mobile);
        return sendSMS(baseMapperClass, mobileList, template, paramList, restrictedIP);
    }

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, List<String> mobileList, SmsTemplate template, List<String> paramList, String restrictedIP) {
        BaseDto<SmsDataDto> dto = new BaseDto<>();
        SmsDataDto data = new SmsDataDto();
        dto.setData(data);

        String redisKey = MessageFormat.format(SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE, restrictedIP);

        if (redisWrapperClient.exists(redisKey)) {
            data.setStatus(false);
            data.setIsRestricted(true);
            return dto;
        }

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

        try {
            String mobileJson = objectMapper.writeValueAsString(mobileList);
            String paramJson = objectMapper.writeValueAsString(paramList);

            //设置请求的的参数
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("templateid", String.valueOf(template.getTemplateId())));
            nvps.add(new BasicNameValuePair("mobiles", mobileJson));
            nvps.add(new BasicNameValuePair("params", paramJson));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            // 执行请求
            HttpResponse response = getHttpClient().execute(httpPost);

            // 执行结果  {"code":200,"msg":"sendid","obj":1}
            String resultCode = getRetCode(EntityUtils.toString(response.getEntity(), "utf-8"));

            String content = template.generateContent(paramList);

            for (String mobile : mobileList) {
                this.createSmsModel(baseMapperClass, mobile, content, resultCode);
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        if (!Strings.isNullOrEmpty(restrictedIP)) {
            redisWrapperClient.setex(redisKey, second, "1");
        }
        data.setStatus(true);
        return dto;
    }

    @Transactional
    private void createSmsModel(Class<? extends BaseMapper> baseMapperClass, String mobile, String content, String resultCode) {
        SmsModel model = new SmsModel(mobile, content, resultCode);
        BaseMapper mapper = this.getMapperByClass(baseMapperClass);
        mapper.create(model);
    }

    private static String getRetCode(String responseBody) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("code").asText();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private BaseMapper getMapperByClass(Class clazz) {
        String fullName = clazz.getName();
        String[] strings = fullName.split("\\.");

        String beanName = Introspector.decapitalize(strings[strings.length - 1]);

        return (BaseMapper) SpringContextUtil.getBeanByName(beanName);
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
