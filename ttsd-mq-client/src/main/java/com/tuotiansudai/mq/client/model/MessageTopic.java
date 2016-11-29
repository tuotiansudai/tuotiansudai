package com.tuotiansudai.mq.client.model;

public enum MessageTopic {
    CouponAssigned("CouponAssigned");

    final String topicName;

    MessageTopic(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
