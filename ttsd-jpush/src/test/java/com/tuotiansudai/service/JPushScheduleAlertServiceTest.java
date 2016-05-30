package com.tuotiansudai.service;


import cn.jpush.api.common.TimeUnit;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;
import cn.jpush.api.schedule.model.SchedulePayload;
import cn.jpush.api.schedule.model.TriggerPayload;
import com.tuotiansudai.jpush.service.JPushScheduleAlertService;
import com.tuotiansudai.repository.model.Environment;
import com.tuotiansudai.util.UUIDGenerator;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class JPushScheduleAlertServiceTest {

    @Autowired
    private JPushScheduleAlertService jPushScheduleAlertService ;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    @Ignore
    public void shouldSendJPushScheduleAlertIsOk(){
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

        ScheduleResult scheduleResult = jPushScheduleAlertService.sendJPushScheduleAlert("测试周期",pushPayload,triggerPayload);
        assertNotNull(scheduleResult);

        scheduleResult = jPushScheduleAlertService.findPushScheduleAlert(scheduleResult.getSchedule_id());

        jPushScheduleAlertService.updatePushScheduleAlert(scheduleResult.getSchedule_id(),SchedulePayload.newBuilder().setName("修改测试类").build());
        jPushScheduleAlertService.deletePushScheduleAlert(scheduleResult.getSchedule_id());
    }

    @Test
    @Ignore
    public void shouldSendJPushPeriodicalScheduleAlertIsOk(){
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
        String startTime = sdf.format(now.getTimeInMillis());
        now.add(Calendar.HOUR_OF_DAY,24);
        String endTime = sdf.format(now.getTimeInMillis());

        TriggerPayload triggerPayload = TriggerPayload.newBuilder().setPeriodTime(startTime,endTime,"10:11:00")
                .setTimeFrequency(TimeUnit.DAY,1,null)
                .buildPeriodical();

        ScheduleResult scheduleResult = jPushScheduleAlertService.sendJPushScheduleAlert(UUIDGenerator.generate(),pushPayload,triggerPayload);
        assertNotNull(scheduleResult);
        jPushScheduleAlertService.deletePushScheduleAlert(scheduleResult.getSchedule_id());
    }

}
