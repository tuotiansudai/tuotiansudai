package com.tuotiansudai.mq.client.model;

import java.util.stream.Stream;

public enum MessageTopic {
    InvestSuccess("InvestSuccess",
            MessageQueue.InvestSuccess_CompletePointTask,
            MessageQueue.InvestSuccess_MembershipUpdate,
            MessageQueue.InvestSuccess_CouponUpdate,
            MessageQueue.InvestSuccess_ActivityReward),
    LoanOutSuccess("LoanOutSuccess",
            MessageQueue.LoanOutSuccess_GenerateRepay,
            MessageQueue.LoanOutSuccess_RewardReferrer,
            MessageQueue.LoanOutSuccess_SmsMessage,
            MessageQueue.LoanOutSuccess_AssignCoupon,
            MessageQueue.LoanOutSuccess_GenerateAnXinContract);

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
