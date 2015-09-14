package com.tuotiansudai.paywrapper.repository.model;

/**
 * 转账方账户类型
 */
public enum UmPayParticAccType {
    /**
     * 个人
     */
    PERSON("01"),
    /**
     * 商户
     */
    MER("02");

    private final String code;

    UmPayParticAccType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
