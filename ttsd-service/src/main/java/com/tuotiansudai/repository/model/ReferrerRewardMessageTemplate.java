package com.tuotiansudai.repository.model;

public enum ReferrerRewardMessageTemplate {
    TRANSFER_OUT_DETAIL("referrer_reward,orderId:{0},referrer:{1},get:¥{2}"),
    NOT_BIND_CARD("未在联动优势开通账户,交易失败");

    private final String description;

    ReferrerRewardMessageTemplate(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
