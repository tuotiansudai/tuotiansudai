package com.tuotiansudai.repository.model;

public enum PledgeType {
    HOUSE("房产抵押物"),
    VEHICLE("车辆抵押物"),
    ENTERPRISE("企业"),
    NONE("无抵押物");

    final private String description;

    PledgeType(String description) {
        this.description = description;
    }
}
