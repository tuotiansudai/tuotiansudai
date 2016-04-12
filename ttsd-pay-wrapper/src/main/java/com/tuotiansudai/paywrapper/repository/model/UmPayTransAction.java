package com.tuotiansudai.paywrapper.repository.model;

/**
 * 转账方向
 */
public enum UmPayTransAction {
    /**
     * 转入
     */
    IN("01"),
    /**
     * 转出
     */
    OUT("02");

    private final String code;

    UmPayTransAction(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
