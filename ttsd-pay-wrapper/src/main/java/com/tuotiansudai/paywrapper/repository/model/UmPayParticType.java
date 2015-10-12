package com.tuotiansudai.paywrapper.repository.model;

/**
 * 转账方类型
 */
public enum UmPayParticType {
    /**
     * 投资者
     */
    INVESTOR("01"),
    /**
     * 融资人
     */
    LOANER("02"),
    /**
     * P2P平台
     */
    PLATFORM("03");

    private final String code;

    UmPayParticType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
