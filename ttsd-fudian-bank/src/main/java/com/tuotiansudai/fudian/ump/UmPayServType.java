package com.tuotiansudai.fudian.ump;

/**
 * 业务类型
 */
public enum UmPayServType {
    /**
     * 还款
     */
    TRANSFER_IN_REPAY("03"),
    /**
     * 平台收费
     */
    TRANSFER_OUT_PLATFORM_FEE("52"),
    /**
     * 还款后返款
     */
    TRANSFER_OUT_REPAY_PAYBACK("54");

    private final String code;

    UmPayServType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
