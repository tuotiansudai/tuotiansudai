package com.tuotiansudai.repository.model;

public enum AgreementType {

    ZCZP0800("无密快捷协议"),
    ZTBB0G00("无密投资协议"),
    ZHKB0H01("无密还款协议"),
    ZKJP0700("借记卡快捷协议");

    private String agreementTypeName;

    AgreementType(String agreementTypeName) {
        this.agreementTypeName = agreementTypeName;
    }

    public String getAgreementTypeName() {
        return agreementTypeName;
    }

    public void setAgreementTypeName(String agreementTypeName) {
        this.agreementTypeName = agreementTypeName;
    }

}
