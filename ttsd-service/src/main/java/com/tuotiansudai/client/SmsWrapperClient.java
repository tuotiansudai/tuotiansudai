package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.tuotiansudai.client.dto.ResultDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class SmsWrapperClient {

    static Logger logger = Logger.getLogger(SmsWrapperClient.class);

    @Value("${smswrapper.host}")
    private String host;

    private final String REGISTER_SMS_URI = "/sms/mobile/{mobile}/captcha/{captcha}";

    @Autowired
    private OkHttpClient okHttpClient;

    public ResultDto sendSms(String mobile, String code) {
        String url = this.host + REGISTER_SMS_URI.replace("{mobile}", mobile).replace("{captcha}", code);

        Request request = new Request.Builder().url(url).get().build();

        ResultDto resultDto = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            String jsonData = response.body().string();
            resultDto = this.jsonConvertToObject(jsonData);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return resultDto;
    }

    public ResultDto jsonConvertToObject(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        ResultDto resultDto = null;
        try {
            resultDto = mapper.readValue(jsonString, ResultDto.class);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return resultDto;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
