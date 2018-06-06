package com.tuotiansudai.mq.consumer.sms;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.enums.JianZhouSmsTemplate;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.sms.client.JianZhouSmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SmsFatalNotifyMessageConsumer implements MessageConsumer{

    private static Logger logger = LoggerFactory.getLogger(SmsFatalNotifyMessageConsumer.class);

    private String fatalNotifyDevMobiles = ETCDConfigReader.getReader().getValue("sms.fatal.dev.mobile");

    private String fatalNotifyQAMobiles = ETCDConfigReader.getReader().getValue("sms.fatal.qa.mobile");

    private Environment environment = Environment.valueOf(ETCDConfigReader.getReader().getValue("common.environment"));

    @Autowired
    private JianZhouSmsClient jianZhouSmsClient = JianZhouSmsClient.getClient();

    @Override
    public MessageQueue queue() {
        return MessageQueue.SmsFatalNotify;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)){
            logger.error("[MQ] parse message failed: {}: '{}'.", this.queue(), message);
        }

        try {
            List<String> mobiles = Lists.newArrayList(Arrays.asList(fatalNotifyQAMobiles.split("|")));
            if (Environment.PRODUCTION == environment) {
                mobiles.addAll(Arrays.asList(fatalNotifyDevMobiles.split("|")));
            }
            jianZhouSmsClient.sendSms(mobiles, JianZhouSmsTemplate.SMS_FATAL_NOTIFY_TEMPLATE, false, Lists.newArrayList(message), null);

        }catch (Exception e){
            logger.error("[MQ] 程序内部异常: {}: {}.{}", this.queue(), message, e.getMessage());
        }
    }
}
