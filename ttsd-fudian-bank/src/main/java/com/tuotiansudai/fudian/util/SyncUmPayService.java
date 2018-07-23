package com.tuotiansudai.fudian.util;

public enum SyncUmPayService {
    PROJECT_ACCOUNT_SEARCH("project_account_search"),
    /**
     * 商户账户查询
     */
    PTP_MER_QUERY("ptp_mer_query"),
    /**
     * 账户流水查询
     */
    TRANSEQ_SEARCH("transeq_search"),
    /**
     * 交易查询
     */
    TRANSFER_SEARCH("transfer_search"),
    /**
     * 用户查询
     */
    USER_SEARCH("user_search");


    private final String serviceName;

    SyncUmPayService(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
