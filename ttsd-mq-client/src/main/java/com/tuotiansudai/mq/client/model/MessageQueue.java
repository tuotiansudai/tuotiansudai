package com.tuotiansudai.mq.client.model;

public enum MessageQueue implements Queue {
    CouponAssigning("CouponAssigning", "{loginName}:{couponId}");

    final String queueName;
    final String messageFormat;

    MessageQueue(String queueName, String messageFormat) {
        this.queueName = queueName;
        this.messageFormat = messageFormat;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getMessageFormat() {
        return messageFormat;
    }
}
