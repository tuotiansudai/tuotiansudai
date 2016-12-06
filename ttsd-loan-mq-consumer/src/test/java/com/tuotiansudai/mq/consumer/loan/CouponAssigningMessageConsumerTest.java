package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CouponAssigningMessageConsumerTest {

    @Autowired
    @Qualifier("couponAssigningMessageConsumer")
    private MessageConsumer consumer;

    @MockBean
    private MQWrapperClient mqClient;

    @MockBean
    private CouponAssignmentService couponAssignmentService;

    @Test
    @Transactional
    public void shouldConsume() {
        String loginName = "helloworld";
        long couponId = 100011;
        long userCouponId = 100023;
        final ArgumentCaptor<Long> couponIdCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        UserCouponModel mockedUserCoupon = buildMockedUserCouponModel(userCouponId);
        when(couponAssignmentService.assign(loginNameCaptor.capture(), couponIdCaptor.capture(), any())).thenReturn(mockedUserCoupon);
        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());

        consumer.consume(String.format("%s:%d", loginName, couponId));

        verify(couponAssignmentService, times(1)).assign(any(), anyLong(), any());
        verify(mqClient, times(1)).sendMessage(any(), any());
        assertEquals(loginName, loginNameCaptor.getValue());
        assertEquals(couponId, couponIdCaptor.getValue().longValue());
        assertEquals(MessageQueue.CouponAssigned_UserMessageSending, messageQueueCaptor.getValue());
        assertEquals(String.format("UserCoupon:%d", userCouponId), messageCaptor.getValue());
    }

    private UserCouponModel buildMockedUserCouponModel(long userCouponId) {
        UserCouponModel userCouponModel = new UserCouponModel();
        userCouponModel.setId(userCouponId);
        return userCouponModel;
    }
}
