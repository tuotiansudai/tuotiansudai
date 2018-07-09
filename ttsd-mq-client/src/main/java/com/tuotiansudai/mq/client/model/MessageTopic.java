package com.tuotiansudai.mq.client.model;

import com.tuotiansudai.etcd.ETCDConfigReader;

import java.text.MessageFormat;

public enum MessageTopic {
    InvestSuccess("InvestSuccess",
            MessageQueue.Invest_Success,
            MessageQueue.Invest_MembershipUpdate,
            MessageQueue.Invest_CheckLoanFull),

    LoanFullSuccess("LoanFullSuccess",
            MessageQueue.LoanFull_Success,
            MessageQueue.LoanFull_GenerateAnXinContract),

    BindBankCard("BindBandCard",
            MessageQueue.BindBankCard_Success,
            MessageQueue.BindBankCard_CompletePointTask),

    Recharge("Recharge",
            MessageQueue.Recharge_Success,
            MessageQueue.Recharge_CompletePointTask),

    Authorization("Authorization",
            MessageQueue.Authorization_Success,
            MessageQueue.Authorization_CompletePointTask),
    ;

    final String topicName;
    final MessageQueue[] queues;
    private final String ENV = ETCDConfigReader.getReader().getValue("common.environment");

    MessageTopic(String topicName, MessageQueue... queues) {
        this.topicName = topicName;
        this.queues = queues;
    }

    public String getTopicName() {
        return "PRODUCTION".equalsIgnoreCase(ENV) ? topicName : MessageFormat.format("{0}-{1}", ENV.toLowerCase(), topicName);
    }

    public MessageQueue[] getQueues() {
        return queues;
    }
}
