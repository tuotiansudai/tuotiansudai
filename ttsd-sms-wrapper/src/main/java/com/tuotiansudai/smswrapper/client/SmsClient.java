package com.tuotiansudai.smswrapper.client;

import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.smswrapper.repository.mapper.BaseMapper;
import com.tuotiansudai.smswrapper.repository.model.SmsModel;
import com.tuotiansudai.util.SpringContextUtil;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Introspector;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;

@Service
public class SmsClient {

    static Logger logger = Logger.getLogger(SmsClient.class);

    private static String REQUEST_BODY_TEMPLATE = "sn={0}&pwd={1}&mobile={2}&content={3}&ext=&stime=&rrid=";

    private static MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private static String SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE = "sms_ip_restricted:{0}";

    public static final char[] DIGIT = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    @Value("${sms.zucp.url}")
    private String url;

    @Value("${sms.zucp.sn}")
    private String sn;

    @Value("${sms.zucp.password}")
    private String password;

    @Value("${sms.interval.seconds}")
    private int second;

    @Autowired
    private OkHttpClient httpClient;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, String mobile, String content, String restrictedIP) {
        BaseDto<SmsDataDto> dto = new BaseDto<>();
        SmsDataDto data = new SmsDataDto();
        dto.setData(data);

        String redisKey = MessageFormat.format(SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE, restrictedIP);

        if (redisWrapperClient.exists(redisKey)) {
            data.setStatus(false);
            data.setIsRestricted(true);
            return dto;
        }

        String requestBody = this.generateRequestBody(mobile, content);
        if (!Strings.isNullOrEmpty(requestBody)) {
            RequestBody okRequestBody = RequestBody.create(MEDIA_TYPE, requestBody);
            Request request = new Request.Builder()
                    .url(this.url)
                    .post(okRequestBody)
                    .build();
            try {
                Response response = httpClient.newCall(request).execute();
                String responseBody = response.body().string();
                String resultCode = this.parseResponse(responseBody);
                this.createSmsModel(baseMapperClass, mobile, content, resultCode);
                data.setStatus(true);
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        if (!Strings.isNullOrEmpty(restrictedIP)) {
            redisWrapperClient.setex(redisKey, second, mobile);
        }

        return dto;
    }

    @Transactional
    private void createSmsModel(Class<? extends BaseMapper> baseMapperClass, String mobile, String content, String resultCode) {
        SmsModel model = new SmsModel(mobile, content, resultCode);
        BaseMapper mapper = this.getMapperByClass(baseMapperClass);
        mapper.create(model);
    }

    private String generateRequestBody(String mobile, String content) {
        String md5Password = this.getMD5();
        if (Strings.isNullOrEmpty(md5Password)) {
            return null;
        }
        return MessageFormat.format(REQUEST_BODY_TEMPLATE, this.sn, md5Password, mobile, content);
    }

    private String getMD5() {
        String source = this.sn + this.password;
        StringBuilder target = new StringBuilder();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(source.getBytes());
            byte[] digestValues = md5.digest();
            for (byte digestValue : digestValues) {
                target.append(new String(new char[]{DIGIT[(digestValue >>> 4) & 0X0F], DIGIT[digestValue & 0X0F]}));
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return target.toString();
    }

    private String parseResponse(String responseBody) {
        try {
            Document document = DocumentHelper.parseText(responseBody);
            Element rootElement = document.getRootElement();
            return rootElement.getText();
        } catch (DocumentException e) {
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
