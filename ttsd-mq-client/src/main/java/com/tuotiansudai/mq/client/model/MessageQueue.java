package com.tuotiansudai.mq.client.model;

import java.util.stream.Stream;

public enum MessageQueue {
    CouponAssigning("CouponAssigning"),
    CouponAssigned_UserMessageSending("CouponAssigned-UserMessageSending"),
    UserRegistered_CompletePointTask("UserRegistered-CompletePointTask"),
    AccountRegistered_CompletePointTask("AccountRegistered-CompletePointTask"),
    InvestSuccess_CompletePointTask("InvestSuccess-CompletePointTask"),
    RechargeSuccess_CompletePointTask("RechargeSuccess-CompletePointTask"),
    BindBankCard_CompletePointTask("BindBankCard-CompletePointTask");

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
