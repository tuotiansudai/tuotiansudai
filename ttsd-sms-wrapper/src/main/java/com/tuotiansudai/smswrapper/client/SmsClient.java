package com.tuotiansudai.smswrapper.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.repository.mapper.BaseMapper;
import com.tuotiansudai.smswrapper.repository.model.SmsModel;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Introspector;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SmsClient implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static Logger logger = Logger.getLogger(SmsClient.class);

    private final static String SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE = "sms_ip_restricted:{0}";

    private final static String WYY_SMS_SEND_COUNT_BY_TODAY_TEMPLATE = "wyy_sms_send_count_by_today:{0}";

    private final static int sendSize = 100;

    private final static int lifeSecond = 172800;

    @Value("${sms.netease.url}")
    private String url;

    @Value("${sms.netease.appKey}")
    private String appKey;

    @Value("${sms.netease.appSecret}")
    private String appSecret;

    @Value("${sms.interval.seconds}")
    private int second;

    @Value("${common.environment}")
    private String environment;

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

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, List<String> mobileList, SmsTemplate template, List<String> paramList) {
        return sendSMS(baseMapperClass, mobileList, template, paramList, "");
    }

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, List<String> mobileList, SmsTemplate template, List<String> paramList, String restrictedIP) {
        BaseDto<SmsDataDto> dto = new BaseDto<>();
        SmsDataDto data = new SmsDataDto();
        dto.setData(data);

        if(Lists.newArrayList(Environment.SMOKE.name(),Environment.DEV.name()).contains(environment)){
            logger.info("[短信发送] 该环境不发送短信");
            return dto;
        }

        if(Environment.QA.name().equals(environment)){
            String redisKey = MessageFormat.format(WYY_SMS_SEND_COUNT_BY_TODAY_TEMPLATE, "SMS");
            String hKey = DateTime.now().withTimeAtStartOfDay().toString("yyyyMMdd");
            String redisValue = redisWrapperClient.hget(redisKey, hKey);

            int smsSendSize = Strings.isNullOrEmpty(redisValue) ? 0 : Integer.parseInt(redisValue);
            if(smsSendSize >= sendSize){
                logger.error(MessageFormat.format("[短信发送] OA环境今日已经发送过10条短信 {0}",redisKey));
                return dto;
            }
            smsSendSize++;
            redisWrapperClient.hset(redisKey, hKey, String.valueOf(smsSendSize), lifeSecond);
        }

        String redisKey = MessageFormat.format(SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE, restrictedIP);

        if (redisWrapperClient.exists(redisKey)) {
            data.setStatus(false);
            data.setIsRestricted(true);
            data.setMessage("this ip " + restrictedIP + " has sent in one second");
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

        String requestEntityString = "";
        try {
            String mobileJson = objectMapper.writeValueAsString(mobileList);
            String paramJson = objectMapper.writeValueAsString(paramList);

            //设置请求的的参数
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("templateid", String.valueOf(template.getTemplateId())));
            nvps.add(new BasicNameValuePair("mobiles", mobileJson));
            nvps.add(new BasicNameValuePair("params", paramJson));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            try {
                requestEntityString = EntityUtils.toString(httpPost.getEntity());
            } catch (Exception e) {
                logger.error("[SmsClient][sendSMS]parse request entity error.", e);
            }

            // 执行请求
            HttpResponse response = getHttpClient().execute(httpPost);

            // 执行结果  {"code":200,"msg":"sendid","obj":1}
            String resultCode = getRetCode(EntityUtils.toString(response.getEntity(), "utf-8"));
            if(!"200".equals(resultCode)) {
                logger.error(MessageFormat.format("[SmsClient][sendSMS]Send sms result fail.request:{0}, response:{1}",
                        EntityUtils.toString(httpPost.getEntity()), EntityUtils.toString(response.getEntity())));
            }
            dto.setSuccess(String.valueOf(HttpStatus.OK.value()).equals(resultCode));

            String content = template.generateContent(paramList);

            for (String mobile : mobileList) {
                this.createSmsModel(baseMapperClass, mobile, content, resultCode);
            }
        } catch (IOException e) {
            logger.error(MessageFormat.format("[SmsClient][sendSMS]Send sms result fail.request:{0}", requestEntityString), e);
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

        return (BaseMapper) applicationContext.getBean(beanName);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SmsClient.applicationContext = applicationContext;
    }
}
