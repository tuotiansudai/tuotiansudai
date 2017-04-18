package com.tuotiansudai.service;

public interface WeChatService {

    String fetchToken();

    String findByOpenid(String openid);

    String fetchOpenid(String sessionId, String state, String code);

    String generateAuthorizeURL(String sessionId, String redirect);

    boolean isWeChatUserBound(String openid);

    boolean bind(String mobile, String weChatUserLoginName);
}
