package com.tuotiansudai.api.dto.v1_0;

public enum UmPayFrontService {
    /**
     * 绑定银行卡
     */
    PTP_MER_BIND_CARD("ptp_mer_bind_card"),
    /**
     * 换卡
     */
    PTP_MER_REPLACE_CARD("ptp_mer_replace_card"),
    /**
     * 签约协议
     */
    PTP_MER_BIND_AGREEMENT("ptp_mer_bind_agreement"),

    PTP_MER_NO_PASSWORD_INVEST("ptp_mer_no_password_invest"),

    /**
     * 个人账户充值
     */
    MER_RECHARGE_PERSON("mer_recharge_person"),
    /**
     * 个人账户提现
     */
    CUST_WITHDRAWALS("cust_withdrawals"),

    /**
     * 投资
     */
    PROJECT_TRANSFER_INVEST("project_transfer_invest");

    private final String serviceName;

    UmPayFrontService(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
