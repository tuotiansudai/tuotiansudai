package com.tuotiansudai.dto;


public enum ContractStatus {
    CONTRACT_NOT_EXIST("合同不存在"),
    CONTRACT_ALREADY_CREATED("合同已经生成"),
    CONTRACT_CREATING("合同正在生成");

    ContractStatus(String description) {
        this.description = description;
    }

    private String description;
}
