package com.tuotiansudai.smswrapper.service;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.FADD;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.dto.sms.*;
import com.tuotiansudai.smswrapper.JianZhouSmsTemplate;
import com.tuotiansudai.smswrapper.client.JianZhouSmsClient;
import com.tuotiansudai.smswrapper.repository.mapper.JianZhouSmsHistoryMapper;
import com.tuotiansudai.smswrapper.repository.model.JianZhouSmsHistoryModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SmsService {

    static Logger logger = Logger.getLogger(SmsService.class);

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
    private JianZhouSmsClient jianZhouSmsClient = JianZhouSmsClient.getClient();

    @Autowired
    private JianZhouSmsHistoryMapper jianZhouSmsHistoryMapper;

    public BaseDto<SmsDataDto> sendRegisterCaptcha(String mobile, String captcha, boolean isVoice, String ip) {
        return sendSMS(Lists.newArrayList(mobile), JianZhouSmsTemplate.SMS_REGISTER_CAPTCHA_TEMPLATE, isVoice, Lists.newArrayList(captcha), ip);
    }

    public BaseDto<SmsDataDto> sendRegisterSuccess(SmsRegisterSuccessNotifyDto notifyDto) {
        if (!Strings.isNullOrEmpty(notifyDto.getReferrerMobile())){
            String mobile = notifyDto.getMobile().substring(0, 3) + "****" + notifyDto.getMobile().substring(7);
            sendSMS(Lists.newArrayList(notifyDto.getMobile()), JianZhouSmsTemplate.SMS_REGISTER_SUCCESS_REFERRER_TEMPLATE, false, Lists.newArrayList(mobile), null);
        }
        return sendSMS(Lists.newArrayList(notifyDto.getMobile()), JianZhouSmsTemplate.SMS_REGISTER_SUCCESS_TEMPLATE, false, Lists.newArrayList(), null);
    }

    public BaseDto<SmsDataDto> sendRetrievePasswordCaptcha(String mobile, String captcha, boolean isVoice, String ip) {
        return sendSMS(Lists.newArrayList(mobile), JianZhouSmsTemplate.SMS_MOBILE_CAPTCHA_TEMPLATE, isVoice, Lists.newArrayList(captcha), ip);
    }

    public BaseDto<SmsDataDto> sendNoPasswordInvestCaptcha(String mobile, String captcha, boolean isVoice, String ip) {
        return sendSMS(Lists.newArrayList(mobile), JianZhouSmsTemplate.SMS_NO_PASSWORD_INVEST_CAPTCHA_TEMPLATE, isVoice, Lists.newArrayList(captcha), ip);
    }

    public BaseDto<SmsDataDto> sendPasswordChangedNotify(String mobile) {
        return sendSMS(Lists.newArrayList(mobile), JianZhouSmsTemplate.SMS_PASSWORD_CHANGED_NOTIFY_TEMPLATE, false, Lists.newArrayList(), "");
    }

    public BaseDto<SmsDataDto> sendFatalNotify(SmsFatalNotifyDto notify) {
        List<String> mobiles = Lists.newArrayList(fatalNotifyQAMobiles);
        if (Environment.PRODUCTION == environment) {
            mobiles.addAll(fatalNotifyDevMobiles);
        }
        return sendSMS(mobiles, JianZhouSmsTemplate.SMS_FATAL_NOTIFY_TEMPLATE, false, Lists.newArrayList(notify.getErrorMessage()), null);
    }

    public BaseDto<SmsDataDto> loanRepayNotify(String mobile, String repayAmount) {
        return sendSMS(Lists.newArrayList(mobile), JianZhouSmsTemplate.SMS_LOAN_REPAY_NOTIFY_TEMPLATE, false, Lists.newArrayList(repayAmount), null);
    }

    public BaseDto<SmsDataDto> cancelTransferLoan(String mobile, String transferLoanName) {
        return sendSMS(Lists.newArrayList(mobile), JianZhouSmsTemplate.SMS_CANCEL_TRANSFER_LOAN_TEMPLATE, false, Lists.newArrayList(transferLoanName), null);
    }

    public BaseDto<SmsDataDto> transferLoanSuccess(String mobile, String transferLoanName) {
        return sendSMS(Lists.newArrayList(mobile), JianZhouSmsTemplate.SMS_TRANSFER_LOAN_SUCCESS_TEMPLATE, false, Lists.newArrayList(transferLoanName), null);
    }

    public BaseDto<SmsDataDto> transferLoanOverdue(String mobile, String transferLoanName) {
        return sendSMS(Lists.newArrayList(mobile), JianZhouSmsTemplate.SMS_TRANSFER_LOAN_OVERDUE_TEMPLATE, false, Lists.newArrayList(transferLoanName), null);
    }

    public BaseDto<SmsDataDto> advancedRepay(InvestSmsNotifyDto dto) {
        return sendSMS(Lists.newArrayList(dto.getMobile()), JianZhouSmsTemplate.SMS_ADVANCED_REPAY_TEMPLATE, false, Lists.newArrayList(dto.getLoanName()), null);
    }

    public BaseDto<SmsDataDto> membershipPrivilegeBySuccess(SmsMembershipPrivilegeNotifyDto dto) {
        return sendSMS(dto.getMobiles(), JianZhouSmsTemplate.SMS_MEMBERSHIP_PRIVILEGE_BUY_SUCCESS_TEMPLATE, false, Lists.newArrayList(), null);
    }

    public BaseDto<SmsDataDto> membershipPrivilegeExpired(SmsMembershipPrivilegeNotifyDto dto) {
        return sendSMS(dto.getMobiles(), JianZhouSmsTemplate.SMS_MEMBERSHIP_PRIVILEGE_EXPIRED_TEMPLATE, false, Lists.newArrayList(), null);
    }

    public BaseDto<SmsDataDto> payroll(SmsPayrollNotifyDto dto) {
        return sendSMS(dto.getMobiles(), JianZhouSmsTemplate.SMS_MEMBERSHIP_PRIVILEGE_BUY_SUCCESS_TEMPLATE, false, Lists.newArrayList(dto.getSendDate()), null);
    }

    public BaseDto<SmsDataDto> usePoint(SmsUsePointNotifyDto dto) {
        return sendSMS(Lists.newArrayList(dto.getMobile()), JianZhouSmsTemplate.SMS_MEMBERSHIP_PRIVILEGE_BUY_SUCCESS_TEMPLATE, false, Lists.newArrayList(dto.getUsePoint(), dto.getSurplusPoint()), null);
    }

    public BaseDto<SmsDataDto> membershipUpgrade(SmsUserReceiveMembershipDto dto) {
        return sendSMS(Lists.newArrayList(dto.getMobile()), JianZhouSmsTemplate.SMS_MEMBERSHIP_UPGRADE_TEMPLATE, false, Lists.newArrayList(dto.getLevel(), dto.getLevel()), null);
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
        return sendSMS(Lists.newArrayList(notifyDto.getMobile()), JianZhouSmsTemplate.SMS_COUPON_ASSIGN_SUCCESS_TEMPLATE, false, Lists.newArrayList(couponName, String.valueOf(notifyDto.getDeadLine())), null);
    }

    public BaseDto<SmsDataDto> couponExpiredNotify(SmsCouponNotifyDto notifyDto) {
        return sendSMS(Lists.newArrayList(notifyDto.getMobile()), JianZhouSmsTemplate.SMS_COUPON_EXPIRED_NOTIFY_TEMPLATE, false, Lists.newArrayList(String.valueOf(notifyDto.getExpiredCount())), null);
    }

    public BaseDto<SmsDataDto> creditLoanBalanceAlert() {
        return sendSMS(Lists.newArrayList(creditLoanAgent), JianZhouSmsTemplate.SMS_CREDIT_LOAN_BALANCE_ALERT_TEMPLATE, false, Lists.newArrayList(), null);
    }

    public BaseDto<SmsDataDto> platformBalanceLowNotify(List<String> mobiles, String warningLine) {
        return sendSMS(mobiles, JianZhouSmsTemplate.SMS_PLATFORM_BALANCE_LOW_NOTIFY_TEMPLATE, false, Lists.newArrayList(warningLine), null);
    }

    public BaseDto<SmsDataDto> generateContractNotify(List<String> mobiles, long businessId) {
        return sendSMS(mobiles, JianZhouSmsTemplate.SMS_GENERATE_CONTRACT_ERROR_NOTIFY_TEMPLATE, false, Lists.newArrayList(String.valueOf(businessId)), null);
    }

    public BaseDto<SmsDataDto> loanRaisingCompleteNotify(LoanRaisingCompleteNotifyDto dto) {
        String[] paramArr = {dto.getLoanRaisingStartDate(), dto.getLoanDuration(), dto.getLoanAmount(),
                dto.getLoanRaisingCompleteTime(), "借款人：" + dto.getLoanerName(), "代理人：" + dto.getAgentName()};
        List<String> paramList = Arrays.asList(paramArr);
        return sendSMS(dto.getMobiles(), JianZhouSmsTemplate.SMS_LOAN_RAISING_COMPLETE_NOTIFY_TEMPLATE, false, paramList, null);
    }

    public BaseDto<SmsDataDto> loanOutCompleteNotify(LoanOutCompleteNotifyDto dto) {
        return sendSMS(dto.getMobiles(), JianZhouSmsTemplate.SMS_LOAN_RAISING_COMPLETE_NOTIFY_TEMPLATE, false, Lists.newArrayList(dto.getLoanName(), dto.getBaseRate()), null);
    }

    private BaseDto<SmsDataDto> sendSMS(List<String> mobileList, JianZhouSmsTemplate template, boolean isVoice, List<String> paramList, String restrictedIP) {
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

        List<JianZhouSmsHistoryModel> models = createSmsHistory(mobileList, template, paramList, isVoice);
        String response = jianZhouSmsClient.sendSms(isVoice, mobileList, template, paramList, null);
        updateSmsHistory(models, response);

        if(response == null || Long.parseLong(response) < 0){
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

    private List<JianZhouSmsHistoryModel> createSmsHistory(List<String> mobileList, JianZhouSmsTemplate template, List<String> paramList, boolean isVoice){
        List<JianZhouSmsHistoryModel> models = new ArrayList<>();
        int count = mobileList.size();
        int batchSize = count / 10 + (count % 1000 > 0 ? 1 : 0);
        for (int batch = 0 ; batch < batchSize ; batchSize++){
            JianZhouSmsHistoryModel model = new JianZhouSmsHistoryModel(
                    String.join(", ", mobileList.subList(batch * 10, (batch + 1) * 10 > count ? count : ((batch + 1) * 10))),
                    template.generateContent(isVoice, paramList),
                    isVoice);
            jianZhouSmsHistoryMapper.create(model);
            models.add(model);
        }
        return models;
    }

    private void updateSmsHistory(List<JianZhouSmsHistoryModel> models, String response){
        models.forEach(model -> {
            model.setResponse(response);
            jianZhouSmsHistoryMapper.update(model);
        });
    }
}