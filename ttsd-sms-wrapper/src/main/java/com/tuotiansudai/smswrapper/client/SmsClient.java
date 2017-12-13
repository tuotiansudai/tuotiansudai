package com.tuotiansudai.smswrapper.client;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.provider.SmsProvider;
import com.tuotiansudai.smswrapper.repository.model.SmsHistoryModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class SmsClient {

    private static Logger logger = Logger.getLogger(SmsClient.class);

    private final static int QA_DAILY_LIMIT = 100;

    private final static String SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE = "sms_ip_restricted:{0}";

    private final static String QA_SEND_COUNT_PER_DAY = "qa_send_count_per_day:{0}";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Value("${sms.interval.seconds}")
    private int second;

    @Value("#{'${sms.antiCooldown.ipList}'.split('\\|')}")
    private List<String> antiCoolDownIpList;

    @Value("${common.environment}")
    private Environment environment;

    private SmsProvider smsProviderAlidayuText;

    private SmsProvider smsProviderAlidayuVoice;

    @Autowired
    public SmsClient(@Qualifier("smsProviderAlidayuText") SmsProvider smsProviderAlidayuText, @Qualifier("smsProviderAlidayuVoice") SmsProvider smsProviderAlidayuVoice) {
        this.smsProviderAlidayuText = smsProviderAlidayuText;
        this.smsProviderAlidayuVoice = smsProviderAlidayuVoice;
    }

    public BaseDto<SmsDataDto> sendSMS(String mobile, SmsTemplate template, boolean isVoice, String param, String restrictedIP) {
        return sendSMS(Lists.newArrayList(mobile), template, isVoice, Lists.newArrayList(param), restrictedIP);
    }

    public BaseDto<SmsDataDto> sendSMS(List<String> mobileList, SmsTemplate template, boolean isVoice, List<String> paramList) {
        return sendSMS(mobileList, template, isVoice, paramList, "");
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

        SmsProvider smsProvider = isVoice ? this.smsProviderAlidayuVoice : this.smsProviderAlidayuText;

        List<SmsHistoryModel> smsHistoryModels = smsProvider.sendSMS(mobileList, template, paramList);

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
