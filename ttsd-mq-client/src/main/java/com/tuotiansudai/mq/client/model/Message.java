package com.tuotiansudai.mq.client.model;

public class Message {
    private final String message;
    private final String messageId;
    private final String publishTime;
    private final String topicName;

    public Message(String messageId, String message, String publishTime, String topicName) {
        this.messageId = messageId;
        this.message = message;
        this.publishTime = publishTime;
        this.topicName = topicName;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getTopicName() {
        return topicName;
    }
}
