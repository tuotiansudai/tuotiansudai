package com.tuotiansudai.job;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.PlatformBalanceLowNotifyDto;
import com.tuotiansudai.service.UMPayRealTimeStatusService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PlatformBalanceMonitorJob implements Job {

    static Logger logger = Logger.getLogger(PlatformBalanceMonitorJob.class);

    @Autowired
    private UMPayRealTimeStatusService umPayRealTimeStatusService;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Value("#{'${platform.balance.notify.mobileList}'.split('\\|')}")
    private List<String> mobileList;

    @Value("${platform.balance.notify.warning.line}")
    private String warningLine; // 5000

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

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

        } catch (Exception e) {
            logger.error("[Platform Balance Monitor] job execution is failed.", e);
            return;
        }

        logger.info("[Platform Balance Monitor] job is done");
    }
}
