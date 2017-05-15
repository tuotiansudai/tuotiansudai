package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.client.WeChatClient;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.service.LoginNameGenerator;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.util.RedisWrapperClient;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class WeChatServiceImpl implements WeChatService {

    private static Logger logger = Logger.getLogger(WeChatServiceImpl.class);

    private final static String AUTHORIZE_URL_TEMPLATE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope=snsapi_base&state={2}#wechat_redirect";

    @Value(value = "${wechat.appId}")
    private String appId;

    @Value(value = "${wechat.authorize.callbck}")
    private String authorizeCallback;

    private final WeChatUserMapper weChatUserMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final LoginNameGenerator loginNameGenerator;

    private final WeChatClient weChatClient;

    @Autowired
    public WeChatServiceImpl(WeChatUserMapper weChatUserMapper, LoginNameGenerator loginNameGenerator, WeChatClient weChatClient) {
        this.loginNameGenerator = loginNameGenerator;
        this.weChatClient = weChatClient;
        this.weChatUserMapper = weChatUserMapper;
    }

    @Override
    public String generateAuthorizeURL(String sessionId, String redirect) {
        String state = UUIDGenerator.generate();
        redisWrapperClient.setex(sessionId, 10, state);

        return MessageFormat.format(AUTHORIZE_URL_TEMPLATE,
                appId,
                Strings.isNullOrEmpty(redirect) ? this.authorizeCallback : MessageFormat.format("{0}?redirect={1}", this.authorizeCallback, redirect),
                state);
    }

    @Override
    public WeChatUserModel parseWeChatUserStatus(String sessionId, String state, String code) {
        String openid = weChatClient.fetchOpenid(sessionId, state, code);

        if (Strings.isNullOrEmpty(openid)) {
            return null;
        }

        WeChatUserModel weChatUserModel = weChatUserMapper.findByOpenid(openid);
        if (weChatUserModel == null) {
            weChatUserModel = new WeChatUserModel(loginNameGenerator.generate(), openid);
            weChatUserMapper.create(weChatUserModel);
        } else {
            weChatUserModel.setLatestLoginTime(new Date());
            weChatUserMapper.update(weChatUserModel);
        }
        return weChatUserModel;
    }
}
