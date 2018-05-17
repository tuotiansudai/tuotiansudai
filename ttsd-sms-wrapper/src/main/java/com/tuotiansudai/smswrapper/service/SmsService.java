package com.tuotiansudai.smswrapper.service;


import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.sms.LoanRaisingCompleteNotifyDto;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.client.JianZhouSmsClient;
import com.tuotiansudai.smswrapper.client.SmsClient;
import com.tuotiansudai.smswrapper.provider.SmsProvider;
import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
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

    @Value("${sms.interval.seconds}")
    private int second;

    @Value("#{'${sms.antiCooldown.ipList}'.split('\\|')}")
    private List<String> antiCoolDownIpList;

    private final static int QA_DAILY_LIMIT = 100;

    private final static String SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE = "sms_ip_restricted:{0}";

    private final static String QA_SEND_COUNT_PER_DAY = "qa_send_count_per_day:{0}";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private JianZhouSmsClient jianZhouSmsClient;

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

        if (notify.getErrorMessage().length() > 20){
            notify.setErrorMessage(notify.getErrorMessage().substring(0, 20));
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

    private BaseDto<SmsDataDto> sendSMS(List<String> mobileList, SmsTemplate template, boolean isVoice, List<String> paramList, String restrictedIP) {
        SmsDataDto data = new SmsDataDto();
        BaseDto<SmsDataDto> dto = new BaseDto<>(data);

        if (Lists.newArrayList(Environment.SMOKE, Environment.DEV).contains(environment)) {
            logger.info(MessageFormat.format("sms sending ignored in {0} environment", environment));
            data.setStatus(true);
            return dto;
        }

        if (hasExceedQALimit(mobileList)) {
            data.setIsRestricted(true);
            data.setMessage(MessageFormat.format("已超出QA环境当日发送限额{0}", QA_DAILY_LIMIT));
            return dto;
        }

        if (isInCoolDown(restrictedIP)) {
            data.setIsRestricted(true);
            data.setMessage(MessageFormat.format("IP: {0} 受限", restrictedIP));
            return dto;
        }

        List<SmsHistoryModel> smsHistoryModels = smsProvider.sendSMS(mobileList, template, paramList);
        jianZhouSmsClient.sendSms(isVoice, mobileList, template, paramList)

        data.setStatus(CollectionUtils.isNotEmpty(smsHistoryModels) && smsHistoryModels.get(0).isSuccess());
        if(!data.getStatus()){
            data.setIsRestricted(true);
            data.setMessage("短信网关返回失败");
        }

        this.setIntoCoolDown(restrictedIP);

        return dto;
    }

    private boolean hasExceedQALimit(List<String> mobileList) {
        String hKey = DateTime.now().toString("yyyyMMdd");
        String redisValue = redisWrapperClient.hget(QA_SEND_COUNT_PER_DAY, hKey);

        int smsSendSize = Strings.isNullOrEmpty(redisValue) ? 0 : Integer.parseInt(redisValue);
        boolean exceed = smsSendSize + mobileList.size() >= QA_DAILY_LIMIT;
        if (!exceed) {
            smsSendSize += mobileList.size();
            redisWrapperClient.hset(QA_SEND_COUNT_PER_DAY, hKey, String.valueOf(smsSendSize), 172800);
        }
        return Environment.QA == environment && exceed;
    }

    private boolean isInCoolDown(String ip) {
        String redisKey = MessageFormat.format(SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE, ip);
        return redisWrapperClient.exists(redisKey);
    }

    private void setIntoCoolDown(String ip) {
        if (!Strings.isNullOrEmpty(ip) && !antiCoolDownIpList.contains(ip)) {
            String redisKey = MessageFormat.format(SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE, ip);
            redisWrapperClient.setex(redisKey, second, ip);
        }
    }
}