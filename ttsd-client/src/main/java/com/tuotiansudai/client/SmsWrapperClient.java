package com.tuotiansudai.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.sms.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

@Component
public class SmsWrapperClient extends BaseClient {

    static Logger logger = Logger.getLogger(SmsWrapperClient.class);

    @Value("${sms.host}")
    protected String host;

    @Value("${sms.port}")
    protected String port;

    @Value("${sms.application.context}")
    protected String applicationContext;

    @Value("${common.environment}")
    private Environment environment;

    private final static String SMS_URL = "/sms";

    private final static String FATAL_NOTIFY_URL = "/sms/fatal-notify";

    public BaseDto<SmsDataDto> sendSms(SmsDto smsDto){
        return send(smsDto, SMS_URL);
    }

    public BaseDto<SmsDataDto> sendFatalNotify(SmsFatalNotifyDto notify) {
        return send(notify, FATAL_NOTIFY_URL);
    }

    private BaseDto<SmsDataDto> send(Object requestData, String requestPath) {
        BaseDto<SmsDataDto> resultDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        resultDto.setData(dataDto);

        if (Lists.newArrayList(Environment.DEV, Environment.SMOKE).contains(environment)) {
            logger.info(MessageFormat.format("Environment is {0}, ignore sms", environment.name()));
            dataDto.setStatus(true);
            return resultDto;
        }

        try {
            String requestJson = requestData == null ? "" : objectMapper.writeValueAsString(requestData);
            String responseString = this.execute(requestPath, requestJson, "POST");
            if (!Strings.isNullOrEmpty(responseString)) {
                return objectMapper.readValue(responseString, new TypeReference<BaseDto<SmsDataDto>>() {
                });
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return resultDto;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(String applicationContext) {
        this.applicationContext = applicationContext;
    }
}
