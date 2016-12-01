package com.tuotiansudai.jpush.client;

import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.enums.PushSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class MobileAppJPushClient {

    static Logger logger = Logger.getLogger(MobileAppJPushClient.class);

    public static final String APP_PUSH_MSG_ID_KEY = "console:push-msg-ids:";

    @Value("${common.jpush.masterSecret}")
    private String masterSecret;

    @Value("${common.jpush.appKey}")
    private String appKey;

    @Value("${common.environment}")
    private Environment environment;

    private OkHttpClient httpClient;

    private ClientConfig clientConfig = ClientConfig.getInstance();

    public MobileAppJPushClient() {
        this.httpClient = new OkHttpClient();
        this.httpClient.setConnectTimeout(5, TimeUnit.SECONDS);
    }

    public PushPayload buildPushObject_all_registration_id_alertWithExtras(List<String> registrationIds, String alert, Map<String, String> extras, PushSource pushSource) {

        return PushPayload.newBuilder()
                .setPlatform(getPlatform(pushSource))
                .setAudience(getAudience(registrationIds))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtras(extras).setBadge(0).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtras(extras).setBuilderId(2).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();
    }

    private Audience getAudience(List<String> registrationIds) {

        if (Environment.isProduction(environment)) {
            return Audience.newBuilder()
                    .addAudienceTarget(AudienceTarget.registrationId(registrationIds))
                    .build();
        } else {
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

    public PushPayload buildPushObject_all_all_alertWithExtras(String alert, Map<String, String> extras, PushSource pushSource) {
        return PushPayload.newBuilder()
                .setPlatform(getPlatform(pushSource))
                .setAudience(getAudienceAll())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).setBadge(0).addExtras(extras).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtras(extras).setBuilderId(2).build())

                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();
    }

    private Audience getAudienceAll() {
        if (Environment.isProduction(environment)) {
            return Audience.newBuilder().setAll(true).build();
        } else {
            return Audience.newBuilder().addAudienceTarget(AudienceTarget.tag("test")).build();
        }
    }

    public boolean sendPushAlertByAll(String alert, Map<String, String> extras, PushSource pushSource) {
        PushPayload payload = buildPushObject_all_all_alertWithExtras(alert, extras, pushSource);
        return sendPush(payload);
    }

    public boolean sendPushAlertByRegistrationIds(List<String> registrationIds, String alert, Map<String, String> extras, PushSource pushSource) {
        PushPayload payload = buildPushObject_all_registration_id_alertWithExtras(registrationIds, alert, extras, pushSource);
        return sendPush(payload);
    }

    private boolean sendPush(PushPayload payload) {
        if (payload == null) {
            logger.error("[JPush] payload is null");
            return false;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), payload.toJSON().toString());
        Request request = new Request.Builder()
                .url(MessageFormat.format("{0}{1}", clientConfig.get(ClientConfig.PUSH_HOST_NAME), clientConfig.get(ClientConfig.PUSH_PATH)))
                .method("POST", requestBody)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .addHeader("Authorization", Credentials.basic(appKey, masterSecret))
                .build();

        int tryTimes = 1;
        do {
            try {
                Response response = httpClient.newCall(request).execute();
                if (!HttpStatus.valueOf(response.code()).is2xxSuccessful()) {
                    return true;
                }
                logger.error(MessageFormat.format("[JPush] push is not 2xx (request={0}, code={1}, response={2}, tryTimes={3})",
                        requestBody.toString(), response.code(), response.body().string(), String.valueOf(tryTimes)));
            } catch (IOException e) {
                logger.error(MessageFormat.format("[JPush] push IOException (request={0}, tryTimes={3})", payload.toJSON().getAsString(), String.valueOf(tryTimes)), e);
            }
        } while (++tryTimes < 4);

        return false;
    }
}
