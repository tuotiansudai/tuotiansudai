package com.tuotiansudai.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.sms.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SmsWrapperClient extends BaseClient {

    static Logger logger = Logger.getLogger(SmsWrapperClient.class);

    @Value("${sms.host}")
    protected String host;

    @Value("${sms.port}")
    protected String port;

    @Value("${sms.application.context}")
    protected String applicationContext;

    private final static String REGISTER_CAPTCHA_SMS_URI = "/sms/register-captcha";

    private final static String NO_PASSWORD_INVEST_CAPTCHA_SMS_URI = "/sms/no-password-invest-captcha";

    private final static String RETRIEVE_PASSWORD_CAPTCHA_URI = "/sms/retrieve-password-captcha";

    private final static String FATAL_NOTIFY_URL = "/sms/fatal-notify";

    private final static String PLATFORM_BALANCE_LOW_NOTIFY = "/sms/platform-balance-low-notify";

    private final static String GENERATE_CONTRACT_ERROR_NOTIFY = "/sms/generate-contract-error-notify";

    private final static String LOAN_OUT_INVESTOR_NOTIFY_URI = "/sms/loan-out-investor-notify";

    private final static String PASSWORD_CHANGED_NOTIFY_URI = "/sms/mobile/{mobile}/password-changed-notify";

    private final static String COUPON_NOTIFY_URI = "/sms/coupon-notify";

    private final static String BIRTHDAY_NOTIFY_URI = "/sms/birthday-notify";

    private final static String LOAN_REPAY_NOTIFY_URL = "/sms/loan-repay-notify";

    private final static String EXPERIENCE_REPAY_NOTIFY_URL = "/sms/experience-repay-notify";

    private final static String CANCEL_TRANSFER_LOAN_URI = "/sms/cancel-transfer-loan";

    private final static String IMPORT_USER_RECEIVE_MEMBERSHIP_URI = "/sms/import-user-receive-membership";

    private final static String NEW_USER_RECEIVE_MEMBERSHIP_URI = "/sms/new-user-receive-membership";

    public BaseDto<SmsDataDto> sendRegisterCaptchaSms(SmsCaptchaDto dto) {
        return send(dto, REGISTER_CAPTCHA_SMS_URI);
    }

    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptchaSms(SmsCaptchaDto dto) {
        return send(dto, NO_PASSWORD_INVEST_CAPTCHA_SMS_URI);
    }

    public BaseDto<SmsDataDto> sendInvestNotify(InvestSmsNotifyDto dto) {
        return send(dto, LOAN_OUT_INVESTOR_NOTIFY_URI);
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

    public BaseDto<SmsDataDto> sendExperienceRepayNotify(ExperienceRepayNotifyDto dto) {
        return send(dto, EXPERIENCE_REPAY_NOTIFY_URL);
    }

    public BaseDto<SmsDataDto> sendCouponNotify(SmsCouponNotifyDto dto) {
        return send(dto, COUPON_NOTIFY_URI);
    }

    public BaseDto<SmsDataDto> sendBirthdayNotify(SmsCouponNotifyDto dto) {
        return send(dto, BIRTHDAY_NOTIFY_URI);
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

    private BaseDto<SmsDataDto> send(Object requestData, String requestPath) {
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
        BaseDto<SmsDataDto> resultDto = new BaseDto<>();
        SmsDataDto dataDto = new SmsDataDto();
        resultDto.setData(dataDto);
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
