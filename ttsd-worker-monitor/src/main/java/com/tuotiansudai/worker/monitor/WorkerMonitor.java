package com.tuotiansudai.worker.monitor;

import com.tuotiansudai.client.MQWrapperClient;
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
    private final StringRedisTemplate redisTemplate;
    private final MonitorConfig monitorConfig;
    private final JavaMailSender mailSender;
    private final MQWrapperClient mqWrapperClient;

    public static void setHealthReportRedisKey(String healthReportRedisKey) {
        HEALTH_REPORT_REDIS_KEY = healthReportRedisKey;
    }

    @Autowired
    public WorkerMonitor(StringRedisTemplate redisTemplate,
                         MonitorConfig monitorConfig,
                         JavaMailSender mailSender,
                         MQWrapperClient mqWrapperClient) {
        this.healthCheckTimer = new Timer();
        this.redisTemplate = redisTemplate;
        this.monitorConfig = monitorConfig;
        this.mailSender = mailSender;
        this.mqWrapperClient = mqWrapperClient;
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

        // 查找所有掉线的worker
        long oldestLivingClock = Clock.systemUTC().millis() - monitorConfig.getMaxSilenceSeconds() * 1000;
        Set<String> missingWorkersNow = workerMap.entrySet().stream()
                .filter(entry -> parseClock(entry.getValue()) < oldestLivingClock)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        // 没有掉线的worker则报告所有worker已恢复
        if (missingWorkersNow.isEmpty()) {
            if (!missingWorkers.isEmpty()) {
                missingWorkers.clear();
                logger.info("[monitor] all workers back to normal");
                notifyOK();
            }
        // 有掉线的worker，则报告哪些worker是刚刚掉线的
        } else {
            Set<String> newMissingWorkers = new HashSet<>(missingWorkersNow);
            newMissingWorkers.removeAll(missingWorkers);
            missingWorkers.clear();
            missingWorkers.addAll(missingWorkersNow);
            if (!newMissingWorkers.isEmpty()) {
                notifyError(newMissingWorkers);
            }
        }
    }

    private long parseClock(String millisecondStr) {
        try {
            return Long.parseLong(millisecondStr);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void notifyOK() {
        String alertMessage = "所有Worker已恢复正常";
        sendNotification(alertMessage, alertMessage);
    }

    private void notifyError(Set<String> missingWorkers) {
        String allMissingWorkers = String.join(", ", missingWorkers);
        logger.error("[monitor] {} lost", allMissingWorkers);

        String smsNotifyMissingWorkers;
        if (missingWorkers.size() <= 3) {
            smsNotifyMissingWorkers = allMissingWorkers;
        } else {
            Object[] workerArray = missingWorkers.toArray();
            smsNotifyMissingWorkers = String.format("%s, %s, %s and other %d workers",
                    workerArray[0], workerArray[1], workerArray[2], missingWorkers.size() - 3);
        }
        String smsText = String.format("Worker[%s] 掉线", smsNotifyMissingWorkers);
        String emailText = String.format("Worker[%s] 掉线", allMissingWorkers);
        sendNotification(smsText, emailText);
    }

    private void sendNotification(String smsText, String emailText) {
        if (monitorConfig.isSmsNotifyEnabled()) {
            logger.info("[monitor] send sms {}", smsText);
//            SmsFatalNotifyDto dto = new SmsFatalNotifyDto(smsText);
            try {
//                smsWrapperClient.sendFatalNotify(dto);
                mqWrapperClient.sendMessage(MessageQueue);
            } catch (Exception e) {
                logger.error("[monitor] send sms {} failed", smsText, e);
            }
        }
        if (monitorConfig.isEmailNotifyEnabled()) {
            logger.info("[monitor] send email {}", emailText);
            try {
                sendNotifyEmail(emailText);
            } catch (Exception e) {
                logger.error("[monitor] send email{} failed", emailText, e);
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
