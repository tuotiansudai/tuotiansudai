package com.tuotiansudai.mq.client.model;

import java.util.stream.Stream;

public enum MessageQueue {
    /*
    此枚举的参数为队列的实际名称
    队列名称的命名要求如下：
    1. 队列名称不能重名;
    2. 必须以英文字母或者数字开头，剩余名称可以是英文，数字，横划线；（注意，不能使用下划线）
    3. 长度不超过256个字符。
    */
    CouponAssigning("CouponAssigning"),
    ExperienceAssigning("ExperienceAssigning"),

    UserCouponReset("UserCouponReset"),
    GenerateReferrerRelation("GenerateReferrerRelation"),
    InvestSuccess_ExperienceRepay("InvestSuccess-ExperienceRepay"),
    TurnOnNoPasswordInvest_CompletePointTask("TurnOnNoPasswordInvest-CompletePointTask"),

    ExperienceRepayCallback("ExperienceRepayCallback"),

    EventMessage("EventMessage"),
    ManualMessage("ManualMessage"),
    PushMessage("PushMessage"),
    EMailMessage("EMailMessage"),

    AuditLog("AuditLog"),
    UserOperateLog("UserOperateLog"),
    TransferReferrerRewardCallback("TransferReferrerRewardCallback"),
    TransferRedEnvelopCallback("TransferRedEnvelopCallback"),
    LoginLog("LoginLog"),

    TransferAnxinContract("TransferAnxinContract"),
    QueryAnxinContract("QueryAnxinContract"),

    LoanStartRaising("LoanStartRaising"),
    LoanStopRaising("LoanStopRaising"),

    CancelTransferApplication("CancelTransferApplication"),
    CouponSmsAssignNotify("CouponSmsAssignNotify"),
    CouponSmsExpiredNotify("CouponSmsExpiredNotify"),

    WeChatBoundNotify("WeChatBoundNotify"),
    WeChatMessageNotify("WeChatMessageNotify"),

    SystemBill("SystemBill"),
    AmountTransfer("AmountTransfer"),
    Payroll("Payroll"),
    CreditLoanBalanceAlert("CreditLoanBalanceAlert"), // 信用贷账户余额不足，短信提醒（延时job）

    //fudian new queues
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

    LoanFull_Success("LoanFull-Success"),
    LoanFull_RewardReferrer("LoanOutSuccess-RewardReferrer"),
    LoanFull_GenerateAnXinContract("LoanOutSuccess-GenerateAnXinContract"),

    LoanRepay_Success("LoanRepay-Success"),
    LoanCallback_Success("LoanCallback-Success"),

    RepaySuccess_InvestRepay("RepaySuccess-InvestRepay"),
    RepaySuccess_CouponRepay("RepaySuccess-CouponRepay"),
    RepaySuccess_ExtraRateRepay("RepaySuccess-ExtraRateRepay"),
    ;

    private final String queueName;

    MessageQueue(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public static boolean contains(String queueName) {
        return Stream.of(MessageQueue.values()).anyMatch(q -> q.getQueueName().equals(queueName));
    }
}
