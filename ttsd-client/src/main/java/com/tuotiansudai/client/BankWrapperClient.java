package com.tuotiansudai.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.BankCallbackType;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.fudian.dto.BankAsyncData;
import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.BankWithdrawDto;
import com.tuotiansudai.repository.model.Source;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class BankWrapperClient {

    private static Logger logger = Logger.getLogger(BankWrapperClient.class);

    private final OkHttpClient okHttpClient;

    private final String baseUrl;

    public BankWrapperClient() {
        ETCDConfigReader reader = ETCDConfigReader.getReader();
        String host = reader.getValue("pay.host");
        String port = reader.getValue("pay.port");
        String applicationContext = reader.getValue("pay.application.context");

        this.baseUrl = MessageFormat.format("http://{0}:{1}{2}", host, port, applicationContext);
        this.okHttpClient = new OkHttpClient();
        this.okHttpClient.setConnectTimeout(180, TimeUnit.SECONDS);
        this.okHttpClient.setReadTimeout(180, TimeUnit.SECONDS);
        this.okHttpClient.setWriteTimeout(180, TimeUnit.SECONDS);
    }

    public BaseDto<PayDataDto> checkBankReturnUrl(String path, String bankReturnParams) {
        RequestBody requestBody = new FormEncodingBuilder().add("reqData", bankReturnParams).build();
        Request request = new Request.Builder()
                .url(this.baseUrl + path)
                .post(requestBody)
                .build();

        try {
            Response response = this.okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                try {
                    return new GsonBuilder().create().fromJson(response.body().string(), new TypeToken<BaseDto<PayDataDto>>() {
                    }.getType());
                } catch (JsonParseException e) {
                    logger.error(MessageFormat.format("parse return return callback error, url: {}, data: {}, response: {}", path, response.body().string()), e);
                }
            } else {
                logger.error(MessageFormat.format("bank return callback is invalid, url: {}, data: {}", path, bankReturnParams));
            }
        } catch (IOException e) {
            logger.error(MessageFormat.format("bank return callback error, url: {}, data: {}", path, bankReturnParams), e);
        }

        return null;
    }

    public Boolean isCallbackSuccess(BankCallbackType bankCallbackType, String orderNo) {
        try {
            Request request = new Request.Builder()
                    .url(this.baseUrl + MessageFormat.format("/callback/{0}/order-no/{1}/is-success", bankCallbackType.name().toLowerCase(), orderNo))
                    .get()
                    .build();

            Response response = this.okHttpClient.newCall(request).execute();

            if (response.code() == HttpStatus.NO_CONTENT.value()) {
                return null;
            }

            return response.code() == HttpStatus.OK.value();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    public BankAsyncData register(Source source, String loginName, String mobile, String realName, String identityCode) {
        return asyncExecute(MessageFormat.format("/user/register/source/{}", source.name().toLowerCase()),
                Maps.newHashMap(ImmutableMap.<String, String>builder()
                        .put("loginName", loginName)
                        .put("mobile", mobile)
                        .put("realName", realName)
                        .put("" +
                                "", identityCode)
                        .build()));
    }

    public BankAsyncData bindBankCard(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo) {
        return asyncExecute(MessageFormat.format("/user/card-bind/source/{0}", source.name().toLowerCase()),
                new BankBaseDto(loginName, mobile, bankUserName, bankAccountNo));
    }

    public BankAsyncData unbindBankCard(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo) {
        return asyncExecute(MessageFormat.format("/user/cancel-card-bind/source/{0}", source.name().toLowerCase()),
                new BankBaseDto(loginName, mobile, bankUserName, bankAccountNo));
    }

    public BankAsyncData withdraw(long withdrawId, Source source, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, long fee, String openId) {
        BankWithdrawDto dto = new BankWithdrawDto(withdrawId, loginName, mobile, bankUserName, bankAccountNo, amount, fee, openId);
        return asyncExecute(MessageFormat.format("/withdraw/source/{0}", source.name().toLowerCase()), dto);
    }

    private BankAsyncData asyncExecute(String path, Object requestData) {
        String content = new GsonBuilder().create().toJson(requestData);
        String url = this.baseUrl + path;

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = this.okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                try {
                    return new GsonBuilder().create().fromJson(response.body().string(), BankAsyncData.class);
                } catch (JsonParseException e) {
                    logger.error(MessageFormat.format("parse pay response error, url: {}, data: {}, response: {}", url, content, response.body().string()), e);
                }
            }
            logger.error(MessageFormat.format("call pay wrapper status: {}, url: {}, data: {}", response.code(), url, content));
        } catch (IOException e) {
            logger.error(MessageFormat.format("call pay wrapper error, url: {}, data: {}", url, content), e);
        }

        return new BankAsyncData();
    }

}
