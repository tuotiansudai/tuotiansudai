package com.tuotiansudai.worker.monitor;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WorkerMonitor {
    private static final Logger logger = LoggerFactory.getLogger(WorkerMonitor.class);
    private static final int HEALTH_CHECK_INTERVAL_SECONDS = 10; // 每10秒检查一次
    private static final int WORKER_GONE_IF_AFTER_SECONDS = 120; // 2分钟没有更新，认为掉线
    private static final String HEALTH_REPORT_REDIS_KEY = "worker:health:report";
    private final SmsWrapperClient smsWrapperClient;
    private final StringRedisTemplate redisTemplate;
    private final Set<String> missingWorkers;

    @Autowired
    public WorkerMonitor(SmsWrapperClient smsWrapperClient, StringRedisTemplate redisTemplate) {
        this.smsWrapperClient = smsWrapperClient;
        this.redisTemplate = redisTemplate;
        this.missingWorkers = new HashSet<>();
    }

    void start() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        checkAllHealth();
                    }
                },
                HEALTH_CHECK_INTERVAL_SECONDS * 1000,
                HEALTH_CHECK_INTERVAL_SECONDS * 1000);
    }

    private void checkAllHealth() {
        // redis hgetAll
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        Map<String, String> workerMap = ops.entries(HEALTH_REPORT_REDIS_KEY);

        long oldestLivingClock = Clock.systemUTC().millis() - WORKER_GONE_IF_AFTER_SECONDS * 1000;
        Set<String> newMessingWorkers = workerMap.entrySet().stream()
                .filter(entry -> isNewMissingWorker(entry.getKey(), entry.getValue(), oldestLivingClock))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        if (!newMessingWorkers.isEmpty()) {
            smsAlarm(newMessingWorkers);
        }
    }

    private boolean isNewMissingWorker(String workerName, String lastReportTimeStr, long oldestLivingClock) {
        long lastReportTime = 0;
        try {
            lastReportTime = Long.parseLong(lastReportTimeStr);
        } catch (NumberFormatException e) {
            logger.warn("can not parse last report time '{}' of worker {}", lastReportTimeStr, workerName);
        }
        if (lastReportTime > oldestLivingClock) {
            if (missingWorkers.remove(workerName)) {
                logger.info("{} come back", workerName);
                if (missingWorkers.isEmpty()) {
                    logger.info("all workers back to normal");
                    smsOK();
                }
            }
            return false;
        } else {
            boolean isNewItem = missingWorkers.add(workerName);
            if (isNewItem) {
                logger.info("{} lost", workerName);
            }
            return isNewItem;
        }
    }

    private void smsOK() {
        String alertMessage = "所有Worker已恢复正常";
        SmsFatalNotifyDto dto = new SmsFatalNotifyDto(alertMessage);
        logger.info(alertMessage);
        smsWrapperClient.sendFatalNotify(dto);
    }

    private void smsAlarm(Set<String> missingWorkers) {
        String allMissingWorkers = String.join(",", missingWorkers);
        String alertMessage = String.format("Worker[%s] 掉线", allMissingWorkers);
        logger.error(alertMessage);
        SmsFatalNotifyDto dto = new SmsFatalNotifyDto(alertMessage);
        smsWrapperClient.sendFatalNotify(dto);
    }
}
