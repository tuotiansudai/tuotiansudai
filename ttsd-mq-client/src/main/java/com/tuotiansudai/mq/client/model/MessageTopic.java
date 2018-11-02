package com.tuotiansudai.mq.client.model;

import com.tuotiansudai.etcd.ETCDConfigReader;

import java.text.MessageFormat;
import java.util.stream.Stream;

public enum MessageTopic {
    InvestSuccess("InvestSuccess",
            MessageQueue.InvestSuccess_CompletePointTask,
            MessageQueue.InvestSuccess_MembershipUpdate,
            MessageQueue.InvestSuccess_CouponUpdate,
            MessageQueue.InvestSuccess_ActivityAnnualized,
            MessageQueue.InvestSuccess_Ranking,
            MessageQueue.InvestSuccess_ActivityInvest),

    LoanOutSuccess("LoanOutSuccess",
            MessageQueue.LoanOutSuccess_GenerateRepay,
            MessageQueue.LoanOutSuccess_RewardReferrer,
            MessageQueue.LoanOutSuccess_AssignCoupon,
            MessageQueue.LoanOutSuccess_AssignAchievement,
            MessageQueue.LoanOutSuccess_GenerateAnXinContract,
            MessageQueue.LoanOutSuccess_SendCashReward,
            MessageQueue.LoanOutSuccess_SmsInvestor),

    RepaySuccess("RepaySuccess",
            MessageQueue.RepaySuccess_InvestRepay,
            MessageQueue.RepaySuccess_CouponRepay,
            MessageQueue.RepaySuccess_ExtraRateRepay);

    final String topicName;
    final MessageQueue[] queues;
    private final String ENV = ETCDConfigReader.getReader().getValue("common.environment");

    MessageTopic(String topicName, MessageQueue... queues) {
        this.topicName = topicName;
        this.queues = queues;
    }

    public String getTopicName() {
        return "PRODUCTION".equalsIgnoreCase(ENV) ? topicName : MessageFormat.format("{0}-{1}", ENV.toLowerCase(), topicName);
    }

    public MessageQueue[] getQueues() {
        return queues;
    }

    public static boolean contains(String topicName) {
        return Stream.of(MessageTopic.values()).anyMatch(t -> t.getTopicName().equals(topicName));
    }
}
