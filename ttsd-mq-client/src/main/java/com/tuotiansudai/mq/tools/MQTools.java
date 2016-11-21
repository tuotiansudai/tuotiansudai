package com.tuotiansudai.mq.tools;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.PagingListResult;
import com.aliyun.mns.model.QueueMeta;
import com.aliyun.mns.model.SubscriptionMeta;
import com.aliyun.mns.model.TopicMeta;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.client.model.MessageTopicQueue;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MQTools {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            return;
        }
        String configFile = args[0];
        Properties properties = new Properties();
        properties.load(new FileInputStream(configFile));

        String endPoint = properties.getProperty("aliyun.mns.endpoint");
        String accessKeyId = properties.getProperty("aliyun.mns.accessKeyId");
        String accessKeySecret = properties.getProperty("aliyun.mns.accessKeySecret");

        MNSClient mnsClient = getMnsClient(endPoint, accessKeyId, accessKeySecret);

        if (mnsClient == null) {
            return;
        }

        try {
            List<String> existingTopicNameList = getExistingTopNameList(mnsClient);
            Stream.of(MessageTopic.values()).forEach(messageTopic -> {
                if (!existingTopicNameList.contains(messageTopic.getTopicName())) {
                    createTopic(mnsClient, messageTopic);
                }
                initSubscription(mnsClient, messageTopic);
            });
            mnsClient.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        } finally {
            mnsClient.close();
        }
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
        List<String> existingTopicNameList = new ArrayList<>();
        PagingListResult<TopicMeta> topicMetaPagingListResult = mnsClient.listTopic("", "", 1000);
        if (topicMetaPagingListResult != null) {
            List<TopicMeta> result = topicMetaPagingListResult.getResult();
            if (result != null) {
                existingTopicNameList = result.stream()
                        .map(TopicMeta::getTopicName)
                        .collect(Collectors.toList());
            }
        }
        return existingTopicNameList;
    }

    private static void createTopic(MNSClient mnsClient, MessageTopic messageTopic) {
        TopicMeta topicMeta = new TopicMeta();
        topicMeta.setTopicName(messageTopic.getTopicName());
        topicMeta.setLoggingEnabled(true);
        mnsClient.createTopic(topicMeta);
    }

    private static void initSubscription(MNSClient mnsClient, MessageTopic messageTopic) {
        CloudTopic topic = mnsClient.getTopicRef(messageTopic.getTopicName());
        // queue which need subscription
        List<String> subscriptionQueueNameList = Stream.of(MessageTopicQueue.values())
                .filter(q -> q.getTopic().equals(messageTopic))
                .map(MessageTopicQueue::getQueueName)
                .collect(Collectors.toList());
        List<String> subscriptionEndpointList = subscriptionQueueNameList.stream()
                .map(queueName -> generateQueueEndpoint(topic, queueName))
                .collect(Collectors.toList());

        // queue which already subscription
        List<SubscriptionMeta> existingSubscriptions = listExistingSubscriptions(topic);
        Set<String> existingEndpoints = existingSubscriptions.stream().map(SubscriptionMeta::getEndpoint).collect(Collectors.toSet());

        // remove invalid subscription
        existingSubscriptions.stream()
                .filter(subscription -> !subscriptionEndpointList.contains(subscription.getEndpoint()))
                .forEach(subscription -> topic.unsubscribe(subscription.getSubscriptionName()));

        // subscript new queue
        subscriptionQueueNameList.stream()
                .filter(queueName -> !existingEndpoints.contains(generateQueueEndpoint(topic, queueName)))
                .map(queueName -> createQueue(mnsClient, queueName))
                .forEach(queueName -> topic.subscribe(generateSubscriptionMeta(messageTopic, queueName, generateQueueEndpoint(topic, queueName))));
    }

    private static List<SubscriptionMeta> listExistingSubscriptions(CloudTopic topic) {
        PagingListResult<SubscriptionMeta> subscriptionMetaPagingListResult = topic.listSubscriptions("", "", 1000);
        if (subscriptionMetaPagingListResult != null) {
            List<SubscriptionMeta> existingSubscriptions = subscriptionMetaPagingListResult.getResult();
            if (existingSubscriptions != null) {
                return existingSubscriptions;
            }
        }
        return new ArrayList<>();
    }

    private static String createQueue(MNSClient mnsClient, String queueName) {
        QueueMeta queueMeta = new QueueMeta();
        queueMeta.setQueueName(queueName);
        queueMeta.setLoggingEnabled(true);
        mnsClient.createQueue(queueMeta);
        return queueName;
    }

    private static SubscriptionMeta generateSubscriptionMeta(MessageTopic topic, String subscriptionName, String endpoint) {
        SubscriptionMeta subscriptionMeta = new SubscriptionMeta();
        subscriptionMeta.setSubscriptionName(subscriptionName);
        subscriptionMeta.setNotifyStrategy(SubscriptionMeta.NotifyStrategy.EXPONENTIAL_DECAY_RETRY);
        subscriptionMeta.setNotifyContentFormat(SubscriptionMeta.NotifyContentFormat.JSON);
        subscriptionMeta.setTopicName(topic.getTopicName());
        subscriptionMeta.setEndpoint(endpoint);
        return subscriptionMeta;
    }

    private static String generateQueueEndpoint(CloudTopic topic, String queueName) {
        return topic.generateQueueEndpoint(queueName).replace("cn-hzjbp-a", "cn-hangzhou");
    }
}
