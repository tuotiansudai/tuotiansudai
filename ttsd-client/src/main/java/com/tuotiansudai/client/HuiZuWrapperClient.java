package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.HuiZuDataDto;
import com.tuotiansudai.dto.PayDataDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HuiZuWrapperClient extends BaseClient {
    static Logger logger = Logger.getLogger(HuiZuWrapperClient.class);

    @Value("${huizu.host}")
    protected String host;

    @Value("${huizu.port}")
    protected String port;

    private final static String commonCertificationPath = "/common-certification";


    public BaseDto<HuiZuDataDto> commonCertification(Object certificationDto) {
        return syncExecute(certificationDto, commonCertificationPath);
    }

    private BaseDto<HuiZuDataDto> syncExecute(Object requestData, String requestPath) {
        try {
            String responseJson = this.execute(requestPath, requestData != null ? objectMapper.writeValueAsString(requestData) : null, "POST");
            return this.parsePayResponseJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<HuiZuDataDto> baseDto = new BaseDto<>();
        HuiZuDataDto huiZuDataDto = new HuiZuDataDto();
        baseDto.setData(huiZuDataDto);

        return baseDto;
    }

    private BaseDto<HuiZuDataDto> parsePayResponseJson(String json) {
        BaseDto<HuiZuDataDto> baseDto = new BaseDto<>();
        HuiZuDataDto huiZuDataDto = new HuiZuDataDto();
        baseDto.setData(huiZuDataDto);
        if (Strings.isNullOrEmpty(json)) {
            baseDto.setSuccess(false);
            return baseDto;
        }

        try {
            baseDto = objectMapper.readValue(json, new TypeReference<BaseDto<HuiZuDataDto>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            baseDto.setSuccess(false);
        }
        return baseDto;
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
        return "";
    }


}
