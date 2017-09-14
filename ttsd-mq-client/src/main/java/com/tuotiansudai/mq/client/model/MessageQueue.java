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
    ObtainPoint("ObtainPoint"),
    InvestSuccess_CompletePointTask("InvestSuccess-CompletePointTask"),
    InvestSuccess_CouponUpdate("InvestSuccess-CouponUpdate"),
    InvestSuccess_MembershipUpdate("InvestSuccess-MembershipUpdate"),
    InvestSuccess_ActivityReward("InvestSuccess-ActivityReward"),
    InvestSuccess_MidSummer("InvestSuccess-MidSummer"),
    InvestSuccess_WechatLottery("InvestSuccess-WechatLottery"),
    InvestSuccess_ExperienceRepay("InvestSuccess-ExperienceRepay"),
    InvestSuccess_ExperienceAssignInterestCoupon("InvestSuccess-ExperienceAssignInterestCoupon"),
    InvestSuccess_DragonBoat("InvestSuccess-DragonBoat"),
    InvestSuccess_HouseDecorate("InvestSuccess-HouseDecorate"),
    InvestSuccess_SchoolSeason("InvestSuccess-SchoolSeason"),
    Celebration_Coupon("Celebration-Coupon"),
    RechargeSuccess_CompletePointTask("RechargeSuccess-CompletePointTask"),
    BindBankCard_CompletePointTask("BindBankCard-CompletePointTask"),
    TurnOnNoPasswordInvest_CompletePointTask("TurnOnNoPasswordInvest-CompletePointTask"),
    ExperienceRepayCallback("ExperienceRepayCallback"),
    MembershipUpgrade_SendJpushMessage("MembershipUpgrade-SendJpushMessage"),
    LoanOutSuccess_GenerateRepay("LoanOutSuccess-GenerateRepay"),
    LoanOutSuccess_RewardReferrer("LoanOutSuccess-RewardReferrer"),
    LoanOutSuccess_AssignCoupon("LoanOutSuccess-AssignCoupon"),
    LoanOutSuccess_AssignAchievementCelebration("LoanOutSuccess-AssignAchievementCelebration"),
    LoanOutSuccess_AssignAchievement("LoanOutSuccess-AssignAchievement"),
    LoanOutSuccess_GenerateAnXinContract("LoanOutSuccess-GenerateAnXinContract"),
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
    ExperienceAssigning("ExperienceAssigning"),
    DragonBoatShareLoginTransfer("DragonBoatShareLoginTransfer"), // 端午节活动分享落地页老用户登录后，消息中转（为了获取用户信息）
    DragonBoatShareLogin("DragonBoatShareLogin"), // 端午节活动分享落地页老用户登录后，发放10元红包，记录老用户邀请数量
    DragonBoatPKEndSendExperience("DragonBoatPKEndSendExperience"), // 端午节PK活动结束后，给PK用户发体验金，
    DragonBoatChampagneEndSendCoupon("DragonBoatChampagneEndSendCoupon"), // 端午节香槟塔活动结束后，给投资用户发优惠券
    InvestSuccess_InvestHeroRanking("InvestSuccess-InvestHeroRanking"); //周年庆 英雄排行榜，用户投资，消息中转（为了获取用户信息）

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
