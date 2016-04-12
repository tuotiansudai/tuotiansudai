package com.tuotiansudai.paywrapper.repository.model;

/**
 * 转账方账户类型
 */
public enum UmPayParticAccType {
    /**
     * 个人
     */
    INDIVIDUAL("01"),
    /**
     * 商户
     */
    MERCHANT("02");

    private final String code;

    UmPayParticAccType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
