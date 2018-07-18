package com.tuotiansudai.client;

import com.google.common.base.Strings;
import com.google.gson.Gson;
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
import com.tuotiansudai.fudian.message.*;
import com.tuotiansudai.fudian.umpdto.UmpRechargeDto;
import com.tuotiansudai.repository.model.Source;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BankWrapperClient {

    public static final Gson gson = new GsonBuilder().create();

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
                .url(this.baseUrl + path)
                .post(requestBody)
                .build();

        try {
            Response response = this.okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                try {
                    return gson.fromJson(response.body().string(), BankReturnCallbackMessage.class);
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

    public BankAsyncMessage registerInvestor(Source source, String loginName, String mobile, String token, String realName, String identityCode) {
        return asyncExecute(MessageFormat.format("/user/register/source/{0}/role/investor", source.name().toLowerCase()),
                new BankRegisterDto(loginName, mobile, token, realName, identityCode));
    }

    public BankAsyncMessage registerLoaner(Source source, String loginName, String mobile, String token, String realName, String identityCode) {
        return asyncExecute(MessageFormat.format("/user/register/source/{0}/role/loaner", source.name().toLowerCase()),
                new BankRegisterDto(loginName, mobile, token, realName, identityCode));
    }

    public BankAsyncMessage authorization(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo) {
        return asyncExecute(MessageFormat.format("/user/authorization/source/{0}", source.name().toLowerCase()),
                new BankBaseDto(loginName, mobile, bankUserName, bankAccountNo));
    }

    public BankAsyncMessage resetPassword(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo) {
        return asyncExecute(MessageFormat.format("/user/password-reset/source/{0}", source.name().toLowerCase()),
                new BankBaseDto(loginName, mobile, bankUserName, bankAccountNo));
    }

    public BankAsyncMessage investorBindBankCard(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo) {
        return asyncExecute(MessageFormat.format("/user/card-bind/source/{0}/role/investor", source.name().toLowerCase()),
                new BankBaseDto(loginName, mobile, bankUserName, bankAccountNo));
    }

    public BankAsyncMessage loanerBindBankCard(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo) {
        return asyncExecute(MessageFormat.format("/user/card-bind/source/{0}/role/loaner", source.name().toLowerCase()),
                new BankBaseDto(loginName, mobile, bankUserName, bankAccountNo));
    }

    public BankAsyncMessage investorUnbindBankCard(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo) {
        return asyncExecute(MessageFormat.format("/user/cancel-card-bind/source/{0}/role/investor", source.name().toLowerCase()),
                new BankBaseDto(loginName, mobile, bankUserName, bankAccountNo));
    }

    public BankAsyncMessage loanerUnbindBankCard(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo) {
        return asyncExecute(MessageFormat.format("/user/cancel-card-bind/source/{0}/role/loaner", source.name().toLowerCase()),
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

    public BankAsyncMessage loanCreditInvest(long transferApplicationId, long investId, Source source, String loginName, String mobile, String bankUserName, String bankAccountNo, long investAmount, long transferAmount, long transferFee, String investOrderNo, String investOrderDate, String loanTxNo) {
        return asyncExecute(MessageFormat.format("/loan-credit-invest/source/{0}", source.name().toLowerCase()),
                new BankLoanCreditInvestDto(loginName, mobile, bankUserName, bankAccountNo, transferApplicationId, investId, investAmount, transferAmount, transferFee, investOrderDate, investOrderNo, loanTxNo));
    }

    public BankReturnCallbackMessage fastInvest(long investId, Source source, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, String loanTxNo, long loanId, String loanName) {
        String json = syncExecute(MessageFormat.format("/loan-fast-invest/source/{0}", source.name().toLowerCase()),
                new BankInvestDto(loginName, mobile, bankUserName, bankAccountNo, investId, amount, loanTxNo, loanId, loanName));

        if (Strings.isNullOrEmpty(json)) {
            return new BankReturnCallbackMessage();
        }

        try {
            return gson.fromJson(json, BankReturnCallbackMessage.class);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Loan Fast Invest] parse response error, loanId: {0}", String.valueOf(loanId)), e);
        }

        return new BankReturnCallbackMessage();
    }

    public BankLoanCreateMessage createLoan(String bankUserName, String bankAccountNo, String loanName, long loanAmount, String endTime) {
        BankLoanCreateDto bankLoanCreateDto = new BankLoanCreateDto(bankUserName, bankAccountNo, loanName, loanAmount, endTime);

        String json = syncExecute("/loan-create", bankLoanCreateDto);

        if (Strings.isNullOrEmpty(json)) {
            return new BankLoanCreateMessage(false, null);
        }

        try {
            return gson.fromJson(json, BankLoanCreateMessage.class);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Loan Create] parse response error, loanId: {0}", loanName), e);
        }

        return new BankLoanCreateMessage(false, null);
    }

    public BankLoanCancelMessage cancelLoan(long loanId, String loanOrderNo, String loanOrderDate, String loanTxNo) {
        BankLoanCancelDto bankLoanCancelDto = new BankLoanCancelDto(loanId, loanTxNo, loanOrderNo, loanOrderDate);

        String json = syncExecute("/loan-cancel", bankLoanCancelDto);

        if (Strings.isNullOrEmpty(json)) {
            return new BankLoanCancelMessage(false, null);
        }

        try {
            return gson.fromJson(json, BankLoanCancelMessage.class);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Loan Create] parse response error, loanId: {0}", String.valueOf(loanId)), e);
        }

        return new BankLoanCancelMessage(false, null);
    }

    public BankLoanFullMessage loanFull(String loginName, String mobile, String bankUserName, String bankAccountNo, long loanId, String loanTxNo, String loanOrderNo, String loanOrderDate, String expectRepayTime, String checkerLoginName, long time) {
        BankLoanFullDto bankLoanFullDto = new BankLoanFullDto(loginName, mobile, bankUserName, bankAccountNo, loanId, loanTxNo, loanOrderNo, loanOrderDate, expectRepayTime, checkerLoginName, time);

        String json = syncExecute("/loan-full", bankLoanFullDto);

        if (Strings.isNullOrEmpty(json)) {
            return new BankLoanFullMessage(false, "请求失败");
        }

        try {
            return gson.fromJson(json, BankLoanFullMessage.class);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Loan Create] parse response error, response: {0}", json), e);
        }

        return new BankLoanFullMessage(false, null);
    }

    public BankAsyncMessage loanRepay(BankLoanRepayDto bankLoanRepayDto) {
        return asyncExecute("/loan-repay/source/web", bankLoanRepayDto);
    }

    public BankMerchantTransferMessage merchantTransfer(String loginName, String mobile, String bankUserName, String bankAccountNo, long amount) {
        BankMerchantTransferDto bankMerchantTransferDto = new BankMerchantTransferDto(loginName, mobile, bankUserName, bankAccountNo, amount);

        String json = syncExecute("/merchant-transfer", bankMerchantTransferDto);

        if (Strings.isNullOrEmpty(json)) {
            return new BankMerchantTransferMessage(false, "请求失败");
        }

        try {
            return gson.fromJson(json, BankMerchantTransferMessage.class);
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Merchant Transfer] parse response error, response: {0}", json), e);
        }

        return new BankMerchantTransferMessage(false, null);
    }

    public BankQueryUserMessage queryUser(String bankUserName, String bankAccountNo) {
        try {
            Request request = new Request.Builder()
                    .url(this.baseUrl + MessageFormat.format("/query/user/user-name/{0}/account-no/{1}", bankUserName, bankAccountNo))
                    .get()
                    .build();

            Response response = this.okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return gson.fromJson(response.body().string(), BankQueryUserMessage.class);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return new BankQueryUserMessage(false, "查询异常");
    }

    public BankQueryLoanMessage queryLoan(String loanTxNo, String loanAccNo) {
        try {
            Request request = new Request.Builder()
                    .url(this.baseUrl + MessageFormat.format("/query/loan/loan-tx-no/{0}/loan-acc-no/{1}", loanTxNo, loanAccNo))
                    .get()
                    .build();

            Response response = this.okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return gson.fromJson(response.body().string(), BankQueryLoanMessage.class);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return new BankQueryLoanMessage(false, "查询异常");
    }

    public BankQueryTradeMessage queryTrade(String bankOrderNo, String bankOrderDate, QueryTradeType queryTradeType) {
        try {
            Request request = new Request.Builder()
                    .url(this.baseUrl + MessageFormat.format("/query/trade/order-no/{0}/order-date/{1}/query-type/{2}", bankOrderNo, bankOrderDate, queryTradeType))
                    .get()
                    .build();
            Response response = this.okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                return gson.fromJson(response.body().string(), BankQueryTradeMessage.class);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return new BankQueryTradeMessage(false, "查询异常");
    }

    public BankQueryLogAccountMessage queryAccountBill(String bankUserName, String bankAccountNo, Date queryOrderDateStart, Date queryOrderDateEnd) {
        try {
            Request request = new Request.Builder()
                    .url(this.baseUrl + MessageFormat.format("/query/log-account/user-name/{0}/account-no/{1}/query-order-date-start/{2}/query-order-date-end/{3}",
                            bankUserName, bankAccountNo, new DateTime(queryOrderDateStart).toString("yyyyMMdd"), new DateTime(queryOrderDateEnd).toString("yyyyMMdd")))
                    .get()
                    .build();
            Response response = this.okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                return gson.fromJson(response.body().string(), BankQueryLogAccountMessage.class);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return new BankQueryLogAccountMessage(false, "查询异常");
    }

    public BankQueryLogLoanAccountMessage queryLoanBill(String loanTxNo, String loanAccNo) {
        try {
            Request request = new Request.Builder()
                    .url(this.baseUrl + MessageFormat.format("/query/log-loan-account/loan-tx-no/{0}/loan-acc-no/{1}", loanTxNo, loanAccNo))
                    .get()
                    .build();
            Response response = this.okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                return gson.fromJson(response.body().string(), BankQueryLogLoanAccountMessage.class);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return new BankQueryLogLoanAccountMessage(false, "查询异常");
    }

    private BankAsyncMessage asyncExecute(String path, Object requestData) {
        String content = gson.toJson(requestData);
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
                    return gson.fromJson(response.body().string(), BankAsyncMessage.class);
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
        String content = gson.toJson(requestData);
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

    private UmpAsyncMessage umpAsyncExecute(String path, Object requestData) {
        String content = gson.toJson(requestData);
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
                    return gson.fromJson(response.body().string(), UmpAsyncMessage.class);
                } catch (JsonParseException e) {
                    logger.error(MessageFormat.format("ump parse pay response error, url: {0}, data: {1}, response: {2}", url, content, response.body().string()), e);
                }
            }
            logger.error(MessageFormat.format("ump call pay wrapper status: {0}, url: {1}, data: {2}", response.code(), url, content));
        } catch (IOException e) {
            logger.error(MessageFormat.format("ump call pay wrapper error, url: {0}, data: {1}", url, content), e);
        }

        return new UmpAsyncMessage();
    }

    public UmpAsyncMessage umpRecharge(String loginName, String payUserId, long rechargeId, long amount, boolean isFastPay, String bankCode) {
        return umpAsyncExecute("/ump/recharge", new UmpRechargeDto(loginName, payUserId, rechargeId, amount, isFastPay, bankCode));
    }

    public UmpAsyncMessage umpWithdraw() {
        return umpAsyncExecute("/ump/withdraw", null);
    }
}
