package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.tuotiansudai.dto.AnxinDataDto;
import com.tuotiansudai.dto.BaseDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class AnxinWrapperClient extends BaseClient {

    static Logger logger = Logger.getLogger(AnxinWrapperClient.class);

    @Value("${anxin.host}")
    protected String host;

    @Value("${anxin.port}")
    protected String port;

    private final static String createLoanContract = "/anxin-sign/create-loan-contract";

    private final static String createTransferContract = "/anxin-sign/create-transfer-contract";

    private final static String queryContract = "/anxin-sign/query-contract";

    private final static String createAccount = "/anxin-sign/create-account";

    private final static String sendCaptcha = "/anxin-sign/send-captcha";

    private final static String verifyCaptcha = "/anxin-sign/verify-captcha";

    private final static String switchSkipAuth = "/anxin-sign/switch-skip-auth";

    private final static String printContract = "/anxin-sign/print-contract";

    private final static String printAnxinContract = "/anxin-sign/print-anxin-contract";

    private final static String batchAnxinContracts = "/anxin-sign/batch-anxin-contracts";

    private final static String isAuthenticationRequired= "/anxin-sign/is-authentication-required";

    public AnxinWrapperClient() {
        this.okHttpClient.setConnectTimeout(300, TimeUnit.SECONDS);
        this.okHttpClient.setReadTimeout(300, TimeUnit.SECONDS);
        this.okHttpClient.setWriteTimeout(300, TimeUnit.SECONDS);
    }

    public BaseDto<AnxinDataDto> createLoanContract(long loanId) {
        return syncExecute(String.valueOf(loanId), createLoanContract, "POST");
    }

    public BaseDto<AnxinDataDto> createTransferContract(long transferId) {
        return syncExecute(String.valueOf(transferId), createTransferContract, "POST");
    }

    public BaseDto<AnxinDataDto> queryContract(Object anxinQueryContractDto) {
        return syncExecute(anxinQueryContractDto, queryContract, "POST");
    }

    public BaseDto<AnxinDataDto> createAccount(String loginName) {
        return syncExecute(loginName, createAccount, "POST");
    }

    public BaseDto<AnxinDataDto> sendCaptcha(Object anxinSendCaptchaDto) {
        return syncExecute(anxinSendCaptchaDto, sendCaptcha, "POST");
    }

    public BaseDto<AnxinDataDto> verifyCaptcha(Object anxinVerifyCaptchaDto) {
        return syncExecute(anxinVerifyCaptchaDto, verifyCaptcha, "POST");
    }

    public BaseDto<AnxinDataDto> switchSkipAuth(Object anxinSwitchSkipAuthDto) {
        return syncExecute(anxinSwitchSkipAuthDto, switchSkipAuth, "POST");
    }

    public BaseDto<AnxinDataDto> isAuthenticationRequired(String loginName){
        return syncExecute(loginName, isAuthenticationRequired, "POST");
    }

    public byte[] printContract(Object requestData) {
        try {
            return this.downPdf(printContract, requestData != null ? objectMapper.writeValueAsString(requestData) : null);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public byte[] printAnxinContract(Object requestData) {
        return this.downPdf(printAnxinContract, requestData != null ? requestData.toString() : null);
    }

    public byte[] batchAnxinContracts(Object requestData) {
        return this.downPdf(batchAnxinContracts, requestData != null ? requestData.toString() : null);
    }

    private BaseDto<AnxinDataDto> parsePayResponseJson(String json) {
        BaseDto<AnxinDataDto> baseDto = new BaseDto<>();
        AnxinDataDto anxinDataDto = new AnxinDataDto();
        baseDto.setData(anxinDataDto);
        if (Strings.isNullOrEmpty(json)) {
            baseDto.setSuccess(false);
            return baseDto;
        }

        try {
            baseDto = objectMapper.readValue(json, new TypeReference<BaseDto<AnxinDataDto>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            baseDto.setSuccess(false);
        }
        return baseDto;
    }

    private BaseDto<AnxinDataDto> syncExecute(Object requestData, String requestPath, String method) {
        try {
            String responseJson = this.execute(requestPath, requestData != null ? (requestData instanceof String ? requestData.toString() : objectMapper.writeValueAsString(requestData)) : null, method);
            return this.parsePayResponseJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<AnxinDataDto> baseDto = new BaseDto<>();
        AnxinDataDto anxinDataDto = new AnxinDataDto();
        baseDto.setData(anxinDataDto);

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
