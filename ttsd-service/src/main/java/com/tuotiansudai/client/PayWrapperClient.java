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

    private final static String URL_TEMPLATE = "http://{host}:{port}{context}{uri}";

    @Value("${paywrapper.host}")
    private String host;

    @Value("${paywrapper.port}")
    private String port;

    @Value("${paywrapper.context}")
    private String context;

    private String registerPath = "/register";

    private String rechargePath = "/recharge";

    private String bindCardPath = "/bind-card";

    private String loanPath = "/loan";

    private String withdrawPath = "/withdraw";

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

        String responseJson = this.post(rechargePath, requestJson);
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

        String responseJson = this.post(bindCardPath, requestJson);
        if (Strings.isNullOrEmpty(responseJson)) {
            payFormDataDto.setStatus(false);
            return baseDto;
        }
        return this.parsePayFormJson(responseJson);
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

    public BaseDto<MonitorDataDto> monitor() {
        String responseJson = this.get("/monitor");
        if (!Strings.isNullOrEmpty(responseJson)) {
            try {
                return objectMapper.readValue(responseJson, new TypeReference<BaseDto<MonitorDataDto>>(){});
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        BaseDto<MonitorDataDto> resultDto = new BaseDto<>();
        MonitorDataDto dataDto = new MonitorDataDto();
        dataDto.setStatus(false);
        resultDto.setData(dataDto);

        return resultDto;
    }

    private String get(String path) {
        String url = URL_TEMPLATE.replace("{host}", host).replace("{port}", port).replace("{context}", context).replace("{uri}", path);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
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

    public void setPort(String port) {
        this.port = port;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
