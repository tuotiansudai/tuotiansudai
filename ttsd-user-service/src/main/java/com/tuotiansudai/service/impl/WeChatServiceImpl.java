package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.service.LoginNameGenerator;
import com.tuotiansudai.service.WeChatService;
import com.tuotiansudai.util.RedisWrapperClient;
import com.tuotiansudai.util.UUIDGenerator;
import com.tuotiansudai.util.WeChatClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class WeChatServiceImpl implements WeChatService {

    private static Logger logger = Logger.getLogger(WeChatServiceImpl.class);

    private final static String AUTHORIZE_URL_TEMPLATE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={0}&redirect_uri={1}&response_type=code&scope=snsapi_base&state={2}#wechat_redirect";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final WeChatClient weChatClient = WeChatClient.getClient();

    @Value(value = "${wechat.authorize.callback}")
    private String authorizeCallback;

    private final WeChatUserMapper weChatUserMapper;

    private final LoginNameGenerator loginNameGenerator;

    @Autowired
    public WeChatServiceImpl(WeChatUserMapper weChatUserMapper, LoginNameGenerator loginNameGenerator) {
        this.loginNameGenerator = loginNameGenerator;
        this.weChatUserMapper = weChatUserMapper;
    }

    @Override
    public String generateAuthorizeURL(String sessionId, String redirect) {
        String state = UUIDGenerator.generate();
        redisWrapperClient.setex(MessageFormat.format("{0}:wechat:state", sessionId), 60, state);

        return MessageFormat.format(AUTHORIZE_URL_TEMPLATE,
                weChatClient.getAppid(),
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

    @Override
    public boolean isBound(String loginName) {
        List<WeChatUserModel> weChatUserModels = weChatUserMapper.findByLoginName(loginName);
        return weChatUserModels.stream().filter(WeChatUserModel::isBound).count() > 0;
    }
}
