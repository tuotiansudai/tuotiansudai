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
    UserCouponReset("UserCouponReset"),
    InvestCallback("InvestCallback"),
    TransferInvestCallback("TransferInvestCallback"),
    UserRegistered_CompletePointTask("UserRegistered-CompletePointTask"),
    GenerateReferrerRelation("GenerateReferrerRelation"),
    AccountRegistered_CompletePointTask("AccountRegistered-CompletePointTask"),
    InvestSuccess_ExperienceRepay("InvestSuccess-ExperienceRepay"),
    InvestSuccess_ActivityAnnualized("InvestSuccess-ActivityAnnualized"),
    InvestSuccess_Ranking("InvestSuccess-Ranking"),
    InvestSuccess_WeChatHelp("InvestSuccess-WeChatHelp"),
    InvestSuccess_ActivityInvest("InvestSuccess-ActivityInvest"),
    ActivityWeChatCoupon("ActivityWeChatCoupon"),
    RechargeSuccess_CompletePointTask("RechargeSuccess-CompletePointTask"),
    TurnOnNoPasswordInvest_CompletePointTask("TurnOnNoPasswordInvest-CompletePointTask"),
    ExperienceRepayCallback("ExperienceRepayCallback"),
    MembershipUpgrade_SendJpushMessage("MembershipUpgrade-SendJpushMessage"),
    LoanOutSuccess_GenerateRepay("LoanOutSuccess-GenerateRepay"),
    LoanOutSuccess_RewardReferrer("LoanOutSuccess-RewardReferrer"),
    LoanOutSuccess_AssignCoupon("LoanOutSuccess-AssignCoupon"),
    LoanOutSuccess_AssignAchievement("LoanOutSuccess-AssignAchievement"),
    LoanOutSuccess_GenerateAnXinContract("LoanOutSuccess-GenerateAnXinContract"),
    LoanOutSuccess_InviteHelpActivity("LoanOutSuccess-InviteHelpActivity"),
    LoanOutSuccess_SendCashReward("LoanOutSuccess-SendCashReward"),
    LoanOutSuccess_SuperScholarActivity("LoanOutSuccess-SuperScholarActivity"),
    RepaySuccess_InvestRepay("RepaySuccess-InvestRepay"),
    RepaySuccess_CouponRepay("RepaySuccess-CouponRepay"),
    RepaySuccess_ExtraRateRepay("RepaySuccess-ExtraRateRepay"),
    EventMessage("EventMessage"),
    ManualMessage("ManualMessage"),
    PushMessage("PushMessage"),
    EMailMessage("EMailMessage"),
    RepaySuccessInvestRepayCallback("RepaySuccessInvestRepayCallback"),
    RepaySuccessCouponRepayCallback("RepaySuccessCouponRepayCallback"),
    RepaySuccessExtraRateRepayCallback("RepaySuccessExtraRateRepayCallback"),
    AuditLog("AuditLog"),
    UserOperateLog("UserOperateLog"),
    TransferReferrerRewardCallback("TransferReferrerRewardCallback"),
    TransferRedEnvelopCallback("TransferRedEnvelopCallback"),
    LoginLog("LoginLog"),
    TransferAnxinContract("TransferAnxinContract"),
    QueryAnxinContract("QueryAnxinContract"),
    LoanOut("LoanOut"),
    LoanStartRaising("LoanStartRaising"),
    LoanStopRaising("LoanStopRaising"),
    CancelTransferApplication("CancelTransferApplication"),
    CouponSmsAssignNotify("CouponSmsAssignNotify"),
    CouponSmsExpiredNotify("CouponSmsExpiredNotify"),
    WeChatBoundNotify("WeChatBoundNotify"),
    WeChatMessageNotify("WeChatMessageNotify"),
    HuiZuRentRepayNotifyQueue("HuiZuRentRepayNotifyQueue"),
    HuiZuOpenAutoRepayQueue("HuiZuOpenAutoRepayQueue"), //慧租开通自动还款
    ExperienceAssigning("ExperienceAssigning"),
    SystemBill("SystemBill"),
    AmountTransfer("AmountTransfer"),
    CreditLoanOutQueue("CreditLoanOutQueue"), //慧租信用贷放款
    CreditLoanRepayQueue("CreditLoanRepayQueue"), //慧租信用贷还款
    CreditLoanActivateAccountQueue("CreditLoanActivateAccountQueue"),
    CreditLoanBill("CreditLoanBill"),
    Payroll("Payroll"),
    CreditLoanBalanceAlert("CreditLoanBalanceAlert"), // 信用贷账户余额不足，短信提醒（延时job）
    LuxuryStageRepayQueue("LuxuryStageRepayQueue"), // 奢侈品还款

    //fudian new queues
    BindBankCard_Success("BindBankCard-Success"),
    BindBankCard_CompletePointTask("BindBankCard-CompletePointTask"),
    UnbindBankCard_Success("UnbindBankCard-Success"),
    RegisterBankAccount_Success("RegisterBankAccount-Success"),
    RegisterBankAccount_CompletePointTask("RegisterBankAccount-CompletePointTask"),
    RegisterBankAccount_EventMessage("RegisterBankAccount-EventMessage"),
    RegisterBankAccount_PushMessage("RegisterBankAccount-PushMessage"),

    Recharge_Success("Recharge-Success"),
    Recharge_CompletePointTask("Recharge-CompletePointTask"),

    Withdraw_Success("Recharge-Success"),
    Withdraw_EventMessage("Recharge-EventMessage"),
    Withdraw_PushMessage("Recharge-PushMessage"),
    Withdraw_WeChatMessage("Recharge-WeChatMessage"),


    Invest_Success("Invest-Success"),
    Invest_CompletePointTask("Invest-CompletePointTask"),
    Invest_MembershipUpdate("Invest-MembershipUpdate"),
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
