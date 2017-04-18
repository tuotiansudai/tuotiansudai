package com.tuotiansudai.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.service.LoginNameGenerator;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WeChatServiceImpl implements WeChatService {

    private static Logger logger = Logger.getLogger(WeChatServiceImpl.class);

    private final static String AUTHORIZE_URL_TEMPLATE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope=snsapi_base&state={2}#wechat_redirect";

    private final static String ACCESS_TOKEN_URL_TEMPLATE = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value(value = "${wechat.appId}")
    private String appId;

    @Value(value = "${wechat.appSecret}")
    private String appSecret;

    @Value(value = "${wechat.redirect}")
    private String defaultRedirect;

    private final UserMapper userMapper;

    private final WeChatUserMapper weChatUserMapper;

    private final RedisWrapperClient redisWrapperClient;

    private final LoginNameGenerator loginNameGenerator;

    @Autowired
    public WeChatServiceImpl(UserMapper userMapper, RedisWrapperClient redisWrapperClient, WeChatUserMapper weChatUserMapper, LoginNameGenerator loginNameGenerator) {
        this.userMapper = userMapper;
        this.redisWrapperClient = redisWrapperClient;
        this.loginNameGenerator = loginNameGenerator;
        this.client.setReadTimeout(5, TimeUnit.SECONDS);
        this.client.setConnectTimeout(5, TimeUnit.SECONDS);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.weChatUserMapper = weChatUserMapper;
    }

    @Override
    public String findByOpenid(String openid) {
        return weChatUserMapper.findByOpenid(openid).getLoginName();
    }

    @Override
    public String fetchOpenid(String sessionId, String state, String code) {
        if (Strings.isNullOrEmpty(redisWrapperClient.get(sessionId)) || Strings.isNullOrEmpty(state) || !state.equals(redisWrapperClient.get(sessionId))) {
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

            String openid = result.get("openid");
            WeChatUserModel weChatUserModel = weChatUserMapper.findByOpenid(openid);
            if (weChatUserModel == null) {
                weChatUserMapper.create(new WeChatUserModel(loginNameGenerator.generate(), openid));
            } else {
                weChatUserModel.setLatestLoginTime(new Date());
                weChatUserMapper.update(weChatUserModel);
            }

            return openid;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    @Override
    public String generateAuthorizeURL(String sessionId, String redirect) {
        return MessageFormat.format(AUTHORIZE_URL_TEMPLATE, appId,
                Strings.isNullOrEmpty(redirect) ? this.defaultRedirect : MessageFormat.format("{0}?redirect={1}", this.defaultRedirect, redirect),
                this.generateAuthorizeState(sessionId));
    }

    @Override
    public boolean isWeChatUserBound(String openid) {
        WeChatUserModel weChatUserModel = weChatUserMapper.findByOpenid(openid);

        return weChatUserModel != null && weChatUserModel.isBound() && Days.daysBetween(new DateTime(weChatUserModel.getLatestLoginTime()), new DateTime()).getDays() < 30;
    }

    @Override
    public boolean bind(String mobile, String openid) {
        UserModel userModel = userMapper.findByMobile(mobile);
        WeChatUserModel weChatUserModel = weChatUserMapper.findByOpenid(openid);
        if (weChatUserModel == null) {
            return false;
        }

        weChatUserModel.setLoginName(userModel.getLoginName());
        weChatUserModel.setBound(true);
        weChatUserModel.setLatestLoginTime(new Date());

        weChatUserMapper.update(weChatUserModel);

        logger.info(MessageFormat.format("wechat bind successfully. mobile: {0} openid: {1}", mobile, openid));

        return true;
    }

    private String generateAuthorizeState(String sessionId) {
        String state = UUIDGenerator.generate();
        redisWrapperClient.setex(sessionId, 10, state);
        return state;
    }
}
