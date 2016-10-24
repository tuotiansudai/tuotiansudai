package com.tuotiansudai.smswrapper.service.impl;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.sms.InvestSmsNotifyDto;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.client.SmsClient;
import com.tuotiansudai.smswrapper.repository.mapper.*;
import com.tuotiansudai.smswrapper.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsClient smsClient;

    @Value("#{'${sms.fatal.dev.mobile}'.split('\\|')}")
    private List<String> fatalNotifyDevMobiles;

    @Value("#{'${sms.fatal.qa.mobile}'.split('\\|')}")
    private List<String> fatalNotifyQAMobiles;

    @Value("${common.environment}")
    private Environment environment;

    @Override
    public BaseDto<SmsDataDto> sendRegisterCaptcha(String mobile, String captcha, String ip) {
        return smsClient.sendSMS(RegisterCaptchaMapper.class, mobile, SmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE, captcha, ip);
    }

    @Override
    public BaseDto<SmsDataDto> sendInvestNotify(InvestSmsNotifyDto dto) {
        List<String> paramList = ImmutableList.<String>builder().add(dto.getLoanName()).add(dto.getAmount()).build();
        return smsClient.sendSMS(InvestNotifyMapper.class, dto.getMobile(), SmsTemplate.SMS_INVEST_NOTIFY_TEMPLATE, paramList, "");
    }

    @Override
    public BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(String mobile, String captcha, String ip) {
        return smsClient.sendSMS(RetrievePasswordCaptchaMapper.class, mobile, SmsTemplate.SMS_MOBILE_CAPTCHA_TEMPLATE, captcha, ip);
    }

    @Override
    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(String mobile, String captcha, String ip) {
        return smsClient.sendSMS(TurnOffNoPasswordInvestCaptchaMapper.class, mobile, SmsTemplate.SMS_NO_PASSWORD_INVEST_CAPTCHA_TEMPLATE, captcha, ip);
    }

    @Override
    public BaseDto<SmsDataDto> sendPasswordChangedNotify(String mobile) {
        return smsClient.sendSMS(UserPasswordChangedNotifyMapper.class, mobile, SmsTemplate.SMS_PASSWORD_CHANGED_NOTIFY_TEMPLATE, "", "");
    }

    @Override
    public BaseDto<SmsDataDto> sendFatalNotify(SmsFatalNotifyDto notify) {
        List<String> mobiles = Lists.newArrayList(fatalNotifyQAMobiles);
        if (Environment.PRODUCTION == environment) {
            mobiles.addAll(fatalNotifyDevMobiles);
        }

        List<String> paramList = ImmutableList.<String>builder().add(environment.name()).add(notify.getErrorMessage()).build();
        BaseDto<SmsDataDto> dto = smsClient.sendSMS(FatalNotifyMapper.class, mobiles, SmsTemplate.SMS_FATAL_NOTIFY_TEMPLATE, paramList, "");
        return dto;
    }

    @Override
    public BaseDto<SmsDataDto> couponNotify(SmsCouponNotifyDto notifyDto) {
        String couponName = (notifyDto.getCouponType() == CouponType.INTEREST_COUPON ? MessageFormat.format("+{0}%", notifyDto.getRate()) : MessageFormat.format("{0}元", notifyDto.getAmount()))
                + notifyDto.getCouponType().getName();

        List<String> paramList = ImmutableList.<String>builder().add(couponName).add(notifyDto.getExpiredDate()).build();
        return smsClient.sendSMS(CouponNotifyMapper.class, notifyDto.getMobile(),SmsTemplate.SMS_COUPON_NOTIFY_TEMPLATE, paramList, "");
    }

    @Override
    public BaseDto<SmsDataDto> birthdayNotify(SmsCouponNotifyDto notifyDto) {
        return smsClient.sendSMS(CouponNotifyMapper.class, notifyDto.getMobile(), SmsTemplate.SMS_BIRTHDAY_NOTIFY_TEMPLATE, "", "");
    }

    @Override
    public BaseDto<SmsDataDto> loanRepayNotify(String mobile, String repayAmount) {
        return smsClient.sendSMS(LoanRepayNotifyMapper.class, mobile, SmsTemplate.SMS_LOAN_REPAY_NOTIFY_TEMPLATE, repayAmount, "");
    }

    @Override
    public BaseDto<SmsDataDto> experienceRepayNotify(List<String> mobiles, String repayAmount) {
        return smsClient.sendSMS(ExperienceRepayNotifyMapper.class, mobiles, SmsTemplate.SMS_EXPERIENCE_REPAY_NOTIFY_TEMPLATE, repayAmount, "");
    }

    @Override
    public BaseDto<SmsDataDto> cancelTransferLoan(String mobile, String transferLoanName) {
        return smsClient.sendSMS(TransferLoanNotifyMapper.class, mobile, SmsTemplate.SMS_CANCEL_TRANSFER_LOAN, transferLoanName, "");
    }

    @Override
    public BaseDto<SmsDataDto> importUserGetGiveMembership(String mobile, int level) {
        return smsClient.sendSMS(MembershipGiveNotifyMapper.class, mobile, SmsTemplate.SMS_IMPORT_RECEIVE_MEMBERSHIP, String.valueOf(level), "");
    }

    @Override
    public BaseDto<SmsDataDto> newUserGetGiveMembership(String mobile, int level) {
        return smsClient.sendSMS(MembershipGiveNotifyMapper.class, mobile, SmsTemplate.SMS_NEW_USER_RECEIVE_MEMBERSHIP, String.valueOf(level), "");
    }

    @Override
    public BaseDto<SmsDataDto> platformBalanceLowNotify(List<String> mobiles, String warningLine) {
        return smsClient.sendSMS(PlatformBalanceLowNotifyMapper.class, mobiles, SmsTemplate.SMS_PLATFORM_BALANCE_LOW_NOTIFY_TEMPLATE, warningLine, "");
    }
}