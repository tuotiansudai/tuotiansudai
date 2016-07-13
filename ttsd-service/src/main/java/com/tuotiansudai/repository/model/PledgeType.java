package com.tuotiansudai.repository.model;

public enum PledgeType {
    HOUSE("房产抵押物"),
    VEHICLE("车辆抵押物"),
    NULL("无抵押物");

    private String description;

    PledgeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
