package com.tuotiansudai.worker.monitor;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class WorkerMonitor {
    private static final Logger logger = LoggerFactory.getLogger(WorkerMonitor.class);
    private final SmsWrapperClient smsWrapperClient;
    private final StringRedisTemplate redisTemplate;
    private final Set<JobWorker> missingWorkers;

    @Autowired
    public WorkerMonitor(SmsWrapperClient smsWrapperClient, StringRedisTemplate redisTemplate) {
        this.smsWrapperClient = smsWrapperClient;
        this.redisTemplate = redisTemplate;
        this.missingWorkers = new HashSet<>();
    }

    public void start() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        checkAllHealth();
                    }
                },
                HealthReporter.REPORT_INTERVAL_SECONDS * 2000,
                HealthReporter.REPORT_INTERVAL_SECONDS * 1000);

    }

    private void checkAllHealth() {
        Set<String> newMessingWorkers = Stream.of(JobWorker.values())
                .filter(this::isNewMissingWorker)
                .map(Enum::name)
                .collect(Collectors.toSet());
        if (!newMessingWorkers.isEmpty()) {
            smsAlarm(newMessingWorkers);
        }
    }

    private boolean isNewMissingWorker(JobWorker jobWorker) {
        if (isWorkerAlive(jobWorker)) {
            if (missingWorkers.remove(jobWorker)) {
                logger.info("{} turn back", jobWorker);
            }
            return false;
        } else {
            boolean isNewItem = missingWorkers.add(jobWorker);
            if (isNewItem) {
                logger.info("{} lost", jobWorker);
            }
            return isNewItem;
        }
    }

    private boolean isWorkerAlive(JobWorker jobWorker) {
        String redisKey = String.format(HealthReporter.WORKER_HEALTH_WORKER_STATUS_REDIS_KEY_TEMPLATE, jobWorker);
        return redisTemplate.hasKey(redisKey);
    }

    private void smsAlarm(Set<String> missingWorkers) {
        String allMissingWorkers = String.join(",", missingWorkers);
        String alarmMessage = String.format("Worker异常退出：%s", allMissingWorkers);
        logger.error(alarmMessage);
        SmsFatalNotifyDto dto = new SmsFatalNotifyDto(alarmMessage);
        smsWrapperClient.sendFatalNotify(dto);
    }
}
