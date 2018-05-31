package com.tuotiansudai.client;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.*;
import com.tuotiansudai.enums.BankCallbackType;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.fudian.dto.*;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankLoanCreateMessage;
import com.tuotiansudai.fudian.message.BankReturnCallbackMessage;
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

    public BankReturnCallbackMessage checkBankReturnUrl(String path, String bankReturnParams) {
        RequestBody requestBody = new FormEncodingBuilder().add("reqData", bankReturnParams).build();
        Request request = new Request.Builder()
//                .url(this.baseUrl + path)
                .url("http://192.168.80.88:30001" + path)
                .post(requestBody)
                .build();

        try {
            Response response = this.okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                try {
                    return new GsonBuilder().create().fromJson(response.body().string(), BankReturnCallbackMessage.class);
                } catch (JsonParseException e) {
                    logger.error(MessageFormat.format("parse return return callback error, url: {0}, data: {1}, response: {2}", path, response.body().string()), e);
                }
            } else {
                logger.error(MessageFormat.format("bank return callback is invalid, url: {0}, data: {1}", path, bankReturnParams));
            }
        } catch (IOException e) {
            logger.error(MessageFormat.format("bank return callback error, url: {0}, data: {1}", path, bankReturnParams), e);
        }

        return null;
    }

    public Boolean isCallbackSuccess(BankCallbackType bankCallbackType, String orderNo) {
        try {
            Request request = new Request.Builder()
                    .url("http://192.168.80.88:30001" + MessageFormat.format("/callback/{0}/order-no/{1}/is-success", bankCallbackType.name().toLowerCase(), orderNo))
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

    public BankAsyncMessage register(Source source, String loginName, String mobile, String realName, String identityCode) {
        return asyncExecute(MessageFormat.format("/user/register/source/{0}", source.name().toLowerCase()),
                new BankRegisterDto(loginName, mobile, realName, identityCode));
    }

    public BankAsyncMessage bindBankCard(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo) {
        return asyncExecute(MessageFormat.format("/user/card-bind/source/{0}", source.name().toLowerCase()),
                new BankBaseDto(loginName, mobile, bankUserName, bankAccountNo));
    }

    public BankAsyncMessage unbindBankCard(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo) {
        return asyncExecute(MessageFormat.format("/user/cancel-card-bind/source/{0}", source.name().toLowerCase()),
                new BankBaseDto(loginName, mobile, bankUserName, bankAccountNo));
    }

    public BankAsyncMessage recharge(long rechargeId, Source source, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, String payType){
        return asyncExecute(MessageFormat.format("/recharge/source/{0}", source.name().toLowerCase()),
                new BankRechargeDto(loginName, mobile, bankUserName, bankAccountNo, rechargeId, amount, RechargePayType.valueOf(payType)));
    }

    public BankAsyncMessage withdraw(long withdrawId, Source source, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, long fee, String openId) {
        return asyncExecute(MessageFormat.format("/withdraw/source/{0}", source.name().toLowerCase()),
                new BankWithdrawDto(withdrawId, loginName, mobile, bankUserName, bankAccountNo, amount, fee, openId));
    }

    public BankAsyncMessage invest(long investId, Source source, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, String loanTxNo, long loanId, String loanName) {
        return asyncExecute(MessageFormat.format("/loan-invest/source/{0}", source.name().toLowerCase()),
                new BankInvestDto(loginName, mobile, bankUserName, bankAccountNo, investId, amount, loanTxNo, loanId, loanName));
    }

    public BankReturnCallbackMessage fastInvest(long investId, Source source, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, String loanTxNo, long loanId, String loanName) {
        String json = syncExecute(MessageFormat.format("/loan-fast-invest/source/{0}", source.name().toLowerCase()),
                new BankInvestDto(loginName, mobile, bankUserName, bankAccountNo, investId, amount, loanTxNo, loanId, loanName));

        if (Strings.isNullOrEmpty(json)) {
            return new BankReturnCallbackMessage();
        }

        try {
            return new GsonBuilder().create().fromJson(json, BankReturnCallbackMessage.class);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Loan Fast Invest] parse response error, loanId: {0}", String.valueOf(loanId)), e);
        }

        return new BankReturnCallbackMessage();
    }

    public BankLoanCreateMessage createLoan(String bankUserName, String bankAccountNo, long loanId, long loanAmount) {
        BankLoanCreateDto bankLoanCreateDto = new BankLoanCreateDto(bankUserName, bankAccountNo, String.valueOf(loanId), loanAmount);

        String json = syncExecute("/loan-create", bankLoanCreateDto);

        if (Strings.isNullOrEmpty(json)) {
            return new BankLoanCreateMessage(false, null);
        }

        try {
            return new GsonBuilder().create().fromJson(json, BankLoanCreateMessage.class);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Loan Create] parse response error, loanId: {0}", String.valueOf(loanId)), e);
        }

        return new BankLoanCreateMessage(false, null);
    }

    private BankAsyncMessage asyncExecute(String path, Object requestData) {
        String content = new GsonBuilder().create().toJson(requestData);
//        String url = this.baseUrl + path;
        String url = "http://localhost:30001" + path;

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), content);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = this.okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                try {
                    return new GsonBuilder().create().fromJson(response.body().string(), BankAsyncMessage.class);
                } catch (JsonParseException e) {
                    logger.error(MessageFormat.format("parse pay response error, url: {0}, data: {1}, response: {2}", url, content, response.body().string()), e);
                }
            }
            logger.error(MessageFormat.format("call pay wrapper status: {0}, url: {1}, data: {2}", response.code(), url, content));
        } catch (IOException e) {
            logger.error(MessageFormat.format("call pay wrapper error, url: {0}, data: {1}", url, content), e);
        }

        return new BankAsyncMessage();
    }

    private String syncExecute(String path, Object requestData) {
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
                return response.body().string();
            }
            logger.error(MessageFormat.format("call pay wrapper status: {0}, url: {1}, request: {2}", response.code(), url, content));
        } catch (IOException e) {
            logger.error(MessageFormat.format("call pay wrapper error, url: {0}, request: {1}", url, content), e);
        }

        return null;
    }


}
