package com.tuotiansudai.cfca.dto;


public enum AnxinContractType {
    TRANSFER_CONTRACT("债权转让合同"),
    LOAN_CONTRACT("直投合同");

    AnxinContractType(String description) {
        this.description = description;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
