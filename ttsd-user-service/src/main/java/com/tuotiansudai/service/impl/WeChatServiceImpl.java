package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.WeChatClient;
import com.tuotiansudai.enums.WeChatMessageType;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.service.LoginNameGenerator;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final UserMapper userMapper;

    private final WeChatUserMapper weChatUserMapper;

    private final RedisWrapperClient redisWrapperClient;

    private final LoginNameGenerator loginNameGenerator;

    private final WeChatClient weChatClient;

    @Autowired
    public WeChatServiceImpl(UserMapper userMapper, RedisWrapperClient redisWrapperClient, WeChatUserMapper weChatUserMapper, LoginNameGenerator loginNameGenerator, WeChatClient weChatClient) {
        this.userMapper = userMapper;
        this.redisWrapperClient = redisWrapperClient;
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
            weChatUserMapper.create(new WeChatUserModel(loginNameGenerator.generate(), openid));
        } else {
            weChatUserModel.setLatestLoginTime(new Date());
            weChatUserMapper.update(weChatUserModel);
        }
        return weChatUserModel;
    }

    @Override
    @Transactional
    public void bind(String mobile, String openid) {
        UserModel userModel = userMapper.findByMobile(mobile);
        WeChatUserModel weChatUserModel = weChatUserMapper.findByOpenid(openid);

        if (userModel == null || weChatUserModel == null) {
            return;
        }

        weChatUserMapper.findByLoginName(userModel.getLoginName())
                .stream()
                .filter(boundUser -> boundUser.getOpenid().equals(openid)
                        && !boundUser.getLoginName().equals(userModel.getLoginName())
                        && boundUser.isBound())
                .forEach(boundUser -> {
                    boundUser.setBound(false);
                    weChatUserMapper.update(boundUser);
                    weChatClient.sendTemplateMessage(WeChatMessageType.BOUND_TO_OTHER_USER, Maps.newHashMap(ImmutableMap.<String, String>builder()
                            .put("openid", boundUser.getOpenid())
                            .put("first", "first")
                            .put("keyword1", "keyword1")
                            .put("keyword2", "keyword2")
                            .put("remark", "remark")
                            .build()));
                    logger.info(MessageFormat.format("wechat unbound previous use successfully. user: {0}, openid: {1}, previous user: {2}", userModel.getLoginName(), openid, boundUser.getLoginName()));
                });


        weChatUserModel.setLoginName(userModel.getLoginName());
        weChatUserModel.setBound(true);
        weChatUserModel.setLatestLoginTime(new Date());

        weChatUserMapper.update(weChatUserModel);

        logger.info(MessageFormat.format("wechat bind successfully. user: {0}, openid: {1}", userModel.getLoginName(), openid));
    }
}
