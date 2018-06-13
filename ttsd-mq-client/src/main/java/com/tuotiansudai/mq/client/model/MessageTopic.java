package com.tuotiansudai.mq.client.model;

import java.util.stream.Stream;

public enum MessageTopic {
    InvestSuccess("InvestSuccess",
            MessageQueue.Invest_Success,
            MessageQueue.Invest_MembershipUpdate,
            MessageQueue.Invest_CheckLoanFull),

    LoanFullSuccess("LoanFullSuccess",
            MessageQueue.LoanFull_Success,
            MessageQueue.LoanFull_GenerateAnXinContract),

    RepaySuccess("RepaySuccess",
            MessageQueue.RepaySuccess_InvestRepay,
            MessageQueue.RepaySuccess_CouponRepay,
            MessageQueue.RepaySuccess_ExtraRateRepay),

    RegisterBankAccount("RegisterBankAccount",
            MessageQueue.RegisterBankAccount_Success,
            MessageQueue.RegisterBankAccount_CompletePointTask),

    BindBankCard("BindBandCard",
            MessageQueue.BindBankCard_Success,
            MessageQueue.BindBankCard_CompletePointTask),

    Recharge("Recharge",
            MessageQueue.Recharge_Success,
            MessageQueue.Recharge_CompletePointTask),

    Authorization("Authorization",
            MessageQueue.Authorization_Success,
            MessageQueue.Authorization_CompletePointTask),
    ;

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
