package com.tuotiansudai.repository.model;

public enum PledgeType {
    HOUSE("房产抵押物"),
    VEHICLE("车辆抵押物"),
    ENTERPRISE_CREDIT("税易经营性借款信用类"),
    ENTERPRISE_PLEDGE("税易经营性借款抵押类"),
    ENTERPRISE_FACTORING("企业经营性借款—保理"),
    ENTERPRISE_BILL("企业经营性借款—票据"),
    PERSONAL_CAPITAL_TURNOVER("个人资金周转"),
    NONE("无抵押物");

    final private String description;

    PledgeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
