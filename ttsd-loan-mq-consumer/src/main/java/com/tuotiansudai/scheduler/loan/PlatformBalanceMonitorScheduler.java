package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.JianZhouSmsTemplate;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.service.UMPayRealTimeStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PlatformBalanceMonitorScheduler {

    static Logger logger = LoggerFactory.getLogger(PlatformBalanceMonitorScheduler.class);

    @Autowired
    private UMPayRealTimeStatusService umPayRealTimeStatusService;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("#{'${platform.balance.notify.mobileList}'.split('\\|')}")
    private List<String> mobileList;

    @Value("${platform.balance.notify.warning.line}")
    private String warningLine; // 5000

    @Scheduled(cron = "0 0 9 * * ?", zone = "Asia/Shanghai")
    public void execute() {

        logger.info("[Platform Balance Monitor] Job is starting...");

        try {
            Map<String, String> data = umPayRealTimeStatusService.getPlatformStatus();

            String balance = data.get("账户余额");

            logger.info("platform balance is: " + balance);

            mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_PLATFORM_BALANCE_LOW_NOTIFY_TEMPLATE, mobileList, Lists.newArrayList(warningLine)));
//
//            if (Double.parseDouble(balance) <= Double.parseDouble(warningLine)) {
//                mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_PLATFORM_BALANCE_LOW_NOTIFY_TEMPLATE, mobileList, Lists.newArrayList(warningLine)));
//            }
            logger.info("[Platform Balance Monitor] job is done");
        } catch (Exception e) {
            logger.error("[Platform Balance Monitor] job execution is failed.", e);
        }
    }
}
