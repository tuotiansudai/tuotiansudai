package com.tuotiansudai.smswrapper.client;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.SmsDataDto;
import com.tuotiansudai.smswrapper.SmsChannel;
import com.tuotiansudai.smswrapper.SmsTemplate;
import com.tuotiansudai.smswrapper.exception.SmsSendingException;
import com.tuotiansudai.smswrapper.provider.SmsProvider;
import com.tuotiansudai.smswrapper.repository.mapper.BaseMapper;
import com.tuotiansudai.smswrapper.repository.model.SmsModel;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.beans.Introspector;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class SmsClient implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static Logger logger = Logger.getLogger(SmsClient.class);

    private final static String SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE = "sms_ip_restricted:{0}";

    private final static String WYY_SMS_SEND_COUNT_BY_TODAY_TEMPLATE = "wyy_sms_send_count_by_today:{0}";

    private final static int sendSize = 100;

    private final static int lifeSecond = 172800;

    @Value("${sms.interval.seconds}")
    private int second;

    @Value("${common.environment}")
    private String environment;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    @Qualifier("smsProviderNetease")
    private SmsProvider smsProviderPrimary;

    @Autowired
    @Qualifier("smsProviderAlidayu")
    private SmsProvider smsProviderBackup;

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, String mobile, SmsTemplate template, String param) {
        return sendSMS(baseMapperClass, mobile, template, param, "");

    }

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, String mobile, SmsTemplate template, String param, String restrictedIP) {
        List<String> mobileList = Collections.singletonList(mobile);
        List<String> paramList = Collections.singletonList(param);
        return sendSMS(baseMapperClass, mobileList, template, paramList, restrictedIP);
    }

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, String mobile, SmsTemplate template, List<String> paramList) {
        return sendSMS(baseMapperClass, mobile, template, paramList, "");
    }

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, String mobile, SmsTemplate template, List<String> paramList, String restrictedIP) {
        List<String> mobileList = Collections.singletonList(mobile);
        return sendSMS(baseMapperClass, mobileList, template, paramList, restrictedIP);
    }

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, List<String> mobileList, SmsTemplate template, String param) {
        return sendSMS(baseMapperClass, mobileList, template, param, "");
    }

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, List<String> mobileList, SmsTemplate template, String param, String restrictedIP) {
        List<String> paramList = Collections.singletonList(param);
        return sendSMS(baseMapperClass, mobileList, template, paramList, restrictedIP);
    }

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, List<String> mobileList, SmsTemplate template, List<String> paramList) {
        return sendSMS(baseMapperClass, mobileList, template, paramList, "");
    }

    public BaseDto<SmsDataDto> sendSMS(Class<? extends BaseMapper> baseMapperClass, List<String> mobileList, SmsTemplate template, List<String> paramList, String restrictedIP) {
        BaseDto<SmsDataDto> dto = new BaseDto<>();
        SmsDataDto data = new SmsDataDto();
        dto.setData(data);

        if (Arrays.asList(Environment.SMOKE.name(), Environment.DEV.name()).contains(environment)) {
            logger.info("sms sending ignored in current environment");
            data.setStatus(true);
            return dto;
        }

        if (isInCooldown(restrictedIP)) {
            data.setStatus(false);
            data.setIsRestricted(true);
            data.setMessage("ip restricted");
            return dto;
        }

        if (Environment.QA.name().equals(environment) && hasExceedLimit()) {
            logger.info(String.format("sms count exceed limit [%d] in QA environment", sendSize));
            data.setStatus(false);
            data.setMessage("sms count exceed");
            return dto;
        }

        try {
            SmsChannel successChannel = sendSMS(mobileList, template, paramList);

            setIntoCooldown(restrictedIP);
            logSmsRecord(baseMapperClass, mobileList, template, paramList, successChannel);

            data.setStatus(true);
        } catch (SmsSendingException e) {
            data.setStatus(false);
            data.setMessage(e.getLocalizedMessage());
        }
        return dto;
    }

    private SmsChannel sendSMS(List<String> mobileList, SmsTemplate template, List<String> paramList) throws SmsSendingException {
        logger.info(String.format("ready to send sms, mobileList: %s, template: %s, params: %s",
                String.join(",", mobileList),
                template.name(),
                String.join(",", paramList)));

        SmsChannel successChannel = null;
        SmsChannel channel = template.getChannel();
        try {
            if (channel == SmsChannel.Primary || channel == SmsChannel.TryBoth) {
                smsProviderPrimary.sendSMS(mobileList, template, paramList);
                successChannel = SmsChannel.Primary;
            }
            if (channel == SmsChannel.Backup) {
                smsProviderBackup.sendSMS(mobileList, template, paramList);
                successChannel = SmsChannel.Backup;
            }
        } catch (SmsSendingException e) {
            if (channel == SmsChannel.TryBoth) {
                logger.warn("send sms via primary channel failed, try backup channel.", e);
                try {
                    smsProviderBackup.sendSMS(mobileList, template, paramList);
                    successChannel = SmsChannel.Backup;
                } catch (SmsSendingException e1) {
                    logger.error("send sms via primary and backup channel all failed.", e1);
                    throw e1;
                }
            } else {
                logger.error(String.format("send sms via %s channel failed.", channel), e);
                throw e;
            }
        }

        logger.info(String.format("send sms success, mobileList: %s, template: %s, params: %s, channel: %s",
                String.join(",", mobileList),
                template.name(),
                String.join(",", paramList),
                successChannel));

        return successChannel;
    }

    private void logSmsRecord(Class<? extends BaseMapper> baseMapperClass, List<String> mobileList, SmsTemplate template, List<String> paramList, SmsChannel successChannel) {
        BaseMapper mapper = this.getMapperByClass(baseMapperClass);
        String content = template.generateContent(paramList, successChannel);

        for (String mobile : mobileList) {
            SmsModel model = new SmsModel(mobile, content, "200-" + successChannel);
            mapper.create(model);
        }
    }

    private boolean hasExceedLimit() {
        String redisKey = MessageFormat.format(WYY_SMS_SEND_COUNT_BY_TODAY_TEMPLATE, "SMS");
        String hKey = DateTime.now().withTimeAtStartOfDay().toString("yyyyMMdd");
        String redisValue = redisWrapperClient.hget(redisKey, hKey);

        int smsSendSize = Strings.isNullOrEmpty(redisValue) ? 0 : Integer.parseInt(redisValue);
        boolean exceed = smsSendSize >= sendSize;
        if (!exceed) {
            smsSendSize++;
            redisWrapperClient.hset(redisKey, hKey, String.valueOf(smsSendSize), lifeSecond);
        }
        return exceed;
    }

    private boolean isInCooldown(String ip) {
        String redisKey = MessageFormat.format(SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE, ip);
        return redisWrapperClient.exists(redisKey);
    }

    private void setIntoCooldown(String ip) {
        if (!Strings.isNullOrEmpty(ip)) {
            String redisKey = MessageFormat.format(SMS_IP_RESTRICTED_REDIS_KEY_TEMPLATE, ip);
            redisWrapperClient.setex(redisKey, second, "1");
        }
    }

    private BaseMapper getMapperByClass(Class clazz) {
        String fullName = clazz.getName();
        String[] strings = fullName.split("\\.");

        String beanName = Introspector.decapitalize(strings[strings.length - 1]);

        return (BaseMapper) applicationContext.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SmsClient.applicationContext = applicationContext;
    }
}
