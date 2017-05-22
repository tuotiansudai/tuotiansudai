package com.tuotiansudai.paywrapper.ghb.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.ghb.message.request.RequestBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.request.RequestMessageContent;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.response.ResponseMessageContent;
import com.tuotiansudai.paywrapper.ghb.service.GHBMessageRecordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class GHBClient {

    private final static Logger logger = Logger.getLogger(GHBClient.class);

    private final GHBMessageRecordService ghbMessageRecordService;

    private final OkHttpClient client = new OkHttpClient();

    @Autowired
    public GHBClient(GHBMessageRecordService ghbMessageRecordService) {
        this.ghbMessageRecordService = ghbMessageRecordService;
        client.setReadTimeout(5, TimeUnit.SECONDS);
        client.setWriteTimeout(5, TimeUnit.SECONDS);
        client.setConnectTimeout(5, TimeUnit.SECONDS);
    }

    public <T extends RequestBaseOGW> BaseDto<PayFormDataDto> generateFormData(RequestMessageContent<T> message) throws PayException {
        try {
            this.ghbMessageRecordService.saveRequestMessage(message);
        } catch (IllegalAccessException e) {
            logger.error(e.getLocalizedMessage(), e);
            return new BaseDto<>(new PayFormDataDto());
        }
        PayFormDataDto payFormDataDto = new PayFormDataDto("url",
                Maps.newHashMap(ImmutableMap.<String, String>builder()
                        .put("transCode", message.getBody().getTranscode())
                        .put("RequestData", message.getFullMessage())
                        .build()));

        return new BaseDto<>(payFormDataDto);
    }

    public <U extends RequestBaseOGW, V extends ResponseBaseOGW> ResponseMessageContent<V> invoke(RequestMessageContent<U> message) {
        try {
            ghbMessageRecordService.saveRequestMessage(message);
        } catch (IllegalAccessException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), message.getFullMessage());
        Request request = new Request.Builder()
                .url("url")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String body = response.body().string();
                ResponseMessageContent<V> messageResponse = new ResponseMessageContent<>(body, message.getBody().getXmlpara().getResponseClass());
                ghbMessageRecordService.saveResponseMessage(messageResponse);
                return messageResponse;
            }
        } catch (IOException | IllegalAccessException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }
}
