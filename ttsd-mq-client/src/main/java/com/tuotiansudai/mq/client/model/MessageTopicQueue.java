package com.tuotiansudai.mq.client.model;

public enum MessageTopicQueue implements Queue {
    UserRegistered_CouponAssigning(MessageTopic.UserRegistered, "UserRegistered-CouponAssigning"),
    CouponAssigned_UserMessageSending(MessageTopic.CouponAssigned, "CouponAssigned-UserMessageSending");


    final MessageTopic topic;
    final String queueName;

    MessageTopicQueue(MessageTopic topic, String queueName) {
        this.topic = topic;
        this.queueName = queueName;
    }

    public MessageTopic getTopic() {
        return topic;
    }

    public String getQueueName() {
        return queueName;
    }
}
