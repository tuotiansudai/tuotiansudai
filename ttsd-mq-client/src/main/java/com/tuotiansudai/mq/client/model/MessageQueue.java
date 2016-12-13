package com.tuotiansudai.mq.client.model;

import java.util.stream.Stream;

public enum MessageQueue {
    CouponAssigning("CouponAssigning"),
    InvestCallback("InvestCallback"),
    TransferInvestCallback("TransferInvestCallback"),
    CouponAssigned_UserMessageSending("CouponAssigned-UserMessageSending"),
    UserRegistered_CompletePointTask("UserRegistered-CompletePointTask"),
    AccountRegistered_CompletePointTask("AccountRegistered-CompletePointTask"),
    InvestSuccess_CompletePointTask("InvestSuccess-CompletePointTask"),
    InvestSuccess_CouponUpdate("InvestSuccess-CouponUpdate"),
    InvestSuccess_MembershipUpdate("InvestSuccess-MembershipUpdate"),
    InvestSuccess_SendJpushMessage("InvestSuccess-SendJpushMessage"),
    RechargeSuccess_CompletePointTask("RechargeSuccess-CompletePointTask"),
    BindBankCard_CompletePointTask("BindBankCard-CompletePointTask"),
    TurnOnNoPasswordInvest_CompletePointTask("TurnOnNoPasswordInvest-CompletePointTask"),
    InvestSuccess_ActivityReward("InvestSuccess-ActivityReward"),
    MembershipUpgrade_SendJpushMessage("MembershipUpgrade-SendJpushMessage");

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
