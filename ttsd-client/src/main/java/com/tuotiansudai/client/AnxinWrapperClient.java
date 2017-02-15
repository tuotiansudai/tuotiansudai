package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.AnxinDataDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Component
public class AnxinWrapperClient extends BaseClient {

    static Logger logger = Logger.getLogger(AnxinWrapperClient.class);

    @Value("${anxin.host}")
    protected String host;

    @Value("${anxin.port}")
    protected String port;

    @Value("${anxin.application.context}")
    protected String applicationContext;

    private final static String createLoanContract = "/anxin-sign/{0}/create-loan-contract";

    private final static String createTransferContract = "/anxin-sign/{0}/create-transfer-contract";

    public BaseDto<BaseDataDto> createLoanContract(long loanId) {
        return syncExecute(null, MessageFormat.format(createLoanContract, String.valueOf(loanId)), "POST");
    }

    public BaseDto<BaseDataDto> createTransferContract(long transferId) {
        return syncExecute(null, MessageFormat.format(createTransferContract, String.valueOf(transferId)), "POST");
    }

    public Map<String, String> getLoanStatus(long loanId) {
        String json = this.execute(MessageFormat.format("/real-time/loan/{0}", String.valueOf(loanId)), null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    public Map<String, String> getTransferStatus(String orderId, Date merDate, String businessType) {
        String json = this.execute(MessageFormat.format("/real-time/transfer/order-id/{0}/mer-date/{1}/business-type/{2}", orderId, new DateTime(merDate).toString("yyyyMMdd"), businessType), null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    private BaseDto<BaseDataDto> parsePayResponseJson(String json) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);
        if (Strings.isNullOrEmpty(json)) {
            baseDto.setSuccess(false);
            return baseDto;
        }

        try {
            baseDto = objectMapper.readValue(json, new TypeReference<BaseDto<BaseDataDto>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            baseDto.setSuccess(false);
        }
        return baseDto;
    }

    private BaseDto<BaseDataDto> syncExecute(Object requestData, String requestPath, String method) {
        try {
            String responseJson = this.execute(requestPath, requestData != null ? objectMapper.writeValueAsString(requestData) : null, method);
            return this.parsePayResponseJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);

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

    public String getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(String applicationContext) {
        this.applicationContext = applicationContext;
    }

}
