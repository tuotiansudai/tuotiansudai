package com.tuotiansudai.mq.client.model;

import java.util.stream.Stream;

public enum MessageTopic {
    InvestSuccess("InvestSuccess",
            MessageQueue.InvestSuccess_CompletePointTask,
            MessageQueue.InvestSuccess_MembershipUpdate,
            MessageQueue.InvestSuccess_CouponUpdate,
            MessageQueue.InvestSuccess_ActivityReward,
            MessageQueue.InvestSuccess_ExperienceAssignInterestCoupon,
            MessageQueue.InvestSuccess_DragonBoat,
            MessageQueue.InvestSuccess_WechatLottery,
            MessageQueue.InvestSuccess_HouseDecorate,
            MessageQueue.InvestSuccess_SchoolSeason,
            MessageQueue.InvestSuccess_NationalMidAutumn,
            MessageQueue.InvestSuccess_ZeroShopping,
            MessageQueue.InvestSuccess_YearEndAwards),

    LoanOutSuccess("LoanOutSuccess",
            MessageQueue.LoanOutSuccess_GenerateRepay,
            MessageQueue.LoanOutSuccess_RewardReferrer,
            MessageQueue.LoanOutSuccess_AssignCoupon,
            MessageQueue.LoanOutSuccess_AssignAchievement,
            MessageQueue.LoanOutSuccess_AssignAchievementCelebration,
            MessageQueue.LoanOutSuccess_GenerateAnXinContract,
            MessageQueue.LoanOutSuccess_NationalMidAutumn,
            MessageQueue.LoanOutSuccess_DoubleEleven),

    RepaySuccess("RepaySuccess",
            MessageQueue.RepaySuccess_InvestRepay,
            MessageQueue.RepaySuccess_CouponRepay,
            MessageQueue.RepaySuccess_ExtraRateRepay);

    final String topicName;
    final MessageQueue[] queues;

    MessageTopic(String topicName, MessageQueue... queues) {
        this.topicName = topicName;
        this.queues = queues;
    }

    public String getTopicName() {
        return topicName;
    }

    public MessageQueue[] getQueues() {
        return queues;
    }

    public static boolean contains(String topicName) {
        return Stream.of(MessageTopic.values()).anyMatch(t -> t.getTopicName().equals(topicName));
    }
}
