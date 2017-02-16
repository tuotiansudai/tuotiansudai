package com.tuotiansudai.util;

import java.text.MessageFormat;

public enum MobileFrontCallbackService {
    /**
     * 绑定银行卡
     */
    PTP_MER_BIND_CARD("ptp_mer_bind_card", "绑卡申请成功", "tuotian://bindcard/{0}"),
    /**
     * 换卡
     */
    PTP_MER_REPLACE_CARD("ptp_mer_replace_card", "换卡申请成功", "tuotian://changecard/{0}"),
    /**
     * 签约协议
     */
    PTP_MER_BIND_AGREEMENT("ptp_mer_bind_agreement", "签约成功" , "tuotian://sign/{0}"),
    /**
     * 无密投资协议
     */
    PTP_MER_NO_PASSWORD_INVEST("ptp_mer_no_password_invest", "恭喜您开通无密投资成功", "tuotian://nopasswordinvest/{0}"),
    /**
     * 个人账户充值
     */
    MER_RECHARGE_PERSON("mer_recharge_person", "充值成功", "tuotian://recharge/{0}"),
    /**
     * 个人账户提现
     */
    CUST_WITHDRAWALS("cust_withdrawals", "提现申请成功", "tuotian://withdraw/{0}"),
    /**
     * 投资
     */
    PROJECT_TRANSFER_INVEST("project_transfer_invest", "投资成功", "tuotian://invest/{0}"),
    /**
     * 债权转让
     */
    PROJECT_TRANSFER_TRANSFER("project_transfer_transfer", "转让成功", "tuotian://invest-transfer/{0}"),
    /**
     * 无密投资
     */
    PROJECT_TRANSFER_NOPASSWORD_INVEST("project_transfer_no_password_invest", "投资成功", "tuotian://invest/{0}"),
    /**
     * 无密债权转让
     */
    PROJECT_TRANSFER_NOPASSWORD_TRANSFER("project_transfer_no_password_transfer", "转让成功", "tuotian://invest-transfer/{0}"),
    /**
     * 购买特权会员
     */
    MEMBERSHIP_PRIVILEGE_PURCHASE("membership_privilege_purchase", "购买成功", "tuotian://membership/{0}");

    private final String serviceName;

    private final String message;

    private final String confirmUrlTemplate;

    MobileFrontCallbackService(String serviceName, String message, String confirmUrlTemplate) {
        this.serviceName = serviceName;
        this.message = message;
        this.confirmUrlTemplate = confirmUrlTemplate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getConfirmUrl(boolean isSuccess) {
        return MessageFormat.format(confirmUrlTemplate, isSuccess ? "success" : "fail");
    }

    public String getMessage() {
        return message;
    }

    public static MobileFrontCallbackService getService(String service){

        for(MobileFrontCallbackService mobileFrontCallbackService : MobileFrontCallbackService.values() ){
            if(mobileFrontCallbackService.getServiceName().equalsIgnoreCase(service)){
                return mobileFrontCallbackService;
            }
        }
        return null;
    }

}
