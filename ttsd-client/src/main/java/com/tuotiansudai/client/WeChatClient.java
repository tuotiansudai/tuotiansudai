package com.tuotiansudai.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.enums.WeChatMessageType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class WeChatClient {

    private static Logger logger = Logger.getLogger(WeChatClient.class);

    private final static String TOKEN_URL_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

    private final static String ACCESS_TOKEN_URL_TEMPLATE = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final static Map<WeChatMessageType, String> TEMPLATE_MAP = Maps.newHashMap();

    @Value(value = "${wechat.appId}")
    private String appId;

    @Value(value = "${wechat.appSecret}")
    private String appSecret;

    private final RedisWrapperClient redisWrapperClient;

    static {
        try (InputStreamReader reader = new InputStreamReader(WeChatClient.class.getClassLoader().getResourceAsStream("ttsd-env.properties"), StandardCharsets.UTF_8.name())) {
            Properties config = new Properties();
            config.load(reader);
            TEMPLATE_MAP.put(WeChatMessageType.BOUND_TO_OTHER_USER, config.getProperty("wechat.template1.id"));
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Autowired
    public WeChatClient(RedisWrapperClient redisWrapperClient) {
        this.redisWrapperClient = redisWrapperClient;
        this.client.setReadTimeout(5, TimeUnit.SECONDS);
        this.client.setConnectTimeout(5, TimeUnit.SECONDS);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private String fetchToken() {
        String token = redisWrapperClient.get("wechat:token");
        if (!Strings.isNullOrEmpty(token)) {
            return token;
        }

        try {
            Request request = new Request.Builder()
                    .url(MessageFormat.format(TOKEN_URL_TEMPLATE, appId, appSecret))
                    .get().build();
            Response response = client.newCall(request).execute();

            if (!HttpStatus.valueOf(response.code()).is2xxSuccessful()) {
                return null;
            }

            String responseString = response.body().string();
            Map<String, String> result = objectMapper.readValue(responseString, new TypeReference<HashMap<String, String>>() {
            });

            if (result.containsKey("errcode")) {
                logger.error(MessageFormat.format("fetch access_token failed, response: {0}", responseString));
                return null;
            }

            token = result.get("access_token");
            redisWrapperClient.setex("wechat:token", Integer.parseInt(result.get("access_token")) - 60, token);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return token;
    }

    public String fetchOpenid(String sessionId, String state, String code) {
        if (Strings.isNullOrEmpty(redisWrapperClient.get(sessionId)) || Strings.isNullOrEmpty(state) || !state.equals(redisWrapperClient.get(sessionId))) {
            redisWrapperClient.del(sessionId);
            return null;
        }

        redisWrapperClient.del(sessionId);

        try {
            Request request = new Request.Builder()
                    .url(MessageFormat.format(ACCESS_TOKEN_URL_TEMPLATE, appId, appSecret, code))
                    .get().build();
            Response response = client.newCall(request).execute();

            if (!HttpStatus.valueOf(response.code()).is2xxSuccessful()) {
                return null;
            }

            String responseString = response.body().string();
            Map<String, String> result = objectMapper.readValue(responseString, new TypeReference<HashMap<String, String>>() {
            });

            if (result.containsKey("errcode")) {
                logger.error(MessageFormat.format("fetch openid failed, response: {0}", responseString));
                return null;
            }

            return result.get("openid");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    public void sendTemplateMessage(WeChatMessageType weChatMessageType, Map<String, String> params) {

    }
}
