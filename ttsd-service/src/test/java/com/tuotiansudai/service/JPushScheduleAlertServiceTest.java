package com.tuotiansudai.service;

import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleClient;
import cn.jpush.api.schedule.ScheduleResult;
import cn.jpush.api.schedule.model.SchedulePayload;
import cn.jpush.api.schedule.model.TriggerPayload;
import com.tuotiansudai.jpush.service.impl.JPushScheduleAlertServiceImpl;
import com.tuotiansudai.repository.model.Environment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class JPushScheduleAlertServiceTest {

    @InjectMocks
    private JPushScheduleAlertServiceImpl jPushScheduleAlertService;
    @Mock
    private ScheduleClient scheduleClient;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGetDefaultInterestWhenHasDefaultInterestIsOk() throws APIConnectionException, APIRequestException {
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.newBuilder().addAudienceTarget(AudienceTarget.tag("test")).build())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert("手动推送测试").addExtra("jumpTo", 3).setBadge(0).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert("手动推送测试").addExtra("jumpTo", 3).setBuilderId(2).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(Environment.DEV)).build())
                .build();

        Calendar now= Calendar.getInstance();
        now.add(Calendar.MINUTE,10);
        TriggerPayload triggerPayload = TriggerPayload.newBuilder().setSingleTime(sdf.format(now.getTimeInMillis())).buildSingle();
        SchedulePayload.Builder schedulePayload = new SchedulePayload.Builder().setName("测试周期")
                .setEnabled(true)
                .setTrigger(triggerPayload).setPush(pushPayload);
        ScheduleResult scheduleResult = new ScheduleResult();
        when(scheduleClient.createSchedule(schedulePayload.build())).thenReturn(scheduleResult);
        assertNotNull(scheduleResult);
        when(scheduleClient.getSchedule(anyString())).thenReturn(scheduleResult);
        assertNotNull(scheduleResult);
    }
}
