package com.tuotiansudai.service;


import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.TimeUnit;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.common.resp.ResponseWrapper;
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
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.jpush.service.impl.JPushScheduleAlertServiceImpl;
import com.tuotiansudai.repository.model.Environment;
import com.tuotiansudai.util.UUIDGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class JPushScheduleAlertServiceTest {

    @InjectMocks
    private JPushScheduleAlertServiceImpl jPushScheduleAlertService ;
    @Mock
    private ScheduleClient scheduleClient = null;
    @Mock
    private RedisWrapperClient redisClient = null;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSendJPushScheduleAlertIsOk() throws APIConnectionException, APIRequestException {
        ReflectionTestUtils.setField(jPushScheduleAlertService, "scheduleClient" ,scheduleClient);
        String scheduleId = "testSchedule";
        PushPayload pushPayload = createCommonPushPayLoad();
        Calendar now= Calendar.getInstance();
        now.add(Calendar.MINUTE,10);
        TriggerPayload triggerPayload = TriggerPayload.newBuilder().setSingleTime(sdf.format(now.getTimeInMillis())).buildSingle();
        ScheduleResult scheduleResult = new ScheduleResult();
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.responseCode = 1;
        responseWrapper.responseContent = "ok";
        scheduleResult.setResponseWrapper(responseWrapper);
        when(scheduleClient.createSchedule((SchedulePayload) anyObject())).thenReturn(scheduleResult);
        when(scheduleClient.getSchedule(anyString())).thenReturn(scheduleResult);
        when(scheduleClient.updateSchedule(anyString(), (SchedulePayload) anyObject())).thenReturn(scheduleResult);
        when(redisClient.sadd(anyString(),anyString())).thenReturn(0l);

        scheduleResult = jPushScheduleAlertService.sendJPushScheduleAlert(scheduleId,pushPayload,triggerPayload);
        scheduleResult = jPushScheduleAlertService.findPushScheduleAlert(scheduleResult.getSchedule_id());
        jPushScheduleAlertService.updatePushScheduleAlert(scheduleResult.getSchedule_id(),SchedulePayload.newBuilder().setName("修改测试类").build());
        assertNotNull(scheduleResult);
        ArgumentCaptor findId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentCaptorSchedulePayload = ArgumentCaptor.forClass(SchedulePayload.class);
        verify(scheduleClient).createSchedule((SchedulePayload) argumentCaptorSchedulePayload.capture());
        String schedulePayloadString = argumentCaptorSchedulePayload.getAllValues().get(0).toString();

        verify(scheduleClient).getSchedule((String)findId.capture());
        verify(scheduleClient).updateSchedule((String)findId.capture(),(SchedulePayload) argumentCaptorSchedulePayload.capture());
    }

    private PushPayload createCommonPushPayLoad(){
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.newBuilder().addAudienceTarget(AudienceTarget.tag("test")).build())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert("手动推送测试").addExtra("jumpTo", 3).setBadge(0).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert("手动推送测试").addExtra("jumpTo", 3).setBuilderId(2).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(Environment.DEV)).build())
                .build();
        return pushPayload;
    }

}
