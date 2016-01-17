package com.tuotiansudai.console.jpush.client;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class MobileAppJPushClient {
    static Logger logger = Logger.getLogger(MobileAppJPushClient.class);
    @Value("${common.jpush.masterSecret}")
    private String masterSecret;

    @Value("${common.jpush.appKey}")
    private String appKey;

    private static JPushClient jPushClient;

    public JPushClient getJPushClient() {
        if (jPushClient == null) {
            jPushClient = new JPushClient(masterSecret, appKey, 3);
        }
        return jPushClient;
    }

    public static PushPayload buildPushObject_all_all_alert(String alert) {
        return PushPayload.alertAll(alert);

    }

    public static PushPayload buildPushObject_all_all_alertWithExtras(String alert, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .build();

    }

    public static PushPayload buildPushObject_android_all_alertWithExtras(String alert, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .build();

    }

    public static PushPayload buildPushObject_android_aliases_alertWithExtras(List<String> alias, String alert, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .build();

    }
    public static PushPayload buildPushObject_ios_aliases_alertWithExtras(List<String> alias, String alert, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .build();

    }

    public static PushPayload buildPushObject_ios_all_alertWithExtras(String alert, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .build();

    }

    public static PushPayload buildPushObject_all_aliases_alert(List<String> alias, String alert, String extraKey, String extraValue) {

        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .build();
    }

    public boolean sendPushAlertByAliases(String jPushAlertId, List<String> alias, String alert, String extraKey, String extraValue) {
        PushPayload payload = buildPushObject_all_aliases_alert(alias, alert, extraKey, extraValue);

        return sendPush(payload, jPushAlertId);
    }
    public boolean sendPushAlertByAndroidAliases(String jPushAlertId, List<String> alias, String alert, String extraKey, String extraValue) {
        PushPayload payload = buildPushObject_android_aliases_alertWithExtras(alias, alert, extraKey, extraValue);

        return sendPush(payload, jPushAlertId);
    }
    public boolean sendPushAlertByIosAliases(String jPushAlertId, List<String> alias, String alert, String extraKey, String extraValue) {
        PushPayload payload = buildPushObject_ios_aliases_alertWithExtras(alias, alert, extraKey, extraValue);

        return sendPush(payload, jPushAlertId);
    }

    public boolean sendPushAlertByAll(String jPushAlertId, String alert, String extraKey, String extraValue) {
        PushPayload payload = null;
        if (StringUtils.isNotEmpty(extraKey) && StringUtils.isNotEmpty(extraValue)) {
            payload = buildPushObject_all_all_alertWithExtras(alert, extraKey, extraValue);
        } else {
            payload = buildPushObject_all_all_alert(alert);

        }
        return sendPush(payload, jPushAlertId);
    }

    public boolean sendPushAlertByAndroid(String jPushAlertId, String alert, String extraKey, String extraValue) {
        PushPayload payload = buildPushObject_android_all_alertWithExtras(alert, extraKey, extraValue);
        return this.sendPush(payload, jPushAlertId);
    }

    public boolean sendPushAlertByIos(String jPushAlertId, String alert, String extraKey, String extraValue) {
        PushPayload payload = buildPushObject_ios_all_alertWithExtras(alert, extraKey, extraValue);
        return this.sendPush(payload, jPushAlertId);
    }

    private boolean sendPush(PushPayload payload, String jPushAlertId) {
        this.setjPushClient(getJPushClient());
        try {
            System.out.println(payload.toJSON());
            logger.debug(MessageFormat.format("request:{0}:{1} begin", jPushAlertId, payload.toJSON()));
            PushResult result = jPushClient.sendPush(payload);
            logger.debug(MessageFormat.format("request:{0}:{1}:{2} end", jPushAlertId, result.msg_id, result.sendno));
            return true;
        } catch (APIConnectionException e) {
            logger.debug(MessageFormat.format("response:{0}:{1}", jPushAlertId, e.getMessage()));

        } catch (APIRequestException e) {
            logger.debug(MessageFormat.format("response:{0}:{1}", jPushAlertId, e.getMessage()));
        }
        return false;

    }


    public static PushPayload buildPushObject_all_alias_alert(String alias, String alert) {

        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(alert))
                .build();
    }

    public static JPushClient getjPushClient() {
        return jPushClient;
    }

    public static void setjPushClient(JPushClient jPushClient) {
        MobileAppJPushClient.jPushClient = jPushClient;
    }
}
