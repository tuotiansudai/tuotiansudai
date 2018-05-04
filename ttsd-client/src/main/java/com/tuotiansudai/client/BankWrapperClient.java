package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.etcd.ETCDConfigReader;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class BankWrapperClient extends BaseClient {

    private static Logger logger = Logger.getLogger(BankWrapperClient.class);

    protected String host;

    protected String port;

    protected String applicationContext;

    public BankWrapperClient() {
        ETCDConfigReader reader = ETCDConfigReader.getReader();
        this.host = reader.getValue("pay.host");
        this.port = reader.getValue("pay.port");
        this.applicationContext = reader.getValue("pay.application.context");

        this.okHttpClient.setConnectTimeout(180, TimeUnit.SECONDS);
        this.okHttpClient.setReadTimeout(180, TimeUnit.SECONDS);
        this.okHttpClient.setWriteTimeout(180, TimeUnit.SECONDS);
    }

    public BaseDto<PayFormDataDto> bindBankCard(String bankUserName, String bankAccountNo) {
        return asyncExecute(Maps.newHashMap(ImmutableMap.<String, String>builder().put("bankUserName", bankUserName).put("bankAccountNo", bankAccountNo).build()),
                "/user/card-bind");
    }

    public BaseDto<PayFormDataDto> unbindBankCard(String bankUserName, String bankAccountNo) {
        return asyncExecute(Maps.newHashMap(ImmutableMap.<String, String>builder().put("bankUserName", bankUserName).put("bankAccountNo", bankAccountNo).build()),
                "/user/cancel-card-bind");
    }

    private BaseDto<PayDataDto> syncExecute(Object requestData, String requestPath) {
        try {
            String responseJson = this.execute(requestPath, requestData != null ? objectMapper.writeValueAsString(requestData) : null, "POST");
            return this.parsePayResponseJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        PayDataDto payDataDto = new PayDataDto(false);
        return new BaseDto<>(false, payDataDto);
    }

    private BaseDto<PayFormDataDto> asyncExecute(Object requestData, String path) {
        try {
            String responseJson = this.execute(path, requestData != null ? objectMapper.writeValueAsString(requestData) : null, "POST");
            return this.parsePayFormJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        PayFormDataDto payFormDataDto = new PayFormDataDto();
        return new BaseDto<>(false, payFormDataDto);
    }

    private BaseDto<PayDataDto> parsePayResponseJson(String json) {
        PayDataDto payDataDto = new PayDataDto(false);
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        if (Strings.isNullOrEmpty(json)) {
            return baseDto;
        }

        try {
            baseDto = objectMapper.readValue(json, new TypeReference<BaseDto<PayDataDto>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            baseDto.setSuccess(false);
        }
        return baseDto;
    }

    private BaseDto<PayFormDataDto> parsePayFormJson(String json) {
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>(false, payFormDataDto);
        if (Strings.isNullOrEmpty(json)) {
            return baseDto;
        }

        try {
            Map<String, String> data = objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
            payFormDataDto.setUrl(data.get("url"));
            payFormDataDto.getFields().put("reqData", data.get("data"));
            baseDto.setSuccess(true);
            payFormDataDto.setStatus(true);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getPort() {
        return port;
    }

    @Override
    public String getApplicationContext() {
        return applicationContext;
    }
}
