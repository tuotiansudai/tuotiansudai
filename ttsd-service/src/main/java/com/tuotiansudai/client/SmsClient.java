package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import com.tuotiansudai.client.dto.ResultDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.IOException;
@Component
public class SmsClient {

    @Value("${sms_wrapper_host}")
    private String host;

    private final String REGISTER_SMS_URI = "/mobile/{mobile}/captcha/{captcha}";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Autowired
    private OkHttpClient httpClient;

    public ResultDto sendSms(String mobile,String code) throws Exception {
        String url = this.host + REGISTER_SMS_URI.replace("{mobile}", mobile).replace("{captcha}",code);

        Request request = new Request.Builder().url(url).get().build();

        Response response = httpClient.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        String jsonData = response.body().string();

        ResultDto resultDto = this.jsonConvertToObject(jsonData);

        return resultDto ;
    }

    public ResultDto jsonConvertToObject(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        ResultDto resultDto = null;
        try {

            resultDto = mapper.readValue(jsonString, ResultDto.class);

        } catch (JsonGenerationException e) {

            e.printStackTrace();

        } catch (JsonMappingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return resultDto;

    }

}
