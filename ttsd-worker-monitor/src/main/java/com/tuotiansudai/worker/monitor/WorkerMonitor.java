package com.tuotiansudai.worker.monitor;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.worker.monitor.config.MonitorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WorkerMonitor {
    private static final Logger logger = LoggerFactory.getLogger(WorkerMonitor.class);
    static String HEALTH_REPORT_REDIS_KEY = "worker:health:report";
    private final Set<String> missingWorkers = new HashSet<>();

    private final Timer healthCheckTimer;
    private final SmsWrapperClient smsWrapperClient;
    private final StringRedisTemplate redisTemplate;
    private final MonitorConfig monitorConfig;
    private final JavaMailSender mailSender;

    public static void setHealthReportRedisKey(String healthReportRedisKey) {
        HEALTH_REPORT_REDIS_KEY = healthReportRedisKey;
    }

    @Autowired
    public WorkerMonitor(SmsWrapperClient smsWrapperClient,
                         StringRedisTemplate redisTemplate,
                         MonitorConfig monitorConfig,
                         JavaMailSender mailSender) {
        this.healthCheckTimer = new Timer();
        this.smsWrapperClient = smsWrapperClient;
        this.redisTemplate = redisTemplate;
        this.monitorConfig = monitorConfig;
        this.mailSender = mailSender;
    }

    void start() {
        logger.info("monitor start on [{}]", monitorConfig.getName());
        healthCheckTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        checkAllHealth();
                    }
                },
                monitorConfig.getHealthCheckIntervalSeconds() * 1000,
                monitorConfig.getHealthCheckIntervalSeconds() * 1000);
    }

    void stop() {
        healthCheckTimer.cancel();
        healthCheckTimer.purge();
    }

    private void checkAllHealth() {
        Map<String, String> workerMap;
        try {
            // redis hgetAll
            HashOperations<String, String, String> ops = redisTemplate.opsForHash();
            workerMap = ops.entries(HEALTH_REPORT_REDIS_KEY);
        } catch (Exception e) {
            logger.error("[monitor] query worker status from redis failed", e);
            return;
        }

        long oldestLivingClock = Clock.systemUTC().millis() - monitorConfig.getMaxSilenceSeconds() * 1000;
        Set<String> newMessingWorkers = workerMap.entrySet().stream()
                .filter(entry -> isNewMissingWorker(entry.getKey(), entry.getValue(), oldestLivingClock))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        if (!newMessingWorkers.isEmpty()) {
            notifyError(newMessingWorkers);
        }
    }

    private boolean isNewMissingWorker(String workerName, String lastReportTimeStr, long oldestLivingClock) {
        long lastReportTime = 0;
        try {
            lastReportTime = Long.parseLong(lastReportTimeStr);
        } catch (NumberFormatException e) {
            logger.warn("[monitor] can not parse last report time '{}' of worker {}", lastReportTimeStr, workerName);
        }
        if (lastReportTime > oldestLivingClock) {
            if (missingWorkers.remove(workerName)) {
                logger.info("[monitor] {} come back", workerName);
                if (missingWorkers.isEmpty()) {
                    logger.info("[monitor] all workers back to normal");
                    notifyOK();
                }
            }
            return false;
        } else {
            boolean isNewItem = missingWorkers.add(workerName);
            if (isNewItem) {
                logger.error("[monitor] {} lost", workerName);
            }
            return isNewItem;
        }
    }

    private void notifyOK() {
        String alertMessage = "所有Worker已恢复正常";
        sendNotification(alertMessage);
    }

    private void notifyError(Set<String> missingWorkers) {
        String allMissingWorkers = String.join(",", missingWorkers);
        String alertMessage = String.format("Worker[%s] 掉线", allMissingWorkers);
        sendNotification(alertMessage);
    }

    private void sendNotification(String text) {
        if (monitorConfig.isSmsNotifyEnabled()) {
            logger.info("[monitor] send sms {}", text);
            SmsFatalNotifyDto dto = new SmsFatalNotifyDto(text);
            try {
                smsWrapperClient.sendFatalNotify(dto);
            } catch (Exception e) {
                logger.error("[monitor] send sms {} failed", text, e);
            }
        }
        if (monitorConfig.isEmailNotifyEnabled()) {
            logger.info("[monitor] send email {}", text);
            try {
                sendNotifyEmail(text);
            } catch (Exception e) {
                logger.error("[monitor] send email{} failed", text, e);
            }
        }
    }

    private void sendNotifyEmail(String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(monitorConfig.getEmailNotifySender());
        message.setTo(monitorConfig.getEmailNotifyRecipients());
        message.setSubject(String.format("worker monitor [%s]", monitorConfig.getName()));
        message.setText(text.replace("\n", "\r\n"));
        mailSender.send(message);
    }
}
