package com.tuotiansudai.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SmsWrapperClient {

    static Logger logger = Logger.getLogger(SmsWrapperClient.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Value("${smswrapper.host}")
    private String host;

    @Value("${smswrapper.port}")
    private String port;

    @Value("${smswrapper.context}")
    private String context;

    private final static String URL_TEMPLATE = "http://{host}:{port}{context}{uri}";

    private final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final static String REGISTER_CAPTCHA_SMS_URI = "/sms/register-captcha";

    private final static String RETRIEVE_PASSWORD_CAPTCHA_URI = "/sms/retrieve-password-captcha";

    private final static String LOAN_OUT_INVESTOR_NOTIFY = "/sms/loan-out-investor-notify";

    private final static String PASSWORD_CHANGED_NOTIFY_URL = "/sms/mobile/{mobile}/password-changed-notify";

    @Autowired
    private OkHttpClient okHttpClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    public BaseDto<SmsDataDto> sendRegisterCaptchaSms(SmsCaptchaDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseString = post(REGISTER_CAPTCHA_SMS_URI, requestJson);
            if (!Strings.isNullOrEmpty(responseString)) {
                return mapper.readValue(responseString, new TypeReference<BaseDto<SmsDataDto>>(){});
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return getDefaultDto();
    }

    public BaseDto<SmsDataDto> sendInvestNotify(InvestSmsNotifyDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseString = post(LOAN_OUT_INVESTOR_NOTIFY, requestJson);
            if (!Strings.isNullOrEmpty(responseString)) {
                return mapper.readValue(responseString, new TypeReference<BaseDto<SmsDataDto>>(){});
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return getDefaultDto();
    }

    public BaseDto<SmsDataDto> sendRetrievePasswordCaptchaSms(SmsCaptchaDto dto) {
        try {
            String requestJson = objectMapper.writeValueAsString(dto);
            String responseString = post(RETRIEVE_PASSWORD_CAPTCHA_URI, requestJson);
            if (!Strings.isNullOrEmpty(responseString)) {
                return mapper.readValue(responseString, new TypeReference<BaseDto<SmsDataDto>>(){});
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return getDefaultDto();
    }

    public BaseDto<SmsDataDto> sendPasswordChangedNotify(String mobile){
        try {
            String responseString = post(PASSWORD_CHANGED_NOTIFY_URL.replace("{mobile}", mobile), "");
            if (!Strings.isNullOrEmpty(responseString)) {
                return mapper.readValue(responseString, new TypeReference<BaseDto<SmsDataDto>>(){});
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return getDefaultDto();
    }

    public BaseDto<MonitorDataDto> monitor() {
        String url = URL_TEMPLATE.replace("{host}", host).replace("{port}", port).replace("{context}", context).replace("{uri}", "/monitor");

        Request request = new Request.Builder().url(url).get().addHeader("Content-Type", "application/json; charset=UTF-8").build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                return mapper.readValue(jsonData, new TypeReference<BaseDto<MonitorDataDto>>(){});
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        BaseDto<MonitorDataDto> resultDto = new BaseDto<>();
        MonitorDataDto dataDto = new MonitorDataDto();
        dataDto.setStatus(false);
        resultDto.setData(dataDto);

        return resultDto;
    }

    private String post(String path, String requestJson) {
        String url = URL_TEMPLATE.replace("{host}", host).replace("{port}", port).replace("{context}", context).replace("{uri}", path);

        RequestBody body = RequestBody.create(JSON, requestJson);

        Request request = new Request.Builder().url(url).post(body).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private BaseDto<SmsDataDto> getDefaultDto() {
        BaseDto<SmsDataDto> resultDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        resultDto.setData(dataDto);
        return resultDto;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
