package com.tuotiansudai.repository.model;

public enum PledgeType {
    HOUSE("房产抵押物"),
    VEHICLE("车辆抵押物"),
    ENTERPRISE_AGENT("企业经营借款"), // 企业经营借款-代理模式
    ENTERPRISE_DIRECT("企业经营借款"), // 企业经营借款-直贷模式
    NONE("无抵押物");

    final private String description;

    PledgeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
