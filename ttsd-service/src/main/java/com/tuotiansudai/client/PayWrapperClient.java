package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.RegisterAccountDto;
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

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Autowired
    private OkHttpClient okHttpClient;

    public BaseDto register(RegisterAccountDto dto) {
        String requestJson;
        try {
            requestJson = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
            BaseDto<PayDataDto> baseDto = new BaseDto();
            baseDto.setSuccess(false);
            return baseDto;
        }

        String responseJson = this.post(registerPath, requestJson);
        if (Strings.isNullOrEmpty(responseJson)) {
            BaseDto<PayDataDto> baseDto = new BaseDto();
            PayDataDto payDataDto = new PayDataDto();
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        return this.parsePayResponseJson(responseJson);
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

    private BaseDto parsePayResponseJson(String json) {
        BaseDto<PayDataDto> baseDto = new BaseDto();
        try {
            baseDto  = objectMapper.readValue(json, new TypeReference<BaseDto<PayDataDto>>(){});
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
