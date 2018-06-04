package com.tuotiansudai.fudian.dto;

public enum RechargePayType {

    FAST_PAY("1"), //快捷
    BANK_PAY("5"), //富滇直充
    GATE_PAY("6"); //网银

    private String value;

    RechargePayType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
