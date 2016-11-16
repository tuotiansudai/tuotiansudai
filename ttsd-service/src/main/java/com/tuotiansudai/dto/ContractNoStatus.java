package com.tuotiansudai.dto;


public enum ContractNoStatus {
    OLD("旧版合同"),
    OK("合同已生成"),
    WAITING("合同生成中");

    ContractNoStatus(String description) {
        this.description = description;
    }

    private String description;
}
