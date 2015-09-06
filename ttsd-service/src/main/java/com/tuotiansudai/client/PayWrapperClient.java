package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
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
public class PayWrapperClient {

    static Logger logger = Logger.getLogger(PayWrapperClient.class);

    @Value("${paywrapper.host}")
    private String host;

    @Value("${paywrapper.register}")
    private String registerPath;

    @Value("${paywrapper.recharge}")
    private String rechargePath;

    @Value("${paywrapper.bind-card}")
    private String bindBankCardPath;
    @Value("${paywrapper.loan}")
    private String loanPath;

    @Value("${paywrapper.withdraw}")
    private String withdrawPath;

    @Value("${paywrapper.invest}")
    private String investPath;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Autowired
    private OkHttpClient okHttpClient;

    public BaseDto<PayDataDto> register(RegisterAccountDto dto) {
        String requestJson;
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        try {
            requestJson = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
            payDataDto.setStatus(false);
            return baseDto;
        }

        String responseJson = this.post(registerPath, requestJson);
        if (Strings.isNullOrEmpty(responseJson)) {
            payDataDto.setStatus(false);
            return baseDto;
        }
        return this.parsePayResponseJson(responseJson);
    }

    public BaseDto<PayFormDataDto> recharge(RechargeDto dto) {
        String requestJson;
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);
        try {
            requestJson = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
            payFormDataDto.setStatus(false);
            return baseDto;
        }

        String responseJson = this.post(withdrawPath, requestJson);
        if (Strings.isNullOrEmpty(responseJson)) {
            payFormDataDto.setStatus(false);
            return baseDto;
        }
        return this.parsePayFormJson(responseJson);
    }

    public BaseDto<PayFormDataDto> withdraw(WithdrawDto dto) {
        String requestJson;
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);
        try {
            requestJson = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
            payFormDataDto.setStatus(false);
            return baseDto;
        }

        String responseJson = this.post(withdrawPath, requestJson);
        if (Strings.isNullOrEmpty(responseJson)) {
            payFormDataDto.setStatus(false);
            return baseDto;
        }
        return this.parsePayFormJson(responseJson);
    }
    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        String requestJson;
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);
        try {
            requestJson = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
            payFormDataDto.setStatus(false);
            return baseDto;
        }

        String responseJson = this.post(bindBankCardPath, requestJson);
        if (Strings.isNullOrEmpty(responseJson)) {
            payFormDataDto.setStatus(false);
            return baseDto;
        }
        return this.parsePayFormJson(responseJson);
    }

    public BaseDto<PayFormDataDto> invest(InvestDto dto) {
//        String requestJson;
//        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
//        PayFormDataDto payFormDataDto = new PayFormDataDto();
//        baseDto.setData(payFormDataDto);
//        try {
//            requestJson = objectMapper.writeValueAsString(dto);
//        } catch (JsonProcessingException e) {
//            logger.error(e.getLocalizedMessage(), e);
//            payFormDataDto.setStatus(false);
//            return baseDto;
//        }
//
//        String responseJson = this.post(withdrawPath, requestJson);
//        if (Strings.isNullOrEmpty(responseJson)) {
//            payFormDataDto.setStatus(false);
//            return baseDto;
//        }
//        return this.parsePayFormJson(responseJson);
        return  null;
    }

    public BaseDto<PayFormDataDto> loan(LoanDto dto) {
        String requestJson;
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);
        try {
            requestJson = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
            payFormDataDto.setStatus(false);
            return baseDto;
        }

        String responseJson = this.post(loanPath, requestJson);
        if (Strings.isNullOrEmpty(responseJson)) {
            payFormDataDto.setStatus(false);
            return baseDto;
        }
        return this.parsePayFormJson(responseJson);
    }

    private String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json; charset=utf-8")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }

    private String post(String path, String requestJson) {
        String url = host + path;
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

    private BaseDto<PayDataDto> parsePayResponseJson(String json) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        try {
            baseDto  = objectMapper.readValue(json, new TypeReference<BaseDto<PayDataDto>>(){});
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            payDataDto.setStatus(false);
        }
        return baseDto;
    }

    private BaseDto<PayFormDataDto> parsePayFormJson(String json) {
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        try {
            baseDto  = objectMapper.readValue(json, new TypeReference<BaseDto<PayFormDataDto>>(){});
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            baseDto.setSuccess(false);
        }
        return baseDto;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
