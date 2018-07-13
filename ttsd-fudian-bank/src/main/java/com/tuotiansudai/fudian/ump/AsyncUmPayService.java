package com.tuotiansudai.fudian.ump;

public enum AsyncUmPayService {
    /**
     * 绑定银行卡
     */
    PTP_MER_BIND_CARD("ptp_mer_bind_card", "callback/ptp_mer_bind_card", "callback/ptp_mer_bind_card", "mer_bind_card_notify", "tuotian://bindcard/{0}"),
    /**
     * 换卡
     */
    PTP_MER_REPLACE_CARD("ptp_mer_replace_card", "callback/ptp_mer_replace_card", "callback/ptp_mer_replace_card", "mer_replace_card_notify", "tuotian://changecard/{0}"),
    /**
     * 正常还款的转账
     */
    NORMAL_REPAY_PROJECT_TRANSFER("project_transfer", "account", "", "normal_repay_notify", ""),
    /**
     * 提前还款的转账
     */
    ADVANCE_REPAY_PROJECT_TRANSFER("project_transfer", "account", "", "advance_repay_notify", ""),
    /**
     * 投资正常返款的转账
     */
    NORMAL_REPAY_PAYBACK_PROJECT_TRANSFER("project_transfer", "", "", "normal_repay_payback_notify", ""),
    /**
     * 手续费正常返款的转账
     */
    NORMAL_REPAY_INVEST_FEE_PROJECT_TRANSFER("project_transfer", "", "", "normal_repay_invest_fee_notify", ""),
    /**
     * 投资提前返款的转账
     */
    ADVANCE_REPAY_PAYBACK_PROJECT_TRANSFER("project_transfer", "", "", "advance_repay_payback_notify", ""),
    /**
     * 手续费提前返款的转账
     */
    ADVANCE_REPAY_INVEST_FEE_PROJECT_TRANSFER("project_transfer", "", "", "advance_repay_invest_fee_notify", ""),
    /**
     * 个人账户充值
     */
    MER_RECHARGE_PERSON("mer_recharge_person", "callback/mer_recharge_person", "callback/mer_recharge_person", "recharge_notify", "tuotian://recharge/{0}"),
    /**
     * 个人账户提现
     */
    CUST_WITHDRAWALS("cust_withdrawals", "callback/cust_withdrawals", "callback/cust_withdrawals", "withdraw_notify", "tuotian://withdraw/{0}"),
    /**
     * 优惠券还款普通转账
     */
    COUPON_REPAY_TRANSFER("transfer", "", "", "coupon_repay_notify", ""),
    /**
     * 阶梯加息还款普通转账
     */
    EXTRA_RATE_TRANSFER("transfer", "", "", "extra_rate_notify", ""),
    /**
     * 体验金还款普通转账
     */
    EXPERIENCE_INTEREST_TRANSFER("transfer", "", "", "experience_repay_notify", ""),
    ;

    private final String serviceName;

    private final String webRetCallbackPath;

    private final String mobileRetCallbackPath;

    private final String notifyCallbackPath;

    private final String mobileLink;

    AsyncUmPayService(String serviceName, String webRetCallbackPath, String mobileRetCallbackPath, String notifyCallbackPath, String mobileLink) {
        this.serviceName = serviceName;
        this.webRetCallbackPath = webRetCallbackPath;
        this.mobileRetCallbackPath = mobileRetCallbackPath;
        this.notifyCallbackPath = notifyCallbackPath;
        this.mobileLink = mobileLink;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getWebRetCallbackPath() {
        return webRetCallbackPath;
    }

    public String getMobileRetCallbackPath() {
        return mobileRetCallbackPath;
    }

    public String getNotifyCallbackPath() {
        return notifyCallbackPath;
    }

    public String getMobileLink() {
        return mobileLink;
    }
}
