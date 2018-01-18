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
    InvestSuccess_CompletePointTask("InvestSuccess-CompletePointTask"),
    InvestSuccess_CouponUpdate("InvestSuccess-CouponUpdate"),
    InvestSuccess_MembershipUpdate("InvestSuccess-MembershipUpdate"),
    InvestSuccess_ExperienceRepay("InvestSuccess-ExperienceRepay"),
    InvestSuccess_ExperienceAssignInterestCoupon("InvestSuccess-ExperienceAssignInterestCoupon"),
    InvestSuccess_YearEndAwards("InvestSuccess-YearEndAwards"),
    InvestSuccess_CashSnowball("InvestSuccess-CashSnowball"),
    Celebration_Coupon("Celebration-Coupon"),
    RechargeSuccess_CompletePointTask("RechargeSuccess-CompletePointTask"),
    BindBankCard_CompletePointTask("BindBankCard-CompletePointTask"),
    TurnOnNoPasswordInvest_CompletePointTask("TurnOnNoPasswordInvest-CompletePointTask"),
    ExperienceRepayCallback("ExperienceRepayCallback"),
    MembershipUpgrade_SendJpushMessage("MembershipUpgrade-SendJpushMessage"),
    LoanOutSuccess_GenerateRepay("LoanOutSuccess-GenerateRepay"),
    LoanOutSuccess_RewardReferrer("LoanOutSuccess-RewardReferrer"),
    LoanOutSuccess_AssignCoupon("LoanOutSuccess-AssignCoupon"),
    LoanOutSuccess_AssignAchievement("LoanOutSuccess-AssignAchievement"),
    LoanOutSuccess_GenerateAnXinContract("LoanOutSuccess-GenerateAnXinContract"),
    LoanOutSuccess_CashSnowball("LoanOutSuccess-CashSnowball"),
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
    ExperienceAssigning("ExperienceAssigning"),
    SystemBill("SystemBill"),
    AmountTransfer("AmountTransfer"),
    CreditLoanOutQueue("CreditLoanOutQueue"), //慧租信用贷放款
    CreditLoanRepayQueue("CreditLoanRepayQueue"), //慧租信用贷还款
    CreditLoanActivateAccountQueue("CreditLoanActivateAccountQueue"),
    CreditLoanBill("CreditLoanBill"),
    Payroll("Payroll"),
    CreditLoanBalanceAlert("CreditLoanBalanceAlert"); // 信用贷账户余额不足，短信提醒（延时job）

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
