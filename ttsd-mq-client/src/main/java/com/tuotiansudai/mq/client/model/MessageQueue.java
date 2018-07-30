package com.tuotiansudai.mq.client.model;

import com.tuotiansudai.etcd.ETCDConfigReader;

import java.text.MessageFormat;

public enum MessageQueue {
    /*
    此枚举的参数为队列的实际名称
    队列名称的命名要求如下：
    1. 队列名称不能重名;
    2. 必须以英文字母或者数字开头，剩余名称可以是英文，数字，横划线；（注意，不能使用下划线）
    3. 长度不超过256个字符。
    */
    UserRegistered_CompletePointTask("UserRegistered-CompletePointTask"),
    CouponAssigning("CouponAssigning"),
    CouponSmsAssignNotify("CouponSmsAssignNotify"),
    CouponSmsExpiredNotify("CouponSmsExpiredNotify"),
    ExperienceAssigning("ExperienceAssigning"),
    GenerateReferrerRelation("GenerateReferrerRelation"),
    EventMessage("EventMessage"),
    ManualMessage("ManualMessage"),
    PushMessage("PushMessage"),
    EMailMessage("EMailMessage"),
    AuditLog("AuditLog"),
    UserOperateLog("UserOperateLog"),
    LoginLog("LoginLog"),
    TransferAnxinContract("TransferAnxinContract"),
    QueryAnxinContract("QueryAnxinContract"),
    LoanStartRaising("LoanStartRaising"),
    LoanStopRaising("LoanStopRaising"),
    CancelTransferApplication("CancelTransferApplication"),
    WeChatBoundNotify("WeChatBoundNotify"),
    WeChatMessageNotify("WeChatMessageNotify"),
    SystemBill("SystemBill"),
    AmountTransfer("AmountTransfer"),

    //ump queues
    UmpRecharge_Success("UmpRecharge-Success"),
    UmpBindCard_Success("UmpBindCard-Success"),
    UmpWithdraw_Success("UmpWithdraw-Success"),
    UmpAmountTransfer("UmpAmountTransfer"),
    UmpLoanRepay_Success("UmpLoanRepay-Success"),
    UmpRepayPayback_Success("UmpRepayPayback-Success"),
    UmpCouponRepay_Success("UmpCouponRepay-Success"),
    UmpExtraRepay_Success("UmpExtraRepay-Success"),
    UmpRepayFee_Success("UmpRepayFee-Success"),

    //fudian new queues
    BankSystemBill("BankSystemBill"),
    BindBankCard_Success("BindBankCard-Success"),
    BindBankCard_CompletePointTask("BindBankCard-CompletePointTask"),
    UnbindBankCard_Success("UnbindBankCard-Success"),

    RegisterBankAccount_Success("RegisterBankAccount-Success"),
    RegisterBankAccount_CompletePointTask("RegisterBankAccount-CompletePointTask"),

    Recharge_Success("Recharge-Success"),
    Recharge_CompletePointTask("Recharge-CompletePointTask"),

    Withdraw_Success("Withdraw-Success"),

    Invest_Success("Invest-Success"),
    Invest_CompletePointTask("Invest-CompletePointTask"),
    Invest_MembershipUpdate("Invest-MembershipUpdate"),

    Authorization_Success("Authorization-Success"),
    Authorization_CompletePointTask("Authorization-CompletePointTask"),

    LoanCreditInvest_Success("LoanCreditInvest-Success"),

    Invest_CheckLoanFull("Invest-CheckLoanFull"),

    LoanCancel_Success("LoanCancel-Success"),
    LoanFull_Success("LoanFull-Success"),
    LoanFull_RewardReferrer("LoanOutSuccess-RewardReferrer"),
    LoanFull_GenerateAnXinContract("LoanOutSuccess-GenerateAnXinContract"),

    LoanRepay_Success("LoanRepay-Success"),
    LoanCallback_Success("LoanCallback-Success"),

    QueryDownloadFiles("QueryDownloadFiles");
    ;

    private final String queueName;

    private final String ENV = ETCDConfigReader.getReader().getValue("common.environment");

    MessageQueue(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return "PRODUCTION".equalsIgnoreCase(ENV) ? queueName : MessageFormat.format("{0}-{1}", ENV.toLowerCase(), queueName);
    }
}
