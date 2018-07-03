package com.tuotiansudai.scheduler.loan;

import com.tuotiansudai.mq.consumer.loan.QueryDownloadFilesMessageConsumer;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class QueryDownloadFilesScheduler {

    @Autowired
    private QueryDownloadFilesMessageConsumer consumer;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String EMAIL_QUERY_IS_SEND = "EMAIL_QUERY_IS_SEND:{0}";

    @Scheduled(cron = "0 8 * * * ?", zone = "Asia/Shanghai")
    public void scheduler() {
        if (redisWrapperClient.exists(MessageFormat.format(EMAIL_QUERY_IS_SEND, DateTime.now().minusDays(1).toString("yyyyMMdd")))){
            return;
        }
        consumer.sendEmailMessage();
    }
}
