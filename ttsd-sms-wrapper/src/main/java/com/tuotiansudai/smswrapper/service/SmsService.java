package com.tuotiansudai.smswrapper.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.smswrapper.client.JianZhouSmsClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Autowired
    private JianZhouSmsClient jianZhouSmsClient = JianZhouSmsClient.getClient();
//
//    public BaseDto<SmsDataDto> sendFatalNotify(SmsFatalNotifyDto notify) {
//        List<String> mobiles = Lists.newArrayList(fatalNotifyQAMobiles);
//        if (Environment.PRODUCTION == environment) {
//            mobiles.addAll(fatalNotifyDevMobiles);
//        }
//        return jianZhouSmsClient.sendSms(mobiles, JianZhouSmsTemplate.SMS_FATAL_NOTIFY_TEMPLATE, false, Lists.newArrayList(notify.getErrorMessage()), null);
//    }
//
//    public BaseDto<SmsDataDto> sendSms(SmsNotifyDto smsNotifyDto) {
//        return jianZhouSmsClient.sendSms(smsNotifyDto.getMobiles(), smsNotifyDto.getJianZhouSmsTemplate(), smsNotifyDto.isVoice(), smsNotifyDto.getParams(), smsNotifyDto.getRequestIP());
//    }

}