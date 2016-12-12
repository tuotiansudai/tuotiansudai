package com.tuotiansudai.worker.monitor;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class WorkerMonitorTest {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @MockBean
    private SmsWrapperClient smsWrapperClient;
    @MockBean
    private JavaMailSender mailSender;

    private final ArgumentCaptor<SmsFatalNotifyDto> smsFatalNotifyDtoCaptor = ArgumentCaptor.forClass(SmsFatalNotifyDto.class);
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
        monitorConfig.setEmailNotifySender("no-reply@tuotiansudai.com");
        monitorConfig.setEmailNotifyRecipients(new String[]{"qa1@tuotiansudai.com", "qa2@tuotiansudai.com"});

        when(smsWrapperClient.sendFatalNotify(smsFatalNotifyDtoCaptor.capture())).thenReturn(null);
        doNothing().when(mailSender).send(mailMessageArgumentCaptor.capture());

        WorkerMonitor.setHealthReportRedisKey("worker:health:report:test");

        this.hashOperations = redisTemplate.opsForHash();
        this.workerMonitor = new WorkerMonitor(smsWrapperClient, redisTemplate, monitorConfig, mailSender);
    }

    @Test
    public void shouldNotify() {
        workerMonitor.start();
        heartBeat("worker1");
        heartBeat("worker2");
        heartBeat("worker3");
        heartBeat("worker4");
        waitSeconds(1);
        heartBeat("worker1");
        heartBeat("worker2");
        waitSeconds(1);
        heartBeat("worker1");
        waitSeconds(1);
        heartBeat("worker1");
        waitSeconds(1);
        heartBeat("worker1");
        waitSeconds(1);
        heartBeat("worker1");
        waitSeconds(1);
        heartBeat("worker1");
        waitSeconds(1);
        heartBeat("worker1");
        waitSeconds(1);
        heartBeat("worker1");
        heartBeat("worker3");
        heartBeat("worker4");
        waitSeconds(1);
        heartBeat("worker1");
        heartBeat("worker2");
        heartBeat("worker3");
        heartBeat("worker4");
        waitSeconds(1);
        heartBeat("worker1");
        heartBeat("worker2");
        heartBeat("worker3");
        heartBeat("worker4");
        waitSeconds(1);
        workerMonitor.stop();
        List<SmsFatalNotifyDto> smsMessages = smsFatalNotifyDtoCaptor.getAllValues();
        assertLost(smsMessages.get(0).getErrorMessage(), "worker3,worker4");
        assertLost(smsMessages.get(1).getErrorMessage(), "worker2");
        assertOK(smsMessages.get(2).getErrorMessage());
        List<SimpleMailMessage> mailMessages = mailMessageArgumentCaptor.getAllValues();
        assertLost(mailMessages.get(0).getText(), "worker3,worker4");
        assertLost(mailMessages.get(1).getText(), "worker2");
        assertOK(mailMessages.get(2).getText());
    }

    private void assertLost(String message, String workerName) {
        assertEquals(String.format("Worker[%s] 掉线", workerName), message);
    }

    private void assertOK(String message) {
        assertEquals("所有Worker已恢复正常", message);
    }

    private void heartBeat(String workerName) {
        hashOperations.put(WorkerMonitor.HEALTH_REPORT_REDIS_KEY, workerName, String.valueOf(Clock.systemUTC().millis()));
    }

    private void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
