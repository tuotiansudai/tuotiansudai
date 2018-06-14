package com.tuotiansudai.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.sms.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

@Component
public class SmsWrapperClient extends BaseClient {

    static Logger logger = Logger.getLogger(SmsWrapperClient.class);

    @Value("${sms.host}")
    protected String host;

    @Value("${sms.port}")
    protected String port;

    @Value("${sms.application.context}")
    protected String applicationContext;

    @Value("${common.environment}")
    private Environment environment;

    private final static String REGISTER_CAPTCHA_SMS_URI = "/sms/register-captcha";

    private final static String NO_PASSWORD_INVEST_CAPTCHA_SMS_URI = "/sms/no-password-invest-captcha";

    private final static String RETRIEVE_PASSWORD_CAPTCHA_URI = "/sms/retrieve-password-captcha";

    private final static String FATAL_NOTIFY_URL = "/sms/fatal-notify";

    private final static String PLATFORM_BALANCE_LOW_NOTIFY = "/sms/platform-balance-low-notify";

    private final static String GENERATE_CONTRACT_ERROR_NOTIFY = "/sms/generate-contract-error-notify";

    private final static String PASSWORD_CHANGED_NOTIFY_URI = "/sms/mobile/{mobile}/password-changed-notify";

    private final static String LOAN_REPAY_NOTIFY_URL = "/sms/loan-repay-notify";

    private final static String CANCEL_TRANSFER_LOAN_URI = "/sms/cancel-transfer-loan";

    private final static String IMPORT_USER_RECEIVE_MEMBERSHIP_URI = "/sms/import-user-receive-membership";

    private final static String NEW_USER_RECEIVE_MEMBERSHIP_URI = "/sms/new-user-receive-membership";

    private final static String LOAN_RAISING_COMPLETE_NOTIFY_URI = "/sms/loan-raising-complete-notify";

    private final static String COUPON_ASSIGN_SUCCESS = "/sms/coupon-assign-success";

    private final static String COUPON_EXPIRED_NOTIFY = "/sms/coupon-expired-notify";

    private final static String CREDIT_LOAN_BALANCE_ALERT = "/sms/credit-loan-balance-alert";

    public BaseDto<SmsDataDto> sendRegisterCaptchaSms(SmsCaptchaDto dto) {
        return send(dto, REGISTER_CAPTCHA_SMS_URI);
    }

    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptchaSms(SmsCaptchaDto dto) {
        return send(dto, NO_PASSWORD_INVEST_CAPTCHA_SMS_URI);
    }

    public BaseDto<SmsDataDto> sendRetrievePasswordCaptchaSms(SmsCaptchaDto dto) {
        return send(dto, RETRIEVE_PASSWORD_CAPTCHA_URI);
    }

    public BaseDto<SmsDataDto> sendPasswordChangedNotify(String mobile) {
        return send(null, PASSWORD_CHANGED_NOTIFY_URI.replace("{mobile}", mobile));
    }

    public BaseDto<SmsDataDto> sendFatalNotify(SmsFatalNotifyDto notify) {
        return send(notify, FATAL_NOTIFY_URL);
    }

    public BaseDto<SmsDataDto> sendPlatformBalanceLowNotify(PlatformBalanceLowNotifyDto notify) {
        return send(notify, PLATFORM_BALANCE_LOW_NOTIFY);
    }

    public BaseDto<SmsDataDto> sendGenerateContractErrorNotify(GenerateContractErrorNotifyDto notify) {
        return send(notify, GENERATE_CONTRACT_ERROR_NOTIFY);
    }

    public BaseDto<SmsDataDto> sendLoanRepayNotify(RepayNotifyDto dto) {
        return send(dto, LOAN_REPAY_NOTIFY_URL);
    }

    public BaseDto<SmsDataDto> sendCancelTransferLoanNotify(SmsCancelTransferLoanNotifyDto dto) {
        return send(dto, CANCEL_TRANSFER_LOAN_URI);
    }

    public BaseDto<SmsDataDto> sendImportUserReceiveMembership(SmsUserReceiveMembershipDto dto) {
        return send(dto, IMPORT_USER_RECEIVE_MEMBERSHIP_URI);
    }

    public BaseDto<SmsDataDto> sendNewUserReceiveMembership(SmsUserReceiveMembershipDto dto) {
        return send(dto, NEW_USER_RECEIVE_MEMBERSHIP_URI);
    }

    public BaseDto<SmsDataDto> sendLoanRaisingCompleteNotify(LoanRaisingCompleteNotifyDto dto) {
        return send(dto, LOAN_RAISING_COMPLETE_NOTIFY_URI);
    }

    public BaseDto<SmsDataDto> sendCouponAssignSuccessNotify(SmsCouponNotifyDto dto) {
        return send(dto, COUPON_ASSIGN_SUCCESS);
    }

    public BaseDto<SmsDataDto> sendCouponExpiredNotify(SmsCouponNotifyDto dto) {
        return send(dto, COUPON_EXPIRED_NOTIFY);
    }

    public BaseDto<SmsDataDto> sendCreditLoanBalanceAlert() {
        return send(null, CREDIT_LOAN_BALANCE_ALERT);
    }

    private BaseDto<SmsDataDto> send(Object requestData, String requestPath) {
        BaseDto<SmsDataDto> resultDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        resultDto.setData(dataDto);

        if (Lists.newArrayList(Environment.DEV, Environment.SMOKE).contains(environment)) {
            logger.info(MessageFormat.format("Environment is {0}, ignore sms", environment.name()));
            dataDto.setStatus(true);
            return resultDto;
        }

        try {
            String requestJson = requestData == null ? "" : objectMapper.writeValueAsString(requestData);
            String responseString = this.execute(requestPath, requestJson, "POST");
            if (!Strings.isNullOrEmpty(responseString)) {
                return objectMapper.readValue(responseString, new TypeReference<BaseDto<SmsDataDto>>() {
                });
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return resultDto;
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

    @Override
    public String getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(String applicationContext) {
        this.applicationContext = applicationContext;
    }
}