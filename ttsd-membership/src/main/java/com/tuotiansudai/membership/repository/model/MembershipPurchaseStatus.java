package com.tuotiansudai.membership.repository.model;

public enum MembershipPurchaseStatus {

    WAIT_PAY("等待支付"),

    SUCCESS("购买成功"),

    FAIL("购买失败");

    private final String description;

    MembershipPurchaseStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
