package com.tuotiansudai.smswrapper.service;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.sms.LoanRaisingCompleteNotifyDto;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.client.SmsClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SmsService {

    static Logger logger = Logger.getLogger(SmsService.class);

    @Autowired
    private SmsClient smsClient;

    @Value("#{'${sms.fatal.dev.mobile}'.split('\\|')}")
    private List<String> fatalNotifyDevMobiles;

    @Value("#{'${sms.fatal.qa.mobile}'.split('\\|')}")
    private List<String> fatalNotifyQAMobiles;

    @Value("${credit.loan.agent}")
    private String creditLoanAgent;

    @Value("${common.environment}")
    private Environment environment;

    public BaseDto<SmsDataDto> sendRegisterCaptcha(String mobile, String captcha, boolean isVoice, String ip) {
        return smsClient.sendSMS(mobile, SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE, isVoice, captcha, ip);
    }

    public BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(String mobile, String captcha, boolean isVoice, String ip) {
        return smsClient.sendSMS(mobile, SmsTemplate.SMS_MOBILE_CAPTCHA_TEMPLATE, isVoice, captcha, ip);
    }

    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(String mobile, String captcha, boolean isVoice, String ip) {
        return smsClient.sendSMS(mobile, SmsTemplate.SMS_NO_PASSWORD_INVEST_CAPTCHA_TEMPLATE, isVoice, captcha, ip);
    }

    public BaseDto<SmsDataDto> sendPasswordChangedNotify(String mobile) {
        return smsClient.sendSMS(mobile, SmsTemplate.SMS_PASSWORD_CHANGED_NOTIFY_TEMPLATE, false, "", "");
    }

    public BaseDto<SmsDataDto> sendFatalNotify(SmsFatalNotifyDto notify) {
        List<String> mobiles = Lists.newArrayList(fatalNotifyQAMobiles);
        if (Environment.PRODUCTION == environment) {
            mobiles.addAll(fatalNotifyDevMobiles);
        }

        List<String> paramList = ImmutableList.<String>builder().add(environment.name()).add(notify.getErrorMessage()).build();
        return smsClient.sendSMS(mobiles, SmsTemplate.SMS_FATAL_NOTIFY_TEMPLATE, false, paramList);
    }

    public BaseDto<SmsDataDto> loanRepayNotify(String mobile, String repayAmount) {
        return smsClient.sendSMS(Lists.newArrayList(mobile), SmsTemplate.SMS_LOAN_REPAY_NOTIFY_TEMPLATE, false, Lists.newArrayList(repayAmount));
    }

    public BaseDto<SmsDataDto> cancelTransferLoan(String mobile, String transferLoanName) {
        return smsClient.sendSMS(Lists.newArrayList(mobile), SmsTemplate.SMS_CANCEL_TRANSFER_LOAN, false, Lists.newArrayList(transferLoanName));
    }

    public BaseDto<SmsDataDto> importUserGetGiveMembership(String mobile, int level) {
        return smsClient.sendSMS(Lists.newArrayList(mobile), SmsTemplate.SMS_IMPORT_RECEIVE_MEMBERSHIP, false, Lists.newArrayList(String.valueOf(level)));
    }

    public BaseDto<SmsDataDto> newUserGetGiveMembership(String mobile, int level) {
        return smsClient.sendSMS(Lists.newArrayList(mobile), SmsTemplate.SMS_NEW_USER_RECEIVE_MEMBERSHIP, false, Lists.newArrayList(String.valueOf(level)));
    }

    private String getCouponName(SmsCouponNotifyDto notifyDto) {
        switch (notifyDto.getCouponType()) {
            case RED_ENVELOPE:
                return notifyDto.getAmount() + "元" + notifyDto.getCouponType().getName();
            case INTEREST_COUPON:
                return notifyDto.getRate() + "%优惠券";
            default:
                return null;
        }
    }

    public BaseDto<SmsDataDto> assignCouponSuccessNotify(SmsCouponNotifyDto notifyDto) {
        String couponName = getCouponName(notifyDto);
        if (null == couponName) {
            return new BaseDto<>(false);
        }
        return smsClient.sendSMS(Lists.newArrayList(notifyDto.getMobile()), SmsTemplate.SMS_COUPON_ASSIGN_SUCCESS_TEMPLATE, false, Lists.newArrayList(couponName));
    }

    public BaseDto<SmsDataDto> couponExpiredNotify(SmsCouponNotifyDto notifyDto) {
        String couponName = getCouponName(notifyDto);
        if (null == couponName) {
            return new BaseDto<>(false);
        }
        return smsClient.sendSMS(Lists.newArrayList(notifyDto.getMobile()), SmsTemplate.SMS_COUPON_EXPIRED_NOTIFY_TEMPLATE, false, Lists.newArrayList(couponName, notifyDto.getExpiredDate()));
    }

    public BaseDto<SmsDataDto> creditLoanBalanceAlert() {
        return smsClient.sendSMS(Lists.newArrayList(creditLoanAgent), SmsTemplate.SMS_CREDIT_LOAN_BALANCE_ALERT_TEMPLATE, false, Lists.newArrayList());
    }

    public BaseDto<SmsDataDto> platformBalanceLowNotify(List<String> mobiles, String warningLine) {
        return smsClient.sendSMS(mobiles, SmsTemplate.SMS_PLATFORM_BALANCE_LOW_NOTIFY_TEMPLATE, false, Lists.newArrayList(warningLine));
    }

    public BaseDto<SmsDataDto> generateContractNotify(List<String> mobiles, long businessId) {
        return smsClient.sendSMS(mobiles, SmsTemplate.SMS_GENERATE_CONTRACT_ERROR_NOTIFY_TEMPLATE, false, Lists.newArrayList(String.valueOf(businessId)));
    }

    public BaseDto<SmsDataDto> loanRaisingCompleteNotify(LoanRaisingCompleteNotifyDto dto) {
        String[] paramArr = {dto.getLoanRaisingStartDate(), dto.getLoanDuration(), dto.getLoanAmount(),
                dto.getLoanRaisingCompleteTime(), "借款人：" + dto.getLoanerName(), "代理人：" + dto.getAgentName()};
        List<String> paramList = Arrays.asList(paramArr);
        return smsClient.sendSMS(dto.getMobiles(), SmsTemplate.SMS_LOAN_RAISING_COMPLETE_NOTIFY_TEMPLATE, false, paramList);
    }
}