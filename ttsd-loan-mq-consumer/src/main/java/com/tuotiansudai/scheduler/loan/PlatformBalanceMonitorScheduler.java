package com.tuotiansudai.scheduler.loan;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.PlatformBalanceLowNotifyDto;
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
    private SmsWrapperClient smsWrapperClient;

    @Value("#{'${platform.balance.notify.mobileList}'.split('\\|')}")
    private List<String> mobileList;

    @Value("${platform.balance.notify.warning.line}")
    private String warningLine; // 5000

//    @Scheduled(cron = "0 0 9 * * ?", zone = "Asia/Shanghai")
    @Scheduled(initialDelay = 1000 * 30, fixedDelay = 1000 * 60 * 10)
    public void execute() {

        logger.info("[Platform Balance Monitor] Job is starting...");

        try {
            Map<String, String> data = umPayRealTimeStatusService.getPlatformStatus();

            String balance = data.get("账户余额");

            logger.info("platform balance is: " + balance);

            if (Double.parseDouble(balance) <= Double.parseDouble(warningLine)) {

                PlatformBalanceLowNotifyDto notifyDto = new PlatformBalanceLowNotifyDto();
                notifyDto.setMobiles(mobileList);
                notifyDto.setWarningLine(warningLine);

                smsWrapperClient.sendPlatformBalanceLowNotify(notifyDto);
            }
            logger.info("[Platform Balance Monitor] job is done");
        } catch (Exception e) {
            logger.error("[Platform Balance Monitor] job execution is failed.", e);
        }
    }
}
