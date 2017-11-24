package com.tuotiansudai.mq.tools;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.PagingListResult;
import com.aliyun.mns.model.QueueMeta;
import com.aliyun.mns.model.SubscriptionMeta;
import com.aliyun.mns.model.TopicMeta;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MQMigration {
    private static final long QUEUE_MESSAGE_VISIBILITY_TIMEOUT_SECONDS = 60 * 60;//1 hour

    public static void main(String[] args) throws IOException {
        ETCDConfigReader etcdConfigReader = ETCDConfigReader.getReader();

        String enabled = etcdConfigReader.getValue("aliyun.mns.enabled");
        if (!"true".equals(enabled)) {
            return;
        }
        String endPoint = etcdConfigReader.getValue("aliyun.mns.endpoint");
        String accessKeyId = etcdConfigReader.getValue("aliyun.mns.accessKeyId");
        String accessKeySecret = etcdConfigReader.getValue("aliyun.mns.accessKeySecret");

        MNSClient mnsClient = getMnsClient(endPoint, accessKeyId, accessKeySecret);

        if (mnsClient != null) {
            initMessageQueue(mnsClient);
            initMessageTopic(mnsClient);
            initSubscription(mnsClient);
            mnsClient.close();
        }
    }

    private static void initMessageQueue(MNSClient mnsClient) {
        List<String> existingQueueNames = getExistingQueueNameList(mnsClient);
        // create new
        Stream.of(MessageQueue.values())
                .filter(messageQueue -> !existingQueueNames.contains(messageQueue.getQueueName()))
                .forEach(messageQueue -> createQueue(mnsClient, messageQueue.getQueueName()));

        // remove out
        existingQueueNames.stream()
                .filter(queueName -> !MessageQueue.contains(queueName))
                .forEach(queueName -> removeQueue(mnsClient, queueName));
    }

    private static void initMessageTopic(MNSClient mnsClient) {
        List<String> existingTopicNameList = getExistingTopNameList(mnsClient);
        // create new
        Stream.of(MessageTopic.values())
                .filter(mt -> !existingTopicNameList.contains(mt.getTopicName()))
                .forEach(mt -> createTopic(mnsClient, mt.getTopicName()));

        // remove out
        existingTopicNameList.stream()
                .filter(topicName -> !MessageTopic.contains(topicName))
                .forEach(topicName -> removeTopic(mnsClient, topicName));
    }

    private static void initSubscription(MNSClient mnsClient) {
        for (MessageTopic messageTopic : MessageTopic.values()) {
            initSubscription(mnsClient, messageTopic);
        }
    }

    private static void initSubscription(MNSClient mnsClient, MessageTopic messageTopic) {
        CloudTopic cloudTopic = mnsClient.getTopicRef(messageTopic.getTopicName());

        // queue name [equal with subscription name] which need subscribe
        Set<String> subscriptionNameList = Stream.of(messageTopic.getQueues())
                .map(MessageQueue::getQueueName)
                .collect(Collectors.toSet());

        // endpoint which already subscription
        List<String> existingSubscriptionNameList = getExistingSubscriptionNameList(cloudTopic);

        // create new
        subscriptionNameList.stream()
                .filter(subscription -> !existingSubscriptionNameList.contains(subscription))
                .forEach(subscription -> subscribe(cloudTopic, subscription, messageTopic.getTopicName(), subscription));

        // remove out
        existingSubscriptionNameList.stream()
                .filter(subscription -> !subscriptionNameList.contains(subscription))
                .forEach(subscription -> unsubscribe(cloudTopic, subscription));
    }

    private static MNSClient getMnsClient(String endPoint, String accessKeyId, String accessKeySecret) {
        try {
            CloudAccount account = new CloudAccount(accessKeyId, accessKeySecret, endPoint);
            return account.getMNSClient();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    private static List<String> getExistingTopNameList(MNSClient mnsClient) {
        PagingListResult<TopicMeta> topicMetaPagingListResult = mnsClient.listTopic("", "", 1000);
        if (topicMetaPagingListResult != null && topicMetaPagingListResult.getResult() != null) {
            List<TopicMeta> result = topicMetaPagingListResult.getResult();
            if (result != null) {
                return result.stream()
                        .map(TopicMeta::getTopicName)
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    private static List<String> getExistingQueueNameList(MNSClient mnsClient) {
        PagingListResult<QueueMeta> queueMetaPagingListResult = mnsClient.listQueue("", "", 1000);
        if (queueMetaPagingListResult != null && queueMetaPagingListResult.getResult() != null) {
            List<QueueMeta> result = queueMetaPagingListResult.getResult();
            if (result != null) {
                return result.stream()
                        .map(QueueMeta::getQueueName)
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    private static List<String> getExistingSubscriptionNameList(CloudTopic topic) {
        PagingListResult<SubscriptionMeta> subscriptionMetaPagingListResult = topic.listSubscriptions("", "", 1000);
        if (subscriptionMetaPagingListResult != null && subscriptionMetaPagingListResult.getResult() != null) {
            List<SubscriptionMeta> result = subscriptionMetaPagingListResult.getResult();
            if (result != null) {
                return result.stream()
                        .map(SubscriptionMeta::getSubscriptionName)
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    private static void createTopic(MNSClient mnsClient, String messageTopic) {
        TopicMeta topicMeta = new TopicMeta();
        topicMeta.setTopicName(messageTopic);
        topicMeta.setLoggingEnabled(true);
        mnsClient.createTopic(topicMeta);
    }

    private static void removeTopic(MNSClient mnsClient, String messageTopic) {
        mnsClient.getTopicRef(messageTopic).delete();
    }

    private static void createQueue(MNSClient mnsClient, String queueName) {
        QueueMeta queueMeta = new QueueMeta();
        queueMeta.setQueueName(queueName);
        queueMeta.setVisibilityTimeout(QUEUE_MESSAGE_VISIBILITY_TIMEOUT_SECONDS);
        queueMeta.setLoggingEnabled(true);
        mnsClient.createQueue(queueMeta);
    }

    private static void removeQueue(MNSClient mnsClient, String queueName) {
        mnsClient.getQueueRef(queueName).delete();
    }

    private static void subscribe(CloudTopic cloudTopic, String subscriptionName, String topicName, String queueName) {
        String endPoint = generateQueueEndpoint(cloudTopic, queueName);
        SubscriptionMeta subscriptionMeta = generateSubscriptionMeta(topicName, subscriptionName, endPoint);
        cloudTopic.subscribe(subscriptionMeta);
    }

    private static void unsubscribe(CloudTopic cloudTopic, String subscription) {
        cloudTopic.unsubscribe(subscription);
    }

    private static SubscriptionMeta generateSubscriptionMeta(String topicName, String subscriptionName, String endpoint) {
        SubscriptionMeta subscriptionMeta = new SubscriptionMeta();
        subscriptionMeta.setSubscriptionName(subscriptionName);
        subscriptionMeta.setNotifyStrategy(SubscriptionMeta.NotifyStrategy.EXPONENTIAL_DECAY_RETRY);
        subscriptionMeta.setNotifyContentFormat(SubscriptionMeta.NotifyContentFormat.SIMPLIFIED);
        subscriptionMeta.setTopicName(topicName);
        subscriptionMeta.setEndpoint(endpoint);
        return subscriptionMeta;
    }

    private static String generateQueueEndpoint(CloudTopic topic, String queueName) {
        return topic.generateQueueEndpoint(queueName).replace("cn-hzjbp-a", "cn-hangzhou");
    }
}
