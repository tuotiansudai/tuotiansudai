package com.tuotiansudai.jpush.client;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.tuotiansudai.repository.model.Environment;
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

    @Value("${common.environment}")
    private Environment environment;


    private static JPushClient jPushClient;

    public JPushClient getJPushClient() {
        if (jPushClient == null) {
            jPushClient = new JPushClient(masterSecret, appKey, 3);
        }
        return jPushClient;
    }

    public PushPayload buildPushObject_all_registration_id_alertWithExtras(List<String> registrationIds, String alert, String extraKey, String extraValue) {

        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationIds))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).setBadge(0).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();
    }

    public PushPayload buildPushObject_all_all_alertWithExtras(String alert, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();

    }

    public PushPayload buildPushObject_android_all_alertWithExtras(String alert, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();

    }

    public PushPayload buildPushObject_android_tags_alertWithExtras(List<String> tags, String alert, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag(tags))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();

    }

    public PushPayload buildPushObject_ios_tags_alertWithExtras(List<String> tags, String alert, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag(tags))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();

    }

    public PushPayload buildPushObject_ios_all_alertWithExtras(String alert, String extraKey, String extraValue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();

    }

    public PushPayload buildPushObject_all_tags_alert(List<String> tags, String alert, String extraKey, String extraValue) {

        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(tags))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();
    }

    public boolean sendPushAlertByTags(String jPushAlertId, List<String> tags, String alert, String extraKey, String extraValue) {
        PushPayload payload = buildPushObject_all_tags_alert(tags, alert, extraKey, extraValue);

        return sendPush(payload, jPushAlertId);
    }

    public boolean sendPushAlertByAndroidTags(String jPushAlertId, List<String> tags, String alert, String extraKey, String extraValue) {
        PushPayload payload = buildPushObject_android_tags_alertWithExtras(tags, alert, extraKey, extraValue);

        return sendPush(payload, jPushAlertId);
    }

    public boolean sendPushAlertByIosTags(String jPushAlertId, List<String> tags, String alert, String extraKey, String extraValue) {
        PushPayload payload = buildPushObject_ios_tags_alertWithExtras(tags, alert, extraKey, extraValue);

        return sendPush(payload, jPushAlertId);
    }

    public boolean sendPushAlertByAll(String jPushAlertId, String alert, String extraKey, String extraValue) {
        PushPayload payload = buildPushObject_all_all_alertWithExtras(alert, extraKey, extraValue);
        return sendPush(payload, jPushAlertId);
    }
    public boolean sendPushAlertByRegistrationIds(String jPushAlertId,List<String> registrationIds, String alert, String extraKey, String extraValue) {
        PushPayload payload = buildPushObject_all_registration_id_alertWithExtras(registrationIds,alert, extraKey, extraValue);
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

    public static JPushClient getjPushClient() {
        return jPushClient;
    }

    public static void setjPushClient(JPushClient jPushClient) {
        MobileAppJPushClient.jPushClient = jPushClient;
    }
}
