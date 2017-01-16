package com.tuotiansudai.client;

import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.squareup.okhttp.*;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.message.PushMessage;
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

    private static Map<PushSource, Platform> PLATFORM_MAPPING = Maps.newHashMap(ImmutableMap.<PushSource, Platform>builder()
            .put(PushSource.ALL, Platform.all())
            .put(PushSource.IOS, Platform.ios())
            .put(PushSource.ANDROID, Platform.android())
            .build());

    private RedisWrapperClient redisWrapperClient;

    private static final String PUSH_ID_KEY = "api:jpushId:store";

    @Autowired
    public PushClient(RedisWrapperClient redisWrapperClient, @Value("${common.jpush.masterSecret}") String masterSecret, @Value("${common.jpush.appKey}") String appKey, @Value("${common.environment}") Environment environment) {
        this.redisWrapperClient = redisWrapperClient;
        this.masterSecret = masterSecret;
        this.appKey = appKey;
        this.environment = environment;
        this.httpClient = new OkHttpClient();
        this.httpClient.setConnectTimeout(5, TimeUnit.SECONDS);
    }

    public void storeJPushId(String loginName, String platform, String jPushId) {
        if (Strings.isNullOrEmpty(loginName) || Strings.isNullOrEmpty(platform) || Strings.isNullOrEmpty(jPushId)) {
            return;
        }
        redisWrapperClient.hset(PUSH_ID_KEY, loginName, MessageFormat.format("{0}-{1}", platform.toLowerCase(), jPushId));
    }

    public void removeJPushId(String loginName) {
        redisWrapperClient.hdel(PUSH_ID_KEY, loginName);
    }

    public void sendJPush(PushMessage pushMessage) {
        if (CollectionUtils.isEmpty(pushMessage.getLoginNames())) {
            send(buildPushPayload(null, pushMessage.getContent(), pushMessage.getPushSource(), Maps.newHashMap()));
            return;
        }

        List<String> registrationIds = Lists.newArrayList();
        for (String loginName : pushMessage.getLoginNames()) {
            String value = redisWrapperClient.hget(PUSH_ID_KEY, loginName);
            if (!Strings.isNullOrEmpty(value) && value.split("-").length == 2) {
                registrationIds.add(value.split("-")[1]);
            }
        }

        for (int batch = 0; batch < registrationIds.size() / 1000 + (registrationIds.size() % 1000 > 0 ? 1 : 0); batch++) {
            send(buildPushPayload(registrationIds.subList(batch * 1000,
                    (batch + 1) * 1000 > registrationIds.size() ? registrationIds.size() : (batch + 1) * 1000),
                    pushMessage.getContent(),
                    pushMessage.getPushSource(),
                    Maps.newHashMap()));
        }
    }

    private PushPayload buildPushPayload(List<String> registrationIds, String content, PushSource pushSource, Map<String, String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(PLATFORM_MAPPING.get(pushSource))
                .setAudience(getAudience(registrationIds))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder().setAlert(content).addExtras(extras).setBadge(0).build())
                        .addPlatformNotification(AndroidNotification.newBuilder().setAlert(content).addExtras(extras).setBuilderId(2).build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(Environment.isProduction(environment)).build())
                .build();
    }

    private Audience getAudience(List<String> registrationIds) {
        Audience.Builder builder = Audience.newBuilder();
        if (CollectionUtils.isNotEmpty(registrationIds)) {
            builder.addAudienceTarget(AudienceTarget.registrationId(registrationIds));
        }

        if (Environment.isProduction(environment)) {
            builder.setAll(CollectionUtils.isEmpty(registrationIds));
        } else {
            builder.addAudienceTarget(AudienceTarget.tag("test"));
        }

        return builder.build();
    }

    private boolean send(PushPayload payload) {
        if (payload == null) {
            logger.warn("[Push] payload is null");
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
                logger.warn(MessageFormat.format("[Push] push is not 2xx (request={0}, code={1}, response={2}, tryTimes={3})",
                        payload.toJSON().toString(), response.code(), response.body().string(), String.valueOf(tryTimes)));
            } catch (Exception e) {
                logger.warn(MessageFormat.format("[Push] push IOException (request={0}, tryTimes={3})", payload.toJSON().getAsString(), String.valueOf(tryTimes)), e);
            }
        } while (++tryTimes < 4);

        return false;
    }
}
