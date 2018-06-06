package com.tuotiansudai.worker.monitor;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.worker.monitor.config.MonitorConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class WorkerMonitorTest {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @MockBean
    private MQWrapperClient mqWrapperClient;
    @MockBean
    private JavaMailSender mailSender;

    private final ArgumentCaptor<SimpleMailMessage> mailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

    private HashOperations<String, String, String> hashOperations;
    private WorkerMonitor workerMonitor;

    @Before
    public void setup() {
        MonitorConfig monitorConfig = new MonitorConfig();
        monitorConfig.setName("QA");
        monitorConfig.setHealthCheckIntervalSeconds(1);
        monitorConfig.setMaxSilenceSeconds(5);
        monitorConfig.setEmailNotifyEnabled(true);
        monitorConfig.setSmsNotifyEnabled(true);

        doNothing().when(mailSender).send(mailMessageArgumentCaptor.capture());

        WorkerMonitor.setHealthReportRedisKey("worker:health:report:test");

        this.hashOperations = redisTemplate.opsForHash();
        this.workerMonitor = new WorkerMonitor(redisTemplate, monitorConfig, mailSender, mqWrapperClient);
    }

    @Test
    public void shouldNotify() {
        workerMonitor.start();
        heartBeat("worker1", "worker2", "worker3", "worker4");
        heartBeat("worker1", "worker2");
        heartBeat("worker1");
        heartBeat("worker1");
        heartBeat("worker1");
        heartBeat("worker1");
        heartBeat("worker1");
        heartBeat("worker1");
        heartBeat("worker1", "worker3", "worker4");
        heartBeat("worker1", "worker2", "worker3", "worker4");
        heartBeat("worker1", "worker2", "worker3", "worker4", "worker5");
        heartBeat("worker1");
        heartBeat("worker1");
        heartBeat("worker1");
        heartBeat("worker1");
        heartBeat("worker1");
        heartBeat("worker1");
        workerMonitor.stop();
        List<SmsNotifyDto> smsMessages = smsFatalNotifyDtoCaptor.getAllValues();
        assertLost(smsMessages.get(0).getErrorMessage(), "worker3, worker4");
        assertLost(smsMessages.get(1).getErrorMessage(), "worker2");
        assertOK(smsMessages.get(2).getErrorMessage());
        assertLost(smsMessages.get(3).getErrorMessage(), "worker2, worker3, worker4 and other 1 workers");
        List<SimpleMailMessage> mailMessages = mailMessageArgumentCaptor.getAllValues();
        assertLost(mailMessages.get(0).getText(), "worker3, worker4");
        assertLost(mailMessages.get(1).getText(), "worker2");
        assertOK(mailMessages.get(2).getText());
        assertLost(mailMessages.get(3).getText(), "worker2, worker3, worker4, worker5");
    }

    private void assertLost(String message, String workerName) {
        assertEquals(String.format("Worker[%s] 掉线", workerName), message);
    }

    private void assertOK(String message) {
        assertEquals("所有Worker已恢复正常", message);
    }

    private void heartBeat(String... workerNames) {
        String nowTime = String.valueOf(Clock.systemUTC().millis());
        for (String workerName : workerNames) {
            hashOperations.put(WorkerMonitor.HEALTH_REPORT_REDIS_KEY, workerName, nowTime);
        }
        waitSeconds(1);
    }

    private void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
