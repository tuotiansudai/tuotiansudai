package com.tuotiansudai.repository.model;

public enum EnterpriseInfoType {
    ENTERPRISE_FACTORING("企业经营性借款—保理"),
    ENTERPRISE_BILL("企业经营性借款—票据"),
    NONE("无抵押物");

    final private String description;

    EnterpriseInfoType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
