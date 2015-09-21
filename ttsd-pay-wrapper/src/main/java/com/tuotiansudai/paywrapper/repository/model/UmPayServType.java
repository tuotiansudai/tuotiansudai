package com.tuotiansudai.paywrapper.repository.model;

/**
 * 业务类型
 */
public enum UmPayServType {
    /**
     * 投资
     */
    TRANSFER_IN_INVEST("01"),

    /**
     * 还款
     */
    TRANSFER_IN_REPAY("03"),

    /**
     * 流标后返款
     */
    TRANSFER_OUT_PAYBACK_ON_CANCEL("51"),

    /**
     * 平台收费
     */
    TRANSFER_OUT_PLATFORM_FEE("52"),

    /**
     * 放款
     */
    TRANSFER_OUT_LOAN_OUT("53"),

    /**
     * 还款后返款
     */
    TRANSFER_OUT_PAYBACK_ON_REPAY("54");

    private final String code;

    UmPayServType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
