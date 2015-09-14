package com.tuotiansudai.paywrapper.repository.model;

public enum UmPayService {
    /**
     * 开户
     */
    MER_REGISTER_PERSON("mer_register_person"),
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
    /**
     * 解约授权
     */
    MER_UNBIND_AGREEMENT("mer_unbind_agreement"),
    /**
     * 发送短信验证码
     */
    MER_SEND_SMS_VERIFICATION("mer_send_sms_verification"),
    /**
     * 验证码个人用户注册
     */
    MER_REGISTER_PERSON_VERI("mer_register_person_veri"),
    /**
     * 发标
     */
    MER_BIND_PROJECT("mer_bind_project"),
    /**
     * 标的更新
     */
    MER_UPDATE_PROJECT("mer_update_project"),
    /**
     * 标的转账(投资,转让等等)
     */
    PROJECT_TRANSFER("project_transfer"),
    /**
     * 无密标的转入
     */
    PROJECT_TRANSFER_NOPWD("project_transfer_nopwd"),
    /**
     * 个人账户充值
     */
    MER_RECHARGE_PERSON("mer_recharge_person"),
    /**
     * 企业客户充值申请
     */
    MER_RECHARGE("mer_recharge"),
    /**
     * 个人客户无密充值
     */
    MER_RECHARGE_PERSON_NOPWD("mer_recharge_person_nopwd"),
    /**
     * 个人账户提现
     */
    CUST_WITHDRAWALS("cust_withdrawals"),
    /**
     * 企业客户提现
     */
    MER_WITHDRAWALS("mer_withdrawals"),
    /**
     * 重置交易密码
     */
    MER_SEND_SMS_PWD("mer_send_sms_pwd"),
    /**
     * 普通转账
     */
    TRANSFER("transfer"),
    /**
     * 普通转账验密接口
     */
    TRANSFER_ASYN("transfer_asyn"),
    /**
     * 交易查询
     */
    SEARCH_TRANSFER("transfer_search"),
    /**
     * 用户查询
     */
    SEARCH_USER("user_search"),
    /**
     * 标的查询
     */
    SEARCH_PROJECT_ACCOUNT("project_account_search"),
    /**
     * 账户流水查询
     */
    SEARCH_TRANSEQ("transeq_search"),
    /**
     * 商户账户查询
     */
    QUERY_PTP_MER("ptp_mer_query"),
    /**
     * 对账文件下载
     */
    DOWNLOAD_SETTLE_FILE_P("download_settle_file_p"),
    /**
     * 绑卡换卡结果通知
     */
    NOTIFY_MER_BIND_CARD("mer_bind_card_notify"),
    /**
     * 绑卡换卡申请通知商户
     */
    NOTIFY_MER_BIND_CARD_APPLY("mer_bind_card_apply_notify"),
    /**
     * 签约免密协议结果通知商户
     */
    NOTIFY_MER_BIND_AGREEMENT("mer_bind_agreement_notify"),
    /**
     * 解约授权结果通知商户
     */
    NOTIFY_MER_UNBIND_AGREEMENT("mer_unbind_agreement_notify"),
    /**
     * 标的交易通知
     */
    NOTIFY_PROJECT_TRANSFER("project_transfer_notify"),
    /**
     * 充值结果通知
     */
    NOTIFY_RECHARGE("recharge_notify"),
    /**
     * 提现申请通知
     */
    NOTIFY_WITHDRAW_APPLY("withdraw_apply_notify"),
    /**
     * 普通转账通知
     */
    NOTIFY_TRANSFER("transfer_notify");

    private final String serviceName;

    UmPayService(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
