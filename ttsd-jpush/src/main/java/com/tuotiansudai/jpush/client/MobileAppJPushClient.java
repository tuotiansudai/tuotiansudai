package com.tuotiansudai.jpush.client;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.report.ReceivedsResult;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.jpush.repository.model.PushSource;
import com.tuotiansudai.repository.model.Environment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RedisWrapperClient redisClient;

    public static final String APP_PUSH_MSG_ID_KEY = "console:push-msg-ids:";

    private static JPushClient jPushClient;

    public JPushClient getJPushClient() {
        if (jPushClient == null) {
            jPushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
        }
        return jPushClient;
    }

    public PushPayload buildPushObject_all_registration_id_alertWithExtras(List<String> registrationIds, String alert, String extraKey, String extraValue, PushSource pushSource) {

        return PushPayload.newBuilder()
                .setPlatform(getPlatform(pushSource))
                .setAudience(getAudience(registrationIds))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).setBadge(0).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).setBuilderId(2).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();
    }

    private Audience getAudience(List<String> registrationIds){

        if(Environment.isProduction(environment))
        {
            return Audience.newBuilder()
                    .addAudienceTarget(AudienceTarget.registrationId(registrationIds))
                    .build();
        }
        else{
           return Audience.newBuilder()
                    .addAudienceTarget(AudienceTarget.tag("test"))
                    .addAudienceTarget(AudienceTarget.registrationId(registrationIds))
                    .build();
        }
    }

    private Platform getPlatform(PushSource pushSource) {
        Platform platform = null;
        switch (pushSource) {
            case ALL:
                platform = Platform.all();
                break;
            case IOS:
                platform = Platform.ios();
                break;
            case ANDROID:
                platform = Platform.android();
                break;
        }
        return platform;
    }

    public PushPayload buildPushObject_all_all_alertWithExtras(String alert, String extraKey, String extraValue, PushSource pushSource) {
        return PushPayload.newBuilder()
                .setPlatform(getPlatform(pushSource))
                .setAudience(getAudienceAll())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).setBadge(0).addExtra(extraKey, extraValue).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtra(extraKey, extraValue).setBuilderId(2).build())

                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();

    }

    private Audience getAudienceAll(){
        if(Environment.isProduction(environment))
        {
            return Audience.newBuilder().setAll(true).build();
        }
        else{
            return Audience.newBuilder().addAudienceTarget(AudienceTarget.tag("test")).build();
        }
    }

    public boolean sendPushAlertByAll(String jPushAlertId, String alert, String extraKey, String extraValue, PushSource pushSource) {
        PushPayload payload = buildPushObject_all_all_alertWithExtras(alert, extraKey, extraValue, pushSource);
        return sendPush(payload, jPushAlertId);
    }

    public boolean sendPushAlertByRegistrationIds(String jPushAlertId, List<String> registrationIds, String alert, String extraKey, String extraValue, PushSource pushSource) {
        PushPayload payload = buildPushObject_all_registration_id_alertWithExtras(registrationIds, alert, extraKey, extraValue, pushSource);
        return sendPush(payload, jPushAlertId);
    }

    private boolean sendPush(PushPayload payload, String jPushAlertId) {
        setjPushClient(getJPushClient());
        try {
            System.out.println(payload.toJSON());
            logger.debug(MessageFormat.format("request:{0}:{1} begin", jPushAlertId, payload.toJSON()));
            PushResult result = jPushClient.sendPush(payload);
            logger.debug(MessageFormat.format("request:{0}:{1}:{2} end", jPushAlertId, result.msg_id, result.sendno));
            redisClient.sadd(APP_PUSH_MSG_ID_KEY + jPushAlertId, String.valueOf(result.msg_id));
            return true;
        } catch (APIConnectionException | APIRequestException e) {
            logger.debug(MessageFormat.format("response:{0}:{1}", jPushAlertId, e.getMessage()));
        }
        return false;

    }

    public ReceivedsResult getReportReceived(String msgIds) {
        setjPushClient(getJPushClient());
        try {
            return jPushClient.getReportReceiveds(msgIds);
        } catch (APIConnectionException | APIRequestException e) {
            logger.debug(MessageFormat.format("get report error, msgIds:{0}:{1}", msgIds, e.getMessage()));
            return null;
        }
    }

    public static JPushClient getjPushClient() {
        return jPushClient;
    }

    public static void setjPushClient(JPushClient jPushClient) {
        MobileAppJPushClient.jPushClient = jPushClient;
    }
}
