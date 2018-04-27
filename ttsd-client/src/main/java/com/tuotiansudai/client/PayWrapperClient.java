package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.message.RepaySuccessAsyncCallBackMessage;
import com.tuotiansudai.message.RepaySuccessMessage;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PayWrapperClient extends BaseClient {

    static Logger logger = Logger.getLogger(PayWrapperClient.class);

    @Value("${pay.host}")
    protected String host;

    @Value("${pay.port}")
    protected String port;

    @Value("${pay.application.context}")
    protected String applicationContext;

    private final static String registerPath = "/register";

    private final static String systemRechargePath = "/system-recharge";

    private final static String membershipPrivilegePurchasePath = "/membership-privilege-purchase";

    private final static String rechargePath = "/recharge";

    private final static String bindCardPath = "/bind-card";

    private final static String replaceCardPath = "/bind-card/replace";

    private final static String withdrawPath = "/withdraw";

    private final static String investPath = "/invest";

    private final static String autoInvestPath = "/auto-invest";

    private final static String autoRepayPath = "/auto-repay";

    private final static String agreementPath = "/agreement";

    private final static String repayPath = "/repay";

    private final static String cancelLoanPath = "/loan/{0}/cancel";

    private final static String resetUmpayPassword = "/reset-umpay-password";

    private final static String purchase = "/invest-transfer/purchase";

    private final static String noPasswordPurchase = "/invest-transfer/no-password-purchase";

    private final static String noPasswordInvestPath = "/no-password-invest";

    private final static String transferCashPath = "/transfer-cash";

    private final static String creditLoanRecharge = "/credit-loan/recharge";

    private final static String noPasswordCreditLoanRecharge = "/credit-loan/no-password-recharge";

    private final static String creditLoanTransferAgent = "/credit-loan/transfer-agent";

    private final static String payrollPath = "/payroll/pay";

    public PayWrapperClient() {
        this.okHttpClient.setConnectTimeout(180, TimeUnit.SECONDS);
        this.okHttpClient.setReadTimeout(180, TimeUnit.SECONDS);
        this.okHttpClient.setWriteTimeout(180, TimeUnit.SECONDS);
    }

    public BaseDto<PayDataDto> transferCash(Object transferCashDto) {
        return syncExecute(transferCashDto, transferCashPath);
    }

    public BaseDto<PayDataDto> InviteHelpActivityTransferCash(Object InviteHelpActivityPayCashDto) {
        return syncExecute(InviteHelpActivityPayCashDto, "/transfer-cash/invite-help-activity");
    }

    public boolean resetUmpayPassword(Object resetUmpayPasswordDto) {
        BaseDto<PayDataDto> baseDto = syncExecute(resetUmpayPasswordDto, resetUmpayPassword);
        return baseDto.isSuccess();
    }

    public BaseDto<PayDataDto> register(Object registerAccountDto) {
        return syncExecute(registerAccountDto, registerPath);
    }

    public BaseDto<PayFormDataDto> recharge(Object rechargeDto) {
        return asyncExecute(rechargeDto, rechargePath);
    }

    public BaseDto<PayFormDataDto> systemRecharge(Object systemRechargeDto) {
        return asyncExecute(systemRechargeDto, systemRechargePath);
    }

    public BaseDto<PayFormDataDto> membershipPrivilegePurchase(Object membershipPrivilegePurchaseDto) {
        return asyncExecute(membershipPrivilegePurchaseDto, membershipPrivilegePurchasePath);
    }

    public BaseDto<PayFormDataDto> withdraw(Object withdrawDto) {
        return asyncExecute(withdrawDto, withdrawPath);
    }

    public BaseDto<PayFormDataDto> bindBankCard(Object bindBankCardDto) {
        return asyncExecute(bindBankCardDto, bindCardPath);
    }

    public BaseDto<PayFormDataDto> replaceBankCard(Object bindBankCardDto) {
        return asyncExecute(bindBankCardDto, replaceCardPath);
    }

    public BaseDto<PayFormDataDto> invest(Object investDto) {
        return asyncExecute(investDto, investPath);
    }

    public BaseDto<PayDataDto> cancelLoan(long loanId) {
        return syncExecute(null, MessageFormat.format(cancelLoanPath, String.valueOf(loanId)));
    }

    public BaseDto<PayFormDataDto> agreement(Object agreementDto) {
        return asyncExecute(agreementDto, agreementPath);
    }

    public BaseDto<PayFormDataDto> repay(Object repayDto) {
        return asyncExecute(repayDto, repayPath);
    }

    public BaseDto<PayDataDto> autoInvest(long loanId) {
        return syncExecute(String.valueOf(loanId), autoInvestPath);
    }

    public BaseDto<PayFormDataDto> purchase(Object investDto) {
        return asyncExecute(investDto, purchase);
    }

    public BaseDto<PayFormDataDto> creditLoanRecharge(Object creditLoanRechargeDto) {
        return asyncExecute(creditLoanRechargeDto, creditLoanRecharge);
    }

    public BaseDto<PayDataDto> noPasswordCreditLoanRecharge(Object creditLoanRechargeDto) {
        return syncExecute(creditLoanRechargeDto, noPasswordCreditLoanRecharge);
    }

    public BaseDto<PayDataDto> creditLoanTransferAgent() {
        return syncExecute(null, creditLoanTransferAgent);
    }

    public BaseDto<PayDataDto> noPasswordPurchase(Object investDto) {
        return syncExecute(investDto, noPasswordPurchase);
    }

    public BaseDto<PayDataDto> autoRepay(long loanRepayId) {
        return syncExecute(String.valueOf(loanRepayId), autoRepayPath);
    }

    public BaseDto<PayDataDto> createLoan(long loanId) {
        return syncExecute(null, MessageFormat.format("/loan/{0}", String.valueOf(loanId)));
    }

    public BaseDto<PayDataDto> investCallback(String orderId) {
        return syncExecute(orderId, "/job/async_invest_notify");
    }

    public BaseDto<PayDataDto> investTransferCallback(String notifyRequestId) {
        return syncExecute(notifyRequestId, "/job/async_invest_transfer_notify");
    }

    public BaseDto<PayDataDto> loanOut(long loanId) {
        return syncExecute(null, MessageFormat.format("/loan/{0}/loan-out", String.valueOf(loanId)));
    }

    public BaseDto<PayDataDto> noPasswordInvest(Object investDto) {
        return syncExecute(investDto, noPasswordInvestPath);
    }

    public BaseDto<PayDataDto> loanOutSuccessNotify(long loanId) {
        return syncExecute(String.valueOf(loanId), "/job/loan-out-success-notify");
    }

    public Map<String, String> getUserStatus(String loginName) {
        String json = this.execute(MessageFormat.format("/real-time/user/{0}", loginName), null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    public Map<String, String> getUserBalance(String loginName) {
        try {
            String json = this.execute(MessageFormat.format("/real-time/user-balance/{0}", loginName), null, "GET");
            if (json == null)
                return null;
            else
                return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
                });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public Map<String, String> getLoanStatus(long loanId) {
        String json = this.execute(MessageFormat.format("/real-time/loan/{0}", String.valueOf(loanId)), null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    public Map<String, String> getTransferStatus(String orderId, Date merDate, String businessType) {
        String json = this.execute(MessageFormat.format("/real-time/transfer/order-id/{0}/mer-date/{1}/business-type/{2}", orderId, new DateTime(merDate).toString("yyyyMMdd"), businessType), null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public Map<String, String> getPlatformStatus() {
        String json = this.execute("/real-time/platform", null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Maps.newHashMap();
    }

    public List<List<String>> getTransferBill(String loginName, Date startDate, Date endDate) {
        String json = this.execute(MessageFormat.format("/transfer-bill/user/{0}/start-date/{1}/end-date/{2}",
                loginName,
                new SimpleDateFormat("yyyyMMdd").format(startDate),
                new SimpleDateFormat("yyyyMMdd").format(endDate)), null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<List<List<String>>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Lists.newArrayList();
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

    private BaseDto<PayDataDto> syncExecute(Object requestData, String requestPath) {
        try {
            String responseJson = this.execute(requestPath, requestData != null ? objectMapper.writeValueAsString(requestData) : null, "POST");
            return this.parsePayResponseJson(responseJson);
        } catch (JsonProcessingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        return baseDto;
    }

    private BaseDto<PayFormDataDto> asyncExecute(Object requestData, String requestPath) {
        try {
            String responseJson = this.execute(requestPath, requestData != null ? objectMapper.writeValueAsString(requestData) : null, "POST");
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

    public BaseDto<PayDataDto> autoLoanOutAfterRaisingComplete(long loanId) {
        return syncExecute(String.valueOf(loanId), "/job/auto-loan-out-after-raising-complete");
    }

    public BaseDto<PayDataDto> sendRedEnvelopeAfterLoanOut(long loanId) {
        return syncExecute(String.valueOf(loanId), "/loan-out/send-red-envelope-after-loan-out");
    }

    public BaseDto<PayDataDto> sendRewardReferrer(long loanId) {
        return syncExecute(String.valueOf(loanId), "/loan-out/referrer-reward-after-loan-out");
    }

    public BaseDto<PayDataDto> generateRepay(long loanId) {
        return syncExecute(String.valueOf(loanId), "/loan-out/generate-repay-after-loan-out");
    }

    public BaseDto<PayDataDto> generateCouponRepay(long loanId) {
        return syncExecute(String.valueOf(loanId), "/loan-out/generate-coupon-repay-after-loan-out");
    }

    public BaseDto<PayDataDto> generateExtraRate(long loanId) {
        return syncExecute(String.valueOf(loanId), "/loan-out/generate-extra-rate-after-loan-out");
    }

    public BaseDto<PayDataDto> assignInvestAchievementUserCoupon(long loanId) {
        return syncExecute(String.valueOf(loanId), "/loan-out/assign-achievement-coupon-after-loan-out");
    }

    public BaseDto<PayDataDto> transferReferrerRewardCallBack(long investReferrerRewardId) {
        return syncExecute(String.valueOf(investReferrerRewardId), "/loan-out/transfer-referrer-reward-callback");
    }

    public BaseDto<PayDataDto> transferRedEnvelopForCallback(long userCouponId) {
        return syncExecute(String.valueOf(userCouponId), "/loan-out/transfer-red-envelop-callback");
    }

    public BaseDto<PayDataDto> postInvestRepay(RepaySuccessMessage repaySuccessMessage) {
        return syncExecute(repaySuccessMessage, "/repay-success/post_invest_repay");
    }

    public BaseDto<PayDataDto> extraRateRepayAfterRepaySuccess(RepaySuccessMessage repaySuccessMessage) {
        return syncExecute(repaySuccessMessage, "/repay-success/extra-rate-repay");
    }

    public BaseDto<PayDataDto> extraRateRepayCallbackAfterRepaySuccess(long notifyRequestId) {
        return syncExecute(String.valueOf(notifyRequestId), "/repay-success/async_extra_rate_repay_notify");
    }

    public BaseDto<PayDataDto> couponRepayAfterRepaySuccess(RepaySuccessMessage repaySuccessMessage) {
        return syncExecute(repaySuccessMessage, "/repay-success/coupon-repay");
    }

    public BaseDto<PayDataDto> couponRepayCallbackAfterRepaySuccess(long notifyRequestId) {
        return syncExecute(String.valueOf(notifyRequestId), "/repay-success/async_coupon_repay_notify");
    }

    public BaseDto<PayDataDto> repayInvestPayback(RepaySuccessAsyncCallBackMessage repaySuccessAsyncCallBackMessage) {
        return syncExecute(repaySuccessAsyncCallBackMessage, "/repay-success/async_invest_repay_notify");
    }

    public BaseDto<PayDataDto> experienceRepay(long investId) {
        return syncExecute(String.valueOf(investId), "/experience/repay");
    }

    public BaseDto<PayDataDto> postExperienceRepay(long notifyRequestId) {
        return syncExecute(String.valueOf(notifyRequestId), "/experience/post-repay");
    }

    public BaseDto<PayDataDto> validateFrontCallback(Map<String, String> params) {
        return syncExecute(params, "/validate-front-callback");
    }

    public BaseDto<PayDataDto> investStatusValidate(String uri) {
        return syncExecute(null, uri);
    }

    public BaseDto<BaseDataDto> payroll(long payrollId) {
        try {
            String responseJson = this.execute(payrollPath, String.valueOf(payrollId), "POST");
            return objectMapper.readValue(responseJson, new TypeReference<BaseDto<BaseDataDto>>() {
            });
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        return baseDto;
    }
}
