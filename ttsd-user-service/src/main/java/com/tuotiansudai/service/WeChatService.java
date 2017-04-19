package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.WeChatUserModel;

public interface WeChatService {

    WeChatUserModel parseWeChatUserStatus(String sessionId, String state, String code);

    String generateAuthorizeURL(String sessionId, String redirect);

    boolean bind(String mobile, String weChatUserLoginName);
}
