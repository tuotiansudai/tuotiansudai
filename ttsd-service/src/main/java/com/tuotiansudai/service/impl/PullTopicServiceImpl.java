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

    private static final String INVEST_COUPON = "investCoupon";

    private static final String RED_ENVELOPE = "redEnvelope";

    @Override
    public void broadcast(String messageBody) {
        MNSClient client = mqClient.getMnsClient();
        Vector<String> consumerNameList = new Vector<String>();
        consumerNameList.add(INVEST_COUPON);
        consumerNameList.add(RED_ENVELOPE);
        QueueMeta queueMetaTemplate = new QueueMeta();
        queueMetaTemplate.setPollingWaitSeconds(30);


        String topicName = "register";
        TopicMeta topicMeta = new TopicMeta();
        topicMeta.setTopicName(topicName);
        CloudPullTopic pullTopic = client.createPullTopic(topicMeta, consumerNameList, true, queueMetaTemplate);
        TopicMessage tMessage = new RawTopicMessage();
        tMessage.setBaseMessageBody(messageBody);
        pullTopic.publishMessage(tMessage);
    }
    @Override
    public void processSynchRedEnvelope() {
        while (true){
            try {
                MNSClient client = mqClient.getMnsClient();
                CloudQueue queue = client.getQueueRef(RED_ENVELOPE);
                //获取消息
                Message popMsg = queue.popMessage();
                if(popMsg != null){
                    // TODO: 16/10/17 处理生成红包逻辑
                    System.out.println(popMsg.getMessageBodyAsRawString());

                    queue.deleteMessage(popMsg.getReceiptHandle());
                }

            }catch (ClientException cl){

            }catch (ServiceException se){

            }
            catch (Exception e){

            }finally {

            }
        }
    }

    @Override
    public void processAsyncRedEnvelope() {
        MNSClient client = mqClient.getMnsClient();
        CloudQueue queue = client.getQueueRef(INVEST_COUPON);

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
