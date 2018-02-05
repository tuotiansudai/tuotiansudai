package com.tuotiansudai.enums;

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
     * 签约免密投资协议
     */
    NO_PASSWORD_INVEST_PTP_MER_BIND_AGREEMENT("ptp_mer_bind_agreement", "account", "callback/no_password_invest_ptp_mer_bind_agreement", "no_password_invest_notify", "tuotian://sign/{0}"),
    /**
     * 签约自动还款协议
     */
    AUTO_REPAY_PTP_MER_BIND_AGREEMENT("ptp_mer_bind_agreement", "account", "callback/auto_repay_ptp_mer_bind_agreement", "auto_repay_notify", "tuotian://sign/{0}"),
    /**
     * 慧租签约自动还款协议
     */
    HUIZU_AUTO_REPAY_PTP_MER_BIND_AGREEMENT("ptp_mer_bind_agreement", "account", "callback/no_password_invest_ptp_mer_bind_agreement", "huizu_auto_repay_notify", "tuotian://sign/{0}"),
    /**
     * 签约借记卡快捷协议
     */
    FAST_PAY_MER_BIND_AGREEMENT("ptp_mer_bind_agreement", "account", "callback/fast_pay_mer_bind_agreement", "fast_pay_notify", "tuotian://sign/{0}"),


    /**
     * 放款标的转账
     */
    LOAN_OUT_PROJECT_TRANSFER("project_transfer", "", "", "loan_out_notify", ""),
    /**
     * 验密投资标的转账
     */
    INVEST_PROJECT_TRANSFER("project_transfer", "callback/invest_project_transfer", "callback/invest_project_transfer", "invest_notify", "tuotian://invest/{0}"),
    /**
     * 直投(债权购买)超投标的转账
     */
    OVER_INVEST_PAYBACK_PROJECT_TRANSFER("project_transfer", "", "", "over_invest_payback_notify", ""),
    /**
     * 流标返款标的转账
     */
    LOAN_CANCEL_PAYBACK_PROJECT_TRANSFER("project_transfer", "", "", "loan_cancel_pay_back_notify", ""),
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
     * 债权购买标的转账
     */
    INVEST_TRANSFER_PROJECT_TRANSFER("project_transfer", "callback/invest_transfer_project_transfer", "callback/invest_transfer_project_transfer", "invest_transfer_notify", "tuotian://invest-transfer/{0}"),

    /**
     * 慧租信用贷验密还款
     */
    CREDIT_LOAN_REPAY_PROJECT_TRANSFER("project_transfer", "", "huizu/callback/credit_loan_repay_project_transfer", "credit_loan_repay_notify", "tuotian://credit-loan-repay/{0}"),

    /**
     * 慧租有密还款
     */
    HUI_ZU_PASSWORD_REPAY_PROJECT_TRANSFER("project_transfer", "", "huizu/callback/hui_zu_password_repay_project_transfer", "hz_repay_notify", "huizu://repay/{0}"),
    /**
     * 债权购买返款标的转账
     */
    INVEST_TRANSFER_PAYBACK_PROJECT_TRANSFER("project_transfer", "", "", "invest_transfer_payback_notify", ""),
    /**
     * 慧租信用贷放款
     */
    CREDIT_LOAN_OUT_PROJECT_TRANSFER("project_transfer", "", "", "credit_loan_out_notify", ""),
    /**
     * 债权购买手续费标的转账
     */
    REPAY_TRANSFER_FEE_PROJECT_TRANSFER("project_transfer", "", "", "repay_transfer_fee_notify", ""),


    /**
     * 无密投资标的转入
     */
    INVEST_PROJECT_TRANSFER_NOPWD("project_transfer_nopwd", "callback/invest_project_transfer_nopwd", "callback/invest_project_transfer_nopwd", "invest_notify", "tuotian://invest/{0}"),
    /**
     * 无密债权购买标的转入
     */
    INVEST_TRANSFER_PROJECT_TRANSFER_NOPWD("project_transfer_nopwd", "callback/invest_transfer_project_transfer_nopwd", "callback/invest_transfer_project_transfer_nopwd", "invest_transfer_notify", "tuotian://invest-transfer/{0}"),

    /**
     * 慧租有密一分钱激活账户
     */
    CREDIT_LOAN_ACTIVATE_ACCOUNT_PROJECT_TRANSFER("project_transfer", "", "huizu/callback/credit_loan_activate_account_project_transfer", "activate_account_notify", "huizu://credit_loan_activate-account/{0}"),

    /**
     * 慧租无密一分钱激活账户
     */
    CREDIT_LOAN_ACTIVATE_ACCOUNT_PROJECT_TRANSFER_NOPWD("project_transfer_nopwd", "", "huizu/callback/credit_loan_activate_account_project_transfer_nopwd", "activate_account_notify", "huizu://credit_loan_activate-account/{0}"),

    /**
     * 慧租信用贷无密还款
     */
    CREDIT_LOAN_REPAY_PROJECT_TRANSFER_NOPWD("project_transfer_nopwd", "","huizu/callback/credit_loan_repay_project_transfer_nopwd", "credit_loan_repay_notify", "tuotian://credit-loan-repay/{0}"),
    /**
     * 慧租无密还款
     */
    HUI_ZU_NO_PASSWORD_REPAY_PROJECT_TRANSFER("project_transfer_nopwd", "", "huizu/callback/hui_zu_no_password_repay_project_transfer", "hz_repay_notify", "huizu://repay/{0}"),

    /**
     * 无密还款标的转入
     */
    NORMAL_REPAY_PROJECT_TRANSFER_NOPWD("project_transfer_nopwd", "", "", "normal_repay_notify", ""),


    /**
     * 个人账户充值
     */
    MER_RECHARGE_PERSON("mer_recharge_person", "account", "callback/mer_recharge_person", "recharge_notify", "tuotian://recharge/{0}"),


    /**
     * 企业客户充值申请
     */
    MER_RECHARGE("mer_recharge", "account", "", "recharge_notify", ""),


    /**
     * 个人账户提现
     */
    CUST_WITHDRAWALS("cust_withdrawals", "account", "callback/cust_withdrawals", "withdraw_notify", "tuotian://withdraw/{0}"),


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
    /**
     * 推荐人奖励普通转账
     */
    REFERRER_REWARD_TRANSFER("transfer", "", "", "referrer_reward_notify", ""),
    /**
     * 推荐人奖励普通转账
     */
    RED_ENVELOPE_TRANSFER("transfer", "", "", "red_envelope_notify", ""),
    /**
     * 抽奖现金奖励普通转账
     */
    LOTTERY_REWARD_TRANSFER("transfer", "", "", "lottery_reward_notify", ""),
    /**
     * 代发工资普通转账
     */
    PAYROLL_TRANSFER("transfer", "", "", "payroll_notify", ""),


    /**
     * 普通会员购买转账验密
     */
    MEMBERSHIP_PRIVILEGE_PURCHASE_TRANSFER_ASYN("transfer_asyn", "", "callback/membership_privilege_purchase_transfer_asyn", "membership-privilege-purchase-notify", "tuotian://membership/{0}"),
    /**
     * 平台充值普通转账验密
     */
    SYSTEM_RECHARGE_TRANSFER_ASYN("transfer_asyn", "finance-manage/system-bill", "", "system_recharge_notify", ""),
    /**
     * 信用贷标的账户充值无密
     */
    CREDIT_LOAN_RECHARGE_TRANSFER_NOPWD("project_transfer_nopwd", "finance-manage/credit-loan-bill", "", "credit_loan_recharge_notify", ""),
    /**
     * 信用贷标的账户充值验密
     */
    CREDIT_LOAN_RECHARGE_TRANSFER("project_transfer", "finance-manage/credit-loan-bill", "", "credit_loan_recharge_notify", ""),
    /**
     * 信用贷转账给代理人
     */
    CREDIT_LOAN_AGENT_TRANSFER("project_transfer", "", "", "credit_loan_transfer_agent_notify", "");

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
