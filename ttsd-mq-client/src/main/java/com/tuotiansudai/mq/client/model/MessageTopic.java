package com.tuotiansudai.mq.client.model;

import java.util.stream.Stream;

public enum MessageTopic {
    InvestSuccess("InvestSuccess",
            MessageQueue.Invest_Success,
            MessageQueue.Invest_CompletePointTask,
            MessageQueue.Invest_MembershipUpdate),

    LoanOutSuccess("LoanOutSuccess",
            MessageQueue.LoanOutSuccess_GenerateRepay,
            MessageQueue.LoanOutSuccess_RewardReferrer,
            MessageQueue.LoanOutSuccess_AssignCoupon,
            MessageQueue.LoanOutSuccess_AssignAchievement,
            MessageQueue.LoanOutSuccess_GenerateAnXinContract,
            MessageQueue.LoanOutSuccess_SendCashReward,
            MessageQueue.LoanOutSuccess_InviteHelpActivity,
            MessageQueue.LoanOutSuccess_SuperScholarActivity),

    RepaySuccess("RepaySuccess",
            MessageQueue.RepaySuccess_InvestRepay,
            MessageQueue.RepaySuccess_CouponRepay,
            MessageQueue.RepaySuccess_ExtraRateRepay),

    RegisterBankAccount("RegisterBankAccount",
            MessageQueue.RegisterBankAccount_Success,
            MessageQueue.RegisterBankAccount_CompletePointTask,
            MessageQueue.RegisterBankAccount_EventMessage,
            MessageQueue.RegisterBankAccount_PushMessage),

    BindBankCard("BindBandCard",
            MessageQueue.BindBankCard_Success,
            MessageQueue.BindBankCard_CompletePointTask),

    Withdraw("Withdraw",
            MessageQueue.Withdraw_Success,
            MessageQueue.Withdraw_EventMessage,
            MessageQueue.Withdraw_PushMessage
    ),

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
