package com.tuotiansudai.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.squareup.okhttp.*;
import com.tuotiansudai.enums.WeChatMessageType;
import com.tuotiansudai.etcd.ETCDConfigReader;
import org.apache.log4j.Logger;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class WeChatClient {

    private static Logger logger = Logger.getLogger(WeChatClient.class);

    private final static WeChatClient instance = new WeChatClient();

    private final static String TOKEN_URL_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

    private final static String JS_API_TICKET_URL_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type=jsapi";

    private final static String ACCESS_TOKEN_URL_TEMPLATE = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

    private final static String MESSAGE_URL_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={0}";

    private final static String WE_CHAT_USER_INFO_URL_TEMPLATE = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}&lang=zh_CN";

    private final static String WE_CHAT_REFRESH_TOKEN_TEMPLATE = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid={0}&grant_type=refresh_token&refresh_token={1}";

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String CANCELED_WE_CHAT_ATTENTION_CODE = "43004";

    private final static Map<WeChatMessageType, String> TEMPLATE_MAP = Maps.newHashMap(
            ImmutableMap.<WeChatMessageType, String>builder()
                    .put(WeChatMessageType.BOUND_TO_OTHER_USER, ETCDConfigReader.getReader().getValue("wechat.bound.to.other.user.id"))
                    .put(WeChatMessageType.TRANSFER_SUCCESS, ETCDConfigReader.getReader().getValue("wechat.transfer.success.id"))
                    .put(WeChatMessageType.WITHDRAW_NOTIFY_SUCCESS, ETCDConfigReader.getReader().getValue("wechat.withdraw.notify.success.id"))
                    .put(WeChatMessageType.WITHDRAW_APPLY_SUCCESS, ETCDConfigReader.getReader().getValue("wechat.withdraw.apply.success.id"))
                    .put(WeChatMessageType.ADVANCE_REPAY_SUCCESS, ETCDConfigReader.getReader().getValue("wechat.advance.repay.success.id"))
                    .put(WeChatMessageType.NORMAL_REPAY_SUCCESS, ETCDConfigReader.getReader().getValue("wechat.normal.repay.success.id"))
                    .put(WeChatMessageType.INVEST_SUCCESS, ETCDConfigReader.getReader().getValue("wechat.invest.success.id"))
                    .put(WeChatMessageType.LOAN_OUT_SUCCESS, ETCDConfigReader.getReader().getValue("wechat.loan.out.success.id"))
                    .build());

    private static String APP_ID = ETCDConfigReader.getReader().getValue("wechat.appId");

    private static String APP_SECRET = ETCDConfigReader.getReader().getValue("wechat.appSecret");

    public static WeChatClient getClient() {
        return instance;
    }

    private WeChatClient() {
        this.client.setConnectTimeout(5, TimeUnit.SECONDS);
        this.client.setReadTimeout(5, TimeUnit.SECONDS);
        this.client.setWriteTimeout(5, TimeUnit.SECONDS);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String getAppid() {
        return APP_ID;
    }

    public String fetchOpenid(String sessionId, String state, String code) {
        String originalState = redisWrapperClient.get(MessageFormat.format("{0}:wechat:state", sessionId));

        logger.info(MessageFormat.format("originalState {0}, state {1}", originalState, state));

        if (Strings.isNullOrEmpty(state) || !state.equals(originalState)) {
            return null;
        }

        try {
            Request request = new Request.Builder()
                    .url(MessageFormat.format(ACCESS_TOKEN_URL_TEMPLATE, APP_ID, APP_SECRET, code))
                    .get().build();
            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                return null;
            }

            String responseString = response.body().string();
            Map<String, String> result = objectMapper.readValue(responseString, new TypeReference<HashMap<String, String>>() {
            });

            if (result.containsKey("errcode")) {
                logger.error(MessageFormat.format("fetch openid failed, response: {0}", responseString));
                return null;
            }

            saveWebAuthorizationToken(result);
            return result.get("openid");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    public void sendTemplateMessage(WeChatMessageType weChatMessageType, Map<String, String> params) {
        String token = this.fetchToken();
        if (Strings.isNullOrEmpty(token)) {
            logger.error("fetch token failed");
            return;
        }

        try {
            URL resource = WeChatClient.class.getClassLoader().getResource(weChatMessageType.getJsonFile());
            if (resource == null) {
                logger.error(MessageFormat.format("load request json failed, json file: {0}", weChatMessageType.getJsonFile()));
                return;
            }
            String bodyTemplate = Resources.toString(resource, Charsets.UTF_8);


            for (String key : params.keySet()) {
                bodyTemplate = bodyTemplate.replace("{{" + key + "}}", params.get(key));
            }

            bodyTemplate = bodyTemplate.replace("{{template_id}}", TEMPLATE_MAP.get(weChatMessageType));

            Request request = new Request.Builder()
                    .url(MessageFormat.format(MESSAGE_URL_TEMPLATE, token))
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyTemplate))
                    .build();

            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                logger.error(MessageFormat.format("send message failed, response code: {0}", response.code()));
                return;
            }

            String responseString = response.body().string();
            Map<String, String> result = objectMapper.readValue(responseString, new TypeReference<HashMap<String, String>>() {
            });

            if (!"0".equals(result.get("errcode"))) {
                if (CANCELED_WE_CHAT_ATTENTION_CODE.equals(result.get("errcode"))) {
                    logger.info(MessageFormat.format("send message failed, response: {0}", responseString));
                    return;
                }
                logger.error(MessageFormat.format("send message failed, response: {0}", responseString));
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    public String fetchJSAPITicket() {
        String ticket = redisWrapperClient.get("wechat:jsapi:ticket");

        if (!Strings.isNullOrEmpty(ticket)) {
            return ticket;
        }

        String token = this.fetchToken();

        if (Strings.isNullOrEmpty(token)) {
            return null;
        }

        try {
            Request request = new Request.Builder()
                    .url(MessageFormat.format(JS_API_TICKET_URL_TEMPLATE, token))
                    .get().build();
            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                return null;
            }

            String responseString = response.body().string();
            Map<String, String> result = objectMapper.readValue(responseString, new TypeReference<HashMap<String, String>>() {
            });

            if (!"0".equals(result.get("errcode"))) {
                logger.error(MessageFormat.format("fetch js api ticket failed, response: {0}", responseString));
            }

            token = result.get("ticket");
            redisWrapperClient.setex("wechat:jsapi:ticket", Integer.parseInt(result.get("expires_in")) - 60, token);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return ticket;
    }

    private String fetchToken() {
        String token = redisWrapperClient.get("wechat:token");
        if (!Strings.isNullOrEmpty(token)) {
            return token;
        }

        try {
            Request request = new Request.Builder()
                    .url(MessageFormat.format(TOKEN_URL_TEMPLATE, APP_ID, APP_SECRET))
                    .get().build();
            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
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
            redisWrapperClient.setex("wechat:token", Integer.parseInt(result.get("expires_in")) - 60, token);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return token;
    }

    public void saveWebAuthorizationToken(Map<String, String> result){
        redisWrapperClient.setex(MessageFormat.format("wechat:web:access:token:{0}", result.get("openid")), Integer.parseInt(result.get("expires_in")) - 60, result.get("access_token"));
        redisWrapperClient.setex(MessageFormat.format("wechat:web:refresh:token:{0}", result.get("openid")), 29 * 24 * 60 * 60, result.get("refresh_token"));
    }

    public String getWebAuthorizationToken(String openId){
        String accessToken = redisWrapperClient.get(MessageFormat.format("wechat:web:access:token:{0}", openId));
        if (accessToken != null) {
            return accessToken;
        }

        String refreshToken = redisWrapperClient.get(MessageFormat.format("wechat:web:refresh:token:{0}", openId));
        if (refreshToken == null){
            return null;
        }
        try {
            Request request = new Request.Builder()
                    .url(MessageFormat.format(WE_CHAT_REFRESH_TOKEN_TEMPLATE, APP_ID, refreshToken))
                    .get().build();
            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                return null;
            }
            String responseString = response.body().string();
            Map<String, String> result = objectMapper.readValue(responseString, new TypeReference<HashMap<String, String>>() {

            });

            if (result.containsKey("errcode")) {
                logger.error(MessageFormat.format("refresh token failed, openId: {0} response: {1}", openId, responseString));
                return null;
            }
            saveWebAuthorizationToken(result);
            return result.get("access_token");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;

    }

    public Map<String, Object> fetchWeChatUserInfo(String openId){
        String token = getWebAuthorizationToken(openId);

        if (Strings.isNullOrEmpty(token)) {
            return null;
        }

        try {
            Request request = new Request.Builder()
                    .url(MessageFormat.format(WE_CHAT_USER_INFO_URL_TEMPLATE, token, openId))
                    .get().build();
            Response response = client.newCall(request).execute();

            if (response.code() != 200) {
                return null;
            }
            String responseString = response.body().string();
            Map<String, Object> result = objectMapper.readValue(responseString, new TypeReference<HashMap<String, Object>>() {
            });

            if (result.containsKey("errcode")) {
                logger.error(MessageFormat.format("fetch we chat user info failed, openId: {0} response: {1}", openId, responseString));
                return null;
            }
            return result;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}
