package com.tuotiansudai.jpush.service;

import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.schedule.ScheduleResult;
import cn.jpush.api.schedule.model.TriggerPayload;

public interface JPushScheduleAlertService {

    ScheduleResult sendJPushScheduleAlert(String jPushAlertId,PushPayload payload,TriggerPayload triggerPayload);

    void deletePushScheduleAlert(String jPushAlertId);

    ScheduleResult findPushScheduleAlert(String jPushAlertId);
}
