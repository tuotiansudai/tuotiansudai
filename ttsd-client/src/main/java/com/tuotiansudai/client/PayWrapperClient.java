package com.tuotiansudai.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
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

    private final static String purchase="/invest-transfer/purchase";

    private final static String noPasswordPurchase = "/invest-transfer/no-password-purchase";

    private final static String noPasswordInvestPath = "/no-password-invest";

    private final static String transferCashPath = "/transfer-cash";

    public BaseDto<PayDataDto> transferCash(Object transferCashDto) {
        return syncExecute(transferCashDto, transferCashPath, "POST");
    }

    public boolean resetUmpayPassword(Object resetUmpayPasswordDto) {
        BaseDto<PayDataDto> baseDto = syncExecute(resetUmpayPasswordDto, resetUmpayPassword, "POST");
        return baseDto.isSuccess();
    }

    public BaseDto<PayDataDto> register(Object registerAccountDto) {
        return syncExecute(registerAccountDto, registerPath, "POST");
    }

    public BaseDto<PayFormDataDto> recharge(Object rechargeDto) {
        return asyncExecute(rechargeDto, rechargePath, "POST");
    }

    public BaseDto<PayFormDataDto> systemRecharge(Object systemRechargeDto) {
        return asyncExecute(systemRechargeDto, systemRechargePath, "POST");
    }

    public BaseDto<PayFormDataDto> membershipPrivilegePurchase(Object membershipPrivilegePurchaseDto) {
        return asyncExecute(membershipPrivilegePurchaseDto, membershipPrivilegePurchasePath, "POST");
    }

    public BaseDto<PayFormDataDto> withdraw(Object withdrawDto) {
        return asyncExecute(withdrawDto, withdrawPath, "POST");
    }

    public BaseDto<PayFormDataDto> bindBankCard(Object bindBankCardDto) {
        return asyncExecute(bindBankCardDto, bindCardPath, "POST");
    }

    public BaseDto<PayFormDataDto> replaceBankCard(Object bindBankCardDto) {
        return asyncExecute(bindBankCardDto, replaceCardPath, "POST");
    }

    public BaseDto<PayFormDataDto> invest(Object investDto) {
        return asyncExecute(investDto, investPath, "POST");
    }

    public BaseDto<PayDataDto> cancelLoan(long loanId) {
        return syncExecute(null, MessageFormat.format(cancelLoanPath, String.valueOf(loanId)), "POST");
    }

    public BaseDto<PayFormDataDto> agreement(Object agreementDto) {
        return asyncExecute(agreementDto, agreementPath, "POST");
    }

    public BaseDto<PayFormDataDto> repay(Object repayDto) {
        return asyncExecute(repayDto, repayPath, "POST");
    }

    public BaseDto<PayDataDto> autoInvest(long loanId) {
        return syncExecute(String.valueOf(loanId), autoInvestPath, "POST");
    }

    public BaseDto<PayFormDataDto> purchase(Object investDto) {
        return asyncExecute(investDto, purchase, "POST");
    }

    public BaseDto<PayDataDto> noPasswordPurchase(Object investDto) {
        return syncExecute(investDto, noPasswordPurchase, "POST");
    }

    public BaseDto<PayDataDto> autoRepay(long loanRepayId) {
        return syncExecute(String.valueOf(loanRepayId), autoRepayPath, "POST");
    }

    public BaseDto<PayDataDto> createLoan(long loanId) {
        return syncExecute(null, MessageFormat.format("/loan/{0}", String.valueOf(loanId)), "POST");
    }

    public BaseDto<PayDataDto> investCallback(String notifyRequestId) {
        return syncExecute(notifyRequestId, "/job/async_invest_notify", "POST");
    }

    public BaseDto<PayDataDto> normalRepayInvestPayback(long notifyRequestId) {
        return syncExecute(notifyRequestId, "/job/async_normal_repay_notify", "POST");
    }

    public BaseDto<PayDataDto> advanceRepayInvestPayback(long notifyRequestId) {
        return syncExecute(notifyRequestId, "/job/async_advance_repay_notify", "POST");
    }

    public BaseDto<PayDataDto> extraRateInvestCallback() {
        return syncExecute(null, "/job/async_extra_rate_invest_notify", "POST");
    }

    public BaseDto<PayDataDto> investTransferCallback(String notifyRequestId) {
        return syncExecute(notifyRequestId, "/job/async_invest_transfer_notify", "POST");
    }

    public BaseDto<PayDataDto> loanOut(long loanId) {
        return syncExecute(null, MessageFormat.format("/loan/{0}/loan-out", String.valueOf(loanId)), "POST");
    }

    public BaseDto<PayDataDto> noPasswordInvest(Object investDto) {
        return syncExecute(investDto, noPasswordInvestPath, "POST");
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

    public Map<String, String> getUserBalance(String loginName) {
        String json = this.execute(MessageFormat.format("/real-time/user-balance/{0}", loginName), null, "GET");
        try {
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

    public List<List<String>> getTransferBill(String loginName, Date startDate, Date endDate) {
        String json = this.execute(MessageFormat.format("/transfer-bill/user/{0}/start-date/{1}/end-date/{2}",
                loginName,
                new SimpleDateFormat("yyyyMMdd").format(startDate),
                new SimpleDateFormat("yyyyMMdd").format(endDate)), null, "GET");
        try {
            return objectMapper.readValue(json, new TypeReference<List<List<String>>>() {});
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Lists.newArrayList();
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
        return syncExecute(String.valueOf(loanId), "/loan-out/send-red-envelope-after-loan-out", "POST");
    }

    public BaseDto<PayDataDto> sendRewardReferrer(long loanId){
        return syncExecute(String.valueOf(loanId), "/loan-out/referrer-reward-after-loan-out", "POST");
    }

    public BaseDto<PayDataDto> createAnXinContract(long loanId){
        return syncExecute(String.valueOf(loanId), "/loan-out/create-anxin-contract-after-loan-out", "POST");
    }

    public BaseDto<PayDataDto> queryAnXinContract(long loanId){
        return syncExecute(String.valueOf(loanId), "/loan-out/query-anxin-contract-after-loan-out", "POST");
    }

    public BaseDto<PayDataDto> generateRepay(long loanId){
        return syncExecute(String.valueOf(loanId), "/loan-out/generate-repay-after-loan-out", "POST");
    }

    public BaseDto<PayDataDto> generateCouponRepay(long loanId){
        return syncExecute(String.valueOf(loanId), "/loan-out/generate-coupon-repay-after-loan-out", "POST");
    }

    public BaseDto<PayDataDto> generateExtraRate(long loanId){
        return syncExecute(String.valueOf(loanId), "/loan-out/generate-extra-rate-after-loan-out", "POST");
    }

    public BaseDto<PayDataDto> assignInvestAchievementUserCoupon(long loanId){
        return syncExecute(String.valueOf(loanId), "/loan-out/assign-achievement-coupon-after-loan-out", "POST");
    }

    public BaseDto<PayDataDto> transferReferrerRewardCallBack(long investReferrerRewardId){
        return syncExecute(String.valueOf(investReferrerRewardId), "/loan-out/transfer-referrer-reward-callback", "POST");
    }

    public BaseDto<PayDataDto> transferRedEnvelopForCallBack(long userCouponId){
        return syncExecute(String.valueOf(userCouponId), "/loan-out/transfer-red-envelop-callback", "POST");
    }

    public BaseDto<PayDataDto> extraRateNormalRepayAfterRepaySuccess(RepaySuccessMessage repaySuccessMessage){
        return syncExecute(repaySuccessMessage, "/repay-success/extra-rate-normal-repay", "POST");
    }
    public BaseDto<PayDataDto> couponRepayAfterRepaySuccess(RepaySuccessMessage repaySuccessMessage){
        return syncExecute(repaySuccessMessage, "/repay-success/coupon-repay", "POST");
    }
    public BaseDto<PayDataDto> couponRepayCallbackAfterRepaySuccess(long notifyRequestId) {
        return syncExecute(String.valueOf(notifyRequestId), "/repay-success/async_coupon_repay_notify", "POST");
    }
    public BaseDto<PayDataDto> experienceRepay(long investId){
        return syncExecute(String.valueOf(investId), "/experience/repay", "POST");
    }

    public BaseDto<PayDataDto> postExperienceRepay(long notifyRequestId) {
        return syncExecute(String.valueOf(notifyRequestId), "/experience/post-repay", "POST");
    }
}
