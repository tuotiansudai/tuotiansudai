package com.tuotiansudai.push.client;

import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.common.collect.Maps;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.Environment;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class PushClient {

    static Logger logger = Logger.getLogger(PushClient.class);

    private String masterSecret;

    private String appKey;

    private Environment environment;

    private OkHttpClient httpClient;

    private ClientConfig clientConfig = ClientConfig.getInstance();

    @Autowired
    public PushClient(@Value("${common.jpush.masterSecret}") String masterSecret, @Value("${common.jpush.appKey}") String appKey, @Value("${common.environment}") Environment environment) {
        this.masterSecret = masterSecret;
        this.appKey = appKey;
        this.environment = environment;
        this.httpClient = new OkHttpClient();
        this.httpClient.setConnectTimeout(5, TimeUnit.SECONDS);
    }

    public boolean sendJPush(List<String> registrationIds, String alert) {
        return send(buildPushPayload(registrationIds, alert, Maps.newHashMap()));
    }

    private PushPayload buildPushPayload(List<String> registrationIds, String alert, Map<String, String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(getAudience(registrationIds))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(alert).addExtras(extras).setBadge(0).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).addExtras(extras).setBuilderId(2).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();
    }

    private Audience getAudience(List<String> registrationIds) {
        Audience.Builder builder = Audience.newBuilder();
        if (!Environment.isProduction(environment)) {
            builder.addAudienceTarget(AudienceTarget.tag("test"));
        }

        if (CollectionUtils.isNotEmpty(registrationIds)) {
            builder.addAudienceTarget(AudienceTarget.registrationId(registrationIds));
        } else {
            builder.setAll(true);
        }

        return builder.build();
    }

    private boolean send(PushPayload payload) {
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
                if (HttpStatus.valueOf(response.code()).is2xxSuccessful()) {
                    return true;
                }
                logger.error(MessageFormat.format("[JPush] push is not 2xx (request={0}, code={1}, response={2}, tryTimes={3})",
                        payload.toJSON().toString(), response.code(), response.body().string(), String.valueOf(tryTimes)));
            } catch (Exception e) {
                logger.error(MessageFormat.format("[JPush] push IOException (request={0}, tryTimes={3})", payload.toJSON().getAsString(), String.valueOf(tryTimes)), e);
            }
        } while (++tryTimes < 4);

        return false;
    }
}
