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
        List<String> subscriptQueueNameList = Stream.of(MessageTopicQueue.values())
                .filter(q -> q.getTopic().equals(messageTopic))
                .map(MessageTopicQueue::getQueueName).collect(Collectors.toList());
        TopicMeta topicMeta = new TopicMeta();
        topicMeta.setTopicName(messageTopic.getTopicName());
        QueueMeta queueMetaTemplate = new QueueMeta();
        mnsClient.createPullTopic(topicMeta, new Vector<>(subscriptQueueNameList), true, queueMetaTemplate);
    }

    private static void initSubscriptQueue(MNSClient mnsClient, MessageTopic messageTopic) {
        CloudTopic topic = mnsClient.getTopicRef(messageTopic.getTopicName());
        // need subscription
        List<String> subscriptQueueNameList = Stream.of(MessageTopicQueue.values())
                .filter(q -> q.getTopic().equals(messageTopic))
                .map(MessageTopicQueue::getQueueName)
                .collect(Collectors.toList());
        List<String> subscriptEndpointList = subscriptQueueNameList.stream()
                .map(topic::generateQueueEndpoint)
                .collect(Collectors.toList());
        // already subscription
        List<SubscriptionMeta> existingSubscriptions = topic.listSubscriptions("", "", 1000).getResult();
        Set<String> existingEndpoints;
        if (existingSubscriptions != null) {
            // remove invalid subscription
            existingSubscriptions.stream()
                    .filter(subscription -> !subscriptEndpointList.contains(subscription.getEndpoint()))
                    .forEach(subscription -> topic.unsubscribe(subscription.getSubscriptionName()));
            // subscript new queue
            existingEndpoints = existingSubscriptions.stream().map(SubscriptionMeta::getEndpoint).collect(Collectors.toSet());
        } else {
            existingEndpoints = new HashSet<>();
        }
        subscriptQueueNameList.stream()
                .filter(queueName -> !existingEndpoints.contains(topic.generateQueueEndpoint(queueName)))
                .map(queueName -> createQueue(mnsClient, queueName))
                .forEach(queueName -> topic.subscribe(generateSubscriptionMeta(messageTopic, topic.generateQueueEndpoint(queueName))));
    }

    private static String createQueue(MNSClient mnsClient, String queueName) {
        QueueMeta queueMeta = new QueueMeta();
        queueMeta.setQueueName(queueName);
        mnsClient.createQueue(queueMeta);
        return queueName;
    }

    private static SubscriptionMeta generateSubscriptionMeta(MessageTopic topic, String endpoint) {
        SubscriptionMeta subscriptionMeta = new SubscriptionMeta();
        subscriptionMeta.setNotifyStrategy(SubscriptionMeta.NotifyStrategy.EXPONENTIAL_DECAY_RETRY);
        subscriptionMeta.setNotifyContentFormat(SubscriptionMeta.NotifyContentFormat.JSON);
        subscriptionMeta.setTopicName(topic.getTopicName());
        subscriptionMeta.setEndpoint(endpoint);
        return subscriptionMeta;
    }
}
