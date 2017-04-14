package com.tuotiansudai.service;

public interface WeChatService {

    String findByOpenid(String openid);

    String fetchOpenid(String sessionId, String state, String code);

    String generateAuthorizeURL(String sessionId);

    boolean isWeChatUserBound(String openid);

    boolean bind(String mobile, String weChatUserLoginName);
}
