package com.tuotiansudai.service.impl;

import com.aliyun.mns.client.*;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.*;
import com.tuotiansudai.client.MQClient;
import com.tuotiansudai.service.PullTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Vector;

@Service
public class PullTopicServiceImpl implements PullTopicService {
    @Autowired
    private MQClient mqClient;

    private static final String QUEUE_INVEST_COUPON = "investCoupon";

    private static final String QUEUE_RED_ENVELOPE = "redEnvelope";

    private static final String TOPIC_REGISTER = "register";

    @Override
    public void broadcast(String messageBody) {
        int count = 0;
        while(true){
            try {

                MNSClient client = mqClient.getMnsClient();
                Vector<String> consumerNameList = new Vector<String>();
                consumerNameList.add(QUEUE_INVEST_COUPON);
                consumerNameList.add(QUEUE_RED_ENVELOPE);
                QueueMeta queueMetaTemplate = new QueueMeta();
                queueMetaTemplate.setPollingWaitSeconds(30);
                queueMetaTemplate.setVisibilityTimeout(300l);


                TopicMeta topicMeta = new TopicMeta();
                topicMeta.setTopicName(TOPIC_REGISTER);
                CloudPullTopic pullTopic = client.createPullTopic(topicMeta, consumerNameList, true, queueMetaTemplate);
                TopicMessage tMessage = new RawTopicMessage();
                tMessage.setBaseMessageBody(messageBody);
                pullTopic.publishMessage(tMessage);
                break;
            }catch (ClientException cl){
                System.out.println("Something wrong with the network connection between client and MNS service."
                        + "Please check your network and DNS availablity.");
                if(++count >=3){
                    System.out.println("send message");
                    break;
                }

            }catch(ServiceException se){
                if(++count >=3){
                    System.out.println("send message");
                    break;
                }
            }catch(Exception e){
                System.out.println("send message");
                break;
            }

        }

    }
    @Override
    public void processSynchRedEnvelope() {
        MNSClient client = mqClient.getMnsClient();
        CloudQueue queue = client.getQueueRef(QUEUE_RED_ENVELOPE);
        while (true){
            try {
                //获取消息
                Message popMsg = queue.popMessage();
                if(popMsg != null){
                    // TODO: 16/10/17 处理生成红包逻辑
                    System.out.println(popMsg.getMessageBodyAsRawString());

                    queue.deleteMessage(popMsg.getReceiptHandle());
                }else{
                    System.out.println("message not exist in TestQueue.\n");
                }

            }catch (ClientException cl){
                System.out.println("Something wrong with the network connection between client and MNS service."
                        + "Please check your network and DNS availablity.");
                cl.printStackTrace();
            }catch (ServiceException se){
                System.out.println("MNS exception requestId:" + se.getRequestId());
                if (se.getErrorCode() != null) {
                    if (se.getErrorCode().equals("QueueNotExist"))
                    {
                        System.out.println("Queue is not exist.Please create before use");
                    } else if (se.getErrorCode().equals("TimeExpired"))
                    {
                        System.out.println("The request is time expired. Please check your local machine timeclock");
                    }
                }
            } catch (Exception e){
                System.out.println("Unknown exception happened!");
            }
        }
    }

    @Override
    public void processAsyncRedEnvelope() {
        MNSClient client = mqClient.getMnsClient();
        CloudQueue queue = client.getQueueRef(QUEUE_RED_ENVELOPE);

        // 异步获取消息
        class AsyncPopCallback<T> implements AsyncCallback<T> {
            private Message message;

            @Override
            public void onSuccess(T msg) {
                // TODO: 16/10/16 红包业务处理
                System.out.println("popMessage success");
                if (msg != null) {
                    message = (Message) msg;
                    String json = message.getMessageBodyAsRawString();
                    System.out.println("json===" + json);
                    doDelete();
                    System.out.println("end=====");
                } else {
                    System.out.println("message is null");
                }
            }

            @Override
            public void onFail(Exception ex) {
                System.out.println("AsyncPopMessage Exception: ");
                ex.printStackTrace();
            }

            public void doDelete() {

                System.out.println("===========doDelete======");
                queue.asyncDeleteMessage(message.getReceiptHandle(),
                        (AsyncPopCallback<Void>) this);
            }

        }
        AsyncPopCallback asyncPopCallback = new AsyncPopCallback();
        AsyncResult<Message> asyncPopMessage = queue.asyncPopMessage(asyncPopCallback);
        asyncPopMessage.getResult();

    }
}
