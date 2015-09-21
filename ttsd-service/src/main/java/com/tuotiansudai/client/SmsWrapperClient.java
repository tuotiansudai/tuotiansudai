package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestSmsNotifyDto;
import com.tuotiansudai.dto.MonitorDataDto;
import com.tuotiansudai.dto.SmsCaptchaDto;
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

    private final static String REGISTER_SMS_URI = "/sms/mobile/captcha/register";

    private final String MOBILE_RETRIEVE_PASSWORD_URI = "/sms/mobile/captcha/retrieve";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private ObjectMapper objectMapper = new ObjectMapper();

    private final String LOANOUT_INVESTOR_NOTIFY_URL = "/sms/mobile/loan-out-investor-notify";

    private final String PASSWORD_CHANGED_NOTIFY_URL = "/sms/mobile/{mobile}/password-changed-notify";

    @Autowired
    private OkHttpClient okHttpClient;

    private final static String URL_TEMPLATE = "http://{host}:{port}{context}{uri}";

    public BaseDto<BaseDataDto> sendSms(String mobile, String code, String ip) {
        BaseDto<BaseDataDto> resultDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        String requestJson = "";
        try {
            requestJson = objectMapper.writeValueAsString(new SmsCaptchaDto(mobile, code, ip));
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
            dataDto.setStatus(false);
            resultDto.setData(dataDto);
            return resultDto;
        }
        String url = URL_TEMPLATE.replace("{host}", host).replace("{port}", port).replace("{context}", context).replace("{uri}", REGISTER_SMS_URI);
        RequestBody body = RequestBody.create(JSON, requestJson);
        Request request = new Request.Builder().url(url).post(body).addHeader("Content-Type", "application/json; charset=UTF-8").build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                return mapper.readValue(jsonData, new TypeReference<BaseDto<BaseDataDto>>(){});
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        dataDto.setStatus(false);
        resultDto.setData(dataDto);
        return resultDto;
    }

    public BaseDto<BaseDataDto> sendInvestNotify(InvestSmsNotifyDto dto) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        String requestJson;
        try {
            requestJson = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
            dataDto.setStatus(false);
            baseDto.setData(dataDto);
            return baseDto;
        }
        String url = URL_TEMPLATE.replace("{host}", host).replace("{port}", port).replace("{context}", context).replace("{uri}", LOANOUT_INVESTOR_NOTIFY_URL);
        RequestBody body = RequestBody.create(JSON, requestJson);
        Request request = new Request.Builder().url(url).post(body).addHeader("Content-Type", "application/json; charset=UTF-8").build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                return mapper.readValue(jsonData, new TypeReference<BaseDto<BaseDataDto>>(){});
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        dataDto.setStatus(false);
        baseDto.setData(dataDto);
        return baseDto;
    }

    public BaseDto sendMobileRetrievePasswordSms(String mobile, String code, String ip) {
        BaseDto baseDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        String requestJson = "";
        try {
            requestJson = objectMapper.writeValueAsString(new SmsCaptchaDto(mobile, code, ip));
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
            dataDto.setStatus(false);
            baseDto.setData(dataDto);
            return baseDto;
        }
        String url = URL_TEMPLATE.replace("{host}", host).replace("{port}", port).replace("{context}", context).replace("{uri}", MOBILE_RETRIEVE_PASSWORD_URI);
        RequestBody body = RequestBody.create(JSON, requestJson);
        Request request = new Request.Builder().url(url).post(body).addHeader("Content-Type", "application/json; charset=UTF-8").build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                return mapper.readValue(jsonData, new TypeReference<BaseDto<BaseDataDto>>(){});
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        dataDto.setStatus(false);
        baseDto.setData(dataDto);
        return baseDto;
    }

    public BaseDto sendPasswordChangedNotify(String mobile){
        String uri = PASSWORD_CHANGED_NOTIFY_URL.replace("{mobile}", mobile);
        String url = URL_TEMPLATE.replace("{host}", host).replace("{port}", port).replace("{context}", context).replace("{uri}", uri);
        Request request = new Request.Builder().url(url).get().addHeader("Content-Type", "application/json; charset=UTF-8").build();

        BaseDto baseDto;

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                return mapper.readValue(jsonData, new TypeReference<BaseDto<BaseDataDto>>(){});
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        baseDto = new BaseDto();
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(false);
        baseDto.setData(dataDto);

        return baseDto;
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
