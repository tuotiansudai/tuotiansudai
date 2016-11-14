package com.tuotiansudai.mq.client.support;

import com.tuotiansudai.mq.client.model.Message;

public class RawMessage {
    public String Message;
    public String MessageId;
    public String MessageMD5;
    public String PublishTime;
    public String Subscriber;
    public String SubscriptionName;
    public String TopicName;
    public String TopicOwner;

    public Message toMessage() {
        return new Message(MessageId, Message, PublishTime, TopicName);
    }
}
