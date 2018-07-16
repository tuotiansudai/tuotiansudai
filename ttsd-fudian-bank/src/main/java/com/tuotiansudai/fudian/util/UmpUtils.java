package com.tuotiansudai.fudian.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.ump.asyn.callback.BaseCallbackRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.fudian.ump.sync.response.BaseSyncResponseModel;
import com.tuotiansudai.fudian.client.PayGateWrapper;
import com.umpay.api.common.ReqData;
import com.umpay.api.exception.ReqDataException;
import com.umpay.api.exception.RetDataException;
import com.umpay.api.exception.VerifyException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class UmpUtils {

    private static Logger logger = LoggerFactory.getLogger(BankClient.class);

    private final PayGateWrapper payGateWrapper;

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static OkHttpClient CLIENT;

    @Autowired
    public UmpUtils(PayGateWrapper payGateWrapper){
        this.payGateWrapper = payGateWrapper;
        CLIENT = new OkHttpClient
                .Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public <T extends BaseSyncRequestModel> void sign(T model){
        try {
            ReqData reqData = payGateWrapper.makeReqDataByPost(model);
            model.setSign(reqData.getSign());
            model.setRequestUrl(reqData.getUrl());
            model.setField(reqData.getField());
        } catch (ReqDataException e) {
            logger.error("[UMP] generate form data fail:{}", e);
        }
    }

    @SuppressWarnings(value = "unchecked")
    public <T extends BaseCallbackRequestModel> T parseCallbackRequest(Map<String, String> paramsMap,
                                                                       String originalQueryString,
                                                                       T model) {
        try {
            model = (T) parseParamsToModel(paramsMap, model.getClass());
            model.setRequestData(originalQueryString);
            model.setResponseTime(new Date());
            model.setResponseData(payGateWrapper.merNotifyResData(model.generatePayResponseData()));
            return model;
        } catch (VerifyException | IOException e) {
            logger.error(MessageFormat.format("Parse callback request failed: {0}", originalQueryString));
        }
        return null;
    }

    private BaseCallbackRequestModel parseParamsToModel(Map<String, String> paramsMap, Class<? extends BaseCallbackRequestModel> model) throws VerifyException, IOException {
        Map<String, String> platNotifyData = payGateWrapper.getPlatNotifyData(paramsMap);
        Map<String, String> newPlatNotifyData = Maps.newHashMap();
        for (String key : platNotifyData.keySet()) {
            StringBuilder newKeyBuilder = new StringBuilder();
            String[] splits = key.split("_");
            for (String split : splits) {
                newKeyBuilder.append(StringUtils.capitalize(split));
            }
            String newKey = StringUtils.uncapitalize(newKeyBuilder.toString());
            newPlatNotifyData.put(newKey, platNotifyData.get(key));
        }

        String json = objectMapper.writeValueAsString(newPlatNotifyData);
        return objectMapper.readValue(json, model);
    }

    public String send(String requestUrl, Map<String, String> field) {
        FormBody.Builder formBody = new FormBody.Builder();
        for (String key : field.keySet()) {
            formBody.add(key, field.get(key));
        }

        Request request = new Request.Builder().url(requestUrl).post(formBody.build()).build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("[UMP Client] call ump is not 200, request data: {}, status: {}", field, response.code());
                return null;
            }

            ResponseBody body = response.body();

            return body == null ? null : body.toString();


        } catch (IOException e) {
            logger.error(MessageFormat.format("[UMP Client] call ump exception, data: {0}",  field), e);
        }

        return null;
    }

    public <T extends BaseSyncResponseModel> void generateResponse(long requestId, String responseBody, T model) {
        try {
            Map<String, String> resData = payGateWrapper.getResData(responseBody);
            model.setRequestId(requestId);
            model.initializeModel(resData);
        } catch (RetDataException e) {
            logger.error(MessageFormat.format("[UMP response] generate exception, data: {0}",  responseBody), e);
        }
    }
}
