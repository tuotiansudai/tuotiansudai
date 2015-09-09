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
     * 偿付
     */
    TRANSFER_IN_PAYBACK("04"),

    /**
     * 贴现
     */
    TRANSFER_IN_DISCOUNT("05"),

    /**
     * 流标后返款
     */
    TRANSFER_OUT_PAYBACK_ON_FAIL("51"),

    /**
     * 平台收费
     */
    TRANSFER_OUT_PLATFORM_FEE("52"),

    /**
     * 放款
     */
    TRANSFER_OUT_GIVE_MONEY_TO_BORROWER("53"),

    /**
     * 还款后返款
     */
    TRANSFER_OUT_REPAY_BACK("54"),

    /**
     * 偿付后返款
     */
    TRANSFER_OUT_PAYBACK_BACK("55"),

    /**
     * 债权转让的返款
     */
    TRANSFER_OUT_TRANSFER_BACK("56"),

    /**
     * 撤资后的返款
     */
    TRANSFER_OUT_WITHDRAW_BACK("57");

    private final String code;

    UmPayServType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
