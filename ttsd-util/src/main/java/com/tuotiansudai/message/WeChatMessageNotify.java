package com.tuotiansudai.message;

import com.tuotiansudai.enums.WeChatMessageType;

import java.io.Serializable;

public class WeChatMessageNotify implements Serializable {
    private String loginName;

    private WeChatMessageType weChatMessageType;

    private Long businessId;

    public WeChatMessageNotify() {
    }

    public WeChatMessageNotify(String loginName, WeChatMessageType weChatMessageType, Long businessId) {
        this.loginName = loginName;
        this.weChatMessageType = weChatMessageType;
        this.businessId = businessId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public WeChatMessageType getWeChatMessageType() {
        return weChatMessageType;
    }

    public void setWeChatMessageType(WeChatMessageType weChatMessageType) {
        this.weChatMessageType = weChatMessageType;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
}
