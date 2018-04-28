package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.WeChatUserModel;

public interface WeChatService {

    WeChatUserModel parseWeChatUserStatus(String sessionId, String state, String code);

    String generateMuteAuthorizeURL(String sessionId, String redirect);

    String generateActiveAuthorizeURL(String sessionId, String redirect);

    boolean isBound(String loginName);
}
