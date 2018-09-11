package com.tuotiansudai.fudian.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.ump.asyn.callback.BaseCallbackRequestModel;
import com.tuotiansudai.fudian.ump.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.fudian.ump.sync.response.BaseSyncResponseModel;
import com.tuotiansudai.fudian.umpwrapper.PayGateWrapper;
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

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static OkHttpClient CLIENT;

    private final PayGateWrapper payGateWrapper;

    @Autowired
    public UmpUtils(PayGateWrapper payGateWrapper) {
        CLIENT = new OkHttpClient
                .Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.payGateWrapper = payGateWrapper;
    }

    public <T extends BaseSyncRequestModel> void sign(T model) {
        try {
            ReqData reqData = payGateWrapper.makeReqDataByPost(model.generatePayRequestData());
            model.setSign(reqData.getSign());
            model.setRequestUrl(reqData.getUrl());
            model.setField(reqData.getField());
        } catch (ReqDataException e) {
            logger.error("[UMP] generate form data fail:{}", e);
        }
    }

    public <T extends BaseCallbackRequestModel> T parseCallbackRequest(Map<String, String> paramsMap,
                                                                       String originalQueryString,
                                                                       Class<T> modelClass) {
        try {
            T model = parseParamsToModel(paramsMap, modelClass);
            model.setRequestData(originalQueryString);
            model.setResponseTime(new Date());
            model.setResponseData(payGateWrapper.merNotifyResData(model.generatePayResponseData()));
            return model;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Parse callback request failed: {0}", originalQueryString));
        }
        return null;
    }

    private <T extends BaseCallbackRequestModel> T parseParamsToModel(Map<String, String> paramsMap, Class<T> model) throws VerifyException, IOException {
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

    public boolean validateCallBack(Map<String, String> paramsMap) throws VerifyException {
        Map<String, String> platNotifyData = payGateWrapper.getPlatNotifyData(paramsMap);
        return "0000".equals(platNotifyData.get("ret_code"));
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

            return body == null ? null : body.string();


        } catch (IOException e) {
            logger.error(MessageFormat.format("[UMP Client] call ump exception, data: {0}", field), e);
        }

        return null;
    }

    public <T extends BaseSyncResponseModel> void generateResponse(Long requestId, String responseBody, T model) {
        try {
            Map<String, String> resData = payGateWrapper.getResData(responseBody);
            model.setRequestId(requestId);
            model.initializeModel(resData);
        } catch (RetDataException e) {
            logger.error(MessageFormat.format("[UMP response] generate exception, data: {0}", responseBody), e);
        }
    }
}
