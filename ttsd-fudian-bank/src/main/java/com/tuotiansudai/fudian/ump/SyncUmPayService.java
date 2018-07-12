package com.tuotiansudai.fudian.ump;

public enum SyncUmPayService {
    /**
     * 重置交易密码
     */
    MER_SEND_SMS_PWD("mer_send_sms_pwd"),
    /**
     * 发标
     */
    MER_BIND_PROJECT("mer_bind_project"),
    /**
     * 标的更新
     */
    MER_UPDATE_PROJECT("mer_update_project"),
    /**
     * 标的查询
     */
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
