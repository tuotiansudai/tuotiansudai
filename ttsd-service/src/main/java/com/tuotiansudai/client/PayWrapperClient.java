package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Component
public class PayWrapperClient extends BaseClient {

    static Logger logger = Logger.getLogger(PayWrapperClient.class);

    @Value("${pay.host}")
    protected String host;

    @Value("${pay.port}")
    protected String port;

    @Value("${pay.application.context}")
    protected String applicationContext;

    private String registerPath = "/register";

    private String systemRechargePath = "/system-recharge";

    private String rechargePath = "/recharge";

    private String bindCardPath = "/bind-card";

    private String replaceCardPath = "/bind-card/replace";

    private String loanPath = "/loan";

    private String loanOutPath = "/loan/loan-out";

    private String withdrawPath = "/withdraw";

    private String investPath = "/invest";

    private String autoInvestPath = "/auto-invest";

    private String agreementPath = "/agreement";

    private String repayPath = "/repay";

    private String cancelLoanPath = "/loan/{0}/cancel";

    private String resetUmpayPassword = "/reset-umpay-password";

    private String purchase="/invest-transfer/purchase";

    private String noPasswordPurchase = "/invest-transfer/no-password-purchase";

    public boolean resetUmpayPassword(ResetUmpayPasswordDto resetUmpayPasswordDto) {
        BaseDto<PayDataDto> baseDto = syncExecute(resetUmpayPasswordDto, resetUmpayPassword, "POST");
        return baseDto.isSuccess();
    }

    public BaseDto<PayDataDto> register(RegisterAccountDto dto) {
        return syncExecute(dto, registerPath, "POST");
    }

    public BaseDto<PayFormDataDto> recharge(RechargeDto dto) {
        return asyncExecute(dto, rechargePath, "POST");
    }

    public BaseDto<PayFormDataDto> systemRecharge(SystemRechargeDto dto) {
        return asyncExecute(dto, systemRechargePath, "POST");
    }

    public BaseDto<PayFormDataDto> withdraw(WithdrawDto dto) {
        return asyncExecute(dto, withdrawPath, "POST");
    }

    public BaseDto<PayFormDataDto> bindBankCard(BindBankCardDto dto) {
        return asyncExecute(dto, bindCardPath, "POST");
    }

    public BaseDto<PayFormDataDto> replaceBankCard(BindBankCardDto dto) {
        return asyncExecute(dto, replaceCardPath, "POST");
    }

    public BaseDto<PayFormDataDto> invest(InvestDto dto) {
        return asyncExecute(dto, investPath, "POST");
    }

    public BaseDto<PayDataDto> cancelLoan(Long loanId) {
        return syncExecute(null, MessageFormat.format(cancelLoanPath, loanId.toString()), "POST");
    }

    public BaseDto<PayFormDataDto> agreement(AgreementDto dto) {
        return asyncExecute(dto, agreementPath, "POST");
    }

    public BaseDto<PayFormDataDto> repay(RepayDto dto) {
        return asyncExecute(dto, repayPath, "POST");
    }

    public BaseDto<PayDataDto> autoInvest(long loanId) {
        return syncExecute(String.valueOf(loanId), autoInvestPath, "POST");
    }

    public BaseDto<PayFormDataDto> purchase(InvestDto investDto) {
        return asyncExecute(investDto, purchase, "POST");
    }

    public BaseDto<PayDataDto> noPasswordPurchase(InvestDto investDto) {
        return syncExecute(investDto, noPasswordPurchase, "POST");
    }

    public BaseDto<PayDataDto> createLoan(LoanDto dto) {
        return syncExecute(dto, loanPath, "POST");
    }

    public BaseDto<PayDataDto> updateLoan(LoanDto dto) {
        return syncExecute(dto, loanPath, "PUT");
    }

    public BaseDto<PayDataDto> investCallback() {
        return syncExecute(null, "/job/async_invest_notify", "POST");
    }

    public BaseDto<PayDataDto> loanOut(LoanOutDto dto) {
        return syncExecute(dto, loanOutPath, "POST");
    }

    public BaseDto<PayDataDto> checkLoanAmount(long loanId) {
        return syncExecute(null, MessageFormat.format("/real-time/loan/{0}/check-amount", String.valueOf(loanId)), "GET");
    }

    public BaseDto<PayDataDto> loanOutSuccessNotify(long loanId){
        return syncExecute(String.valueOf(loanId), "/job/loan-out-success-notify", "POST");
    }

    public Map<String, String> getUserStatus(String loginName) {
        String json = this.execute(MessageFormat.format("/real-time/user/{0}", loginName), null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    public Map<String, String> getLoanStatus(long loanId) {
        String json = this.execute(MessageFormat.format("/real-time/loan/{0}", String.valueOf(loanId)), null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    public Map<String, String> getTransferStatus(String orderId, Date merDate, String businessType) {
        String json = this.execute(MessageFormat.format("/real-time/transfer/order-id/{0}/mer-date/{1}/business-type/{2}", orderId, new DateTime(merDate).toString("yyyyMMdd"), businessType), null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public Map<String, String> getPlatformStatus() {
        String json = this.execute("/real-time/platform", null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    public BaseDto<PayDataDto> postNormalRepay(long loanRepayId) {
        return syncExecute(loanRepayId, "/job/post_normal_repay", "POST");
    }

    public BaseDto<PayDataDto> postAdvanceRepay(long loanRepayId) {
        return syncExecute(loanRepayId, "/job/post_advance_repay", "POST");
    }

    private BaseDto<PayDataDto> parsePayResponseJson(String json) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        if (Strings.isNullOrEmpty(json)) {
            baseDto.setSuccess(false);
            return baseDto;
        }

        try {
            baseDto = objectMapper.readValue(json, new TypeReference<BaseDto<PayDataDto>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            baseDto.setSuccess(false);
        }
        return baseDto;
    }

    private BaseDto<PayFormDataDto> parsePayFormJson(String json) {
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        if (Strings.isNullOrEmpty(json)) {
            baseDto.setSuccess(false);
            return baseDto;
        }

        try {
            baseDto = objectMapper.readValue(json, new TypeReference<BaseDto<PayFormDataDto>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            baseDto.setSuccess(false);
        }
        return baseDto;
    }

    private BaseDto<PayDataDto> syncExecute(Object requestData, String requestPath, String method) {
        try {
            String responseJson = this.execute(requestPath, requestData != null ? objectMapper.writeValueAsString(requestData) : null, method);
            return this.parsePayResponseJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        return baseDto;
    }

    private BaseDto<PayFormDataDto> asyncExecute(Object requestData, String requestPath, String method) {
        try {
            String responseJson = this.execute(requestPath, requestData != null ? objectMapper.writeValueAsString(requestData) : null, method);
            return this.parsePayFormJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);

        return baseDto;
    }

    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(String applicationContext) {
        this.applicationContext = applicationContext;
    }

    public BaseDto<PayDataDto> autoLoanOutAfterRaisingComplete(long loanId){
        return syncExecute(String.valueOf(loanId), "/job/auto-loan-out-after-raising-complete", "POST");
    }

    public BaseDto<PayDataDto> sendRedEnvelopeAfterLoanOut(long loanId){
        return syncExecute(String.valueOf(loanId), "/job/sed-red-envelope-after-loan-out", "POST");
    }
}
