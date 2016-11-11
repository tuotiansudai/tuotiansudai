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
import java.util.*;
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
                if (existingTopicNameList.contains(messageTopic.getTopicName())) {
                    initSubscriptQueue(mnsClient, messageTopic);
                } else {
                    createPullTopic(mnsClient, messageTopic);
                }
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
        System.out.println(endPoint);
        try {
            System.out.println("59");
            CloudAccount account = new CloudAccount(accessKeyId, accessKeySecret, endPoint);
            System.out.println("61");
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

    private static void createPullTopic(MNSClient mnsClient, MessageTopic messageTopic) {
        System.out.println("85");
        List<String> subscriptQueueNameList = Stream.of(MessageTopicQueue.values())
                .filter(q -> q.getTopic().equals(messageTopic))
                .map(MessageTopicQueue::getQueueName).collect(Collectors.toList());
        TopicMeta topicMeta = new TopicMeta();
        topicMeta.setTopicName(messageTopic.getTopicName());
        QueueMeta queueMetaTemplate = new QueueMeta();
        System.out.println("92");
        mnsClient.createPullTopic(topicMeta, new Vector<>(subscriptQueueNameList), true, queueMetaTemplate);
        System.out.println("93");
    }

    private static void initSubscriptQueue(MNSClient mnsClient, MessageTopic messageTopic) {
        System.out.println("96:" + messageTopic.getTopicName());
        CloudTopic topic = mnsClient.getTopicRef(messageTopic.getTopicName());
        // need subscription
        List<String> subscriptQueueNameList = Stream.of(MessageTopicQueue.values())
                .filter(q -> q.getTopic().equals(messageTopic))
                .map(MessageTopicQueue::getQueueName)
                .collect(Collectors.toList());
        List<String> subscriptEndpointList = subscriptQueueNameList.stream()
                .map(topic::generateQueueEndpoint)
                .collect(Collectors.toList());
        System.out.println("106:" + messageTopic.getTopicName());
        // already subscription
        List<SubscriptionMeta> existingSubscriptions = listExistingSubscriptions(topic);
        // remove invalid subscription
        existingSubscriptions.stream()
                .filter(subscription -> !subscriptEndpointList.contains(subscription.getEndpoint()))
                .forEach(subscription -> topic.unsubscribe(subscription.getSubscriptionName()));
        System.out.println("113:" + messageTopic.getTopicName());
        // subscript new queue
        Set<String> existingEndpoints = existingSubscriptions.stream().map(SubscriptionMeta::getEndpoint).collect(Collectors.toSet());
        subscriptQueueNameList.stream()
                .filter(queueName -> !existingEndpoints.contains(topic.generateQueueEndpoint(queueName)))
                .map(queueName -> createQueue(mnsClient, queueName))
                .forEach(queueName -> topic.subscribe(generateSubscriptionMeta(messageTopic, topic.generateQueueEndpoint(queueName))));
    }

    private static List<SubscriptionMeta> listExistingSubscriptions(CloudTopic topic) {
        System.out.println(123);
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
        System.out.println(135);
        QueueMeta queueMeta = new QueueMeta();
        queueMeta.setQueueName(queueName);
        mnsClient.createQueue(queueMeta);
        return queueName;
    }

    private static SubscriptionMeta generateSubscriptionMeta(MessageTopic topic, String endpoint) {
        System.out.println(142);
        System.out.printf(endpoint);
        SubscriptionMeta subscriptionMeta = new SubscriptionMeta();
        subscriptionMeta.setNotifyStrategy(SubscriptionMeta.NotifyStrategy.EXPONENTIAL_DECAY_RETRY);
        subscriptionMeta.setNotifyContentFormat(SubscriptionMeta.NotifyContentFormat.JSON);
        subscriptionMeta.setTopicName(topic.getTopicName());
        subscriptionMeta.setEndpoint(endpoint);
        return subscriptionMeta;
    }
}
