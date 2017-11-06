package com.tuotiansudai.repository.model;

public enum PayrollPayStatus {

    WAITING("待支付"),
    PAYING("正在处理"),
    SUCCESS("支付成功"),
    FAIL("支付失败");

    private final String description;

    PayrollPayStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
