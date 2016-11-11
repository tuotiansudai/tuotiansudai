package com.tuotian.mq.consumer.loan;

import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.mq.client.MQClient;
import com.tuotiansudai.mq.client.model.Message;
import com.tuotiansudai.mq.client.model.MessageTopicQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class LoanMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(MQClient.class);

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Override
    public MessageTopicQueue queue() {
        return MessageTopicQueue.UserRegistered_CouponAssigning;
    }

    @Transactional
    @Override
    public void consume(Message message) {
        String mobile = message.getMessage();
        couponAssignmentService.assignUserCoupon(mobile,
                Arrays.asList(UserGroup.ALL_USER, UserGroup.NEW_REGISTERED_USER, UserGroup.NOT_ACCOUNT_NOT_INVESTED_USER));
    }
}
