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
     * 债权购买
     */
    TRANSFER_IN_TRANSFER("02"),

    /**
     * 还款
     */
    TRANSFER_IN_REPAY("03"),

    /**
     * 流标后返款
     */
    TRANSFER_OUT_CANCEL_PAYBACK("51"),

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
    TRANSFER_OUT_REPAY_PAYBACK("54"),

    /**
     * 债权转让的返款
     */
    TRANSFER_OUT_TRANSFER("56"),

    /**
     * 超投返款（撤资后返款）
     */
    TRANSFER_OVER_INVEST_PAYBACK("57");

    private final String code;

    UmPayServType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
