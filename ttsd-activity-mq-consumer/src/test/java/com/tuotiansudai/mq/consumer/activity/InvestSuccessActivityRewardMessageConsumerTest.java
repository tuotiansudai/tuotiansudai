package com.tuotiansudai.mq.consumer.activity;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.TransferStatus;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class InvestSuccessActivityRewardMessageConsumerTest {

    @Autowired
    @Qualifier("investSuccessActivityRewardMessageConsumer")
    private MessageConsumer consumer;

    @MockBean
    private MQWrapperClient mqClient;

    @MockBean
    private CouponAssignmentService couponAssignmentService;

    @MockBean
    private InvestMapper investMapper;

    @MockBean
    private LoanDetailsMapper loanDetailsMapper;

    @Test
    @Transactional
    public void shouldConsume() {
        long investId = 1000000001;
        long loanId = 200000001;
        long userCouponId = 100023;
        final ArgumentCaptor<Long> investIdCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<Long> loanIdCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        ReflectionTestUtils.setField(consumer, "activityChristmasStartTime", new DateTime().minusDays(3).toDate());
        ReflectionTestUtils.setField(consumer, "activityChristmasEndTime", new DateTime().plusDays(3).toDate());
        InvestModel mockedInvestModel = buildMockedInvestModel(investId, loanId);
        LoanDetailsModel mockedLoanDetailsModel = buildMockedLoanDetailsModel(loanId);
        UserCouponModel mockedUserCoupon = buildMockedUserCouponModel(userCouponId);

        when(investMapper.findById(investIdCaptor.capture())).thenReturn(mockedInvestModel);
        when(loanDetailsMapper.getByLoanId(loanIdCaptor.capture())).thenReturn(mockedLoanDetailsModel);
        when(couponAssignmentService.assign(loginNameCaptor.capture(), anyLong(), any())).thenReturn(mockedUserCoupon);
        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());

        consumer.consume(String.valueOf(investId));

        verify(couponAssignmentService, times(1)).assign(any(), anyLong(), any());
        verify(mqClient, times(1)).sendMessage(any(), any());

        assertEquals("test123", loginNameCaptor.getValue());
        assertEquals(investId, investIdCaptor.getValue().longValue());
        assertEquals(loanId, loanIdCaptor.getValue().longValue());
        assertEquals(MessageQueue.InvestSuccess_ActivityReward, messageQueueCaptor.getValue());
        assertEquals(String.format("UserCoupon:%d", userCouponId), messageCaptor.getValue());
    }

    private UserCouponModel buildMockedUserCouponModel(long userCouponId) {
        UserCouponModel userCouponModel = new UserCouponModel();
        userCouponModel.setId(userCouponId);
        return userCouponModel;
    }

    private InvestModel buildMockedInvestModel(long investId, long loanId) {
        InvestModel investModel = new InvestModel();
        investModel.setId(investId);
        investModel.setLoginName("test123");
        investModel.setLoanId(loanId);
        investModel.setAmount(3200000);
        investModel.setTransferStatus(TransferStatus.NONTRANSFERABLE);
        return investModel;
    }

    private LoanDetailsModel buildMockedLoanDetailsModel(long loanId) {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setLoanId(loanId);
        loanDetailsModel.setActivity(true);
        loanDetailsModel.setActivityDesc("圣诞专享");
        return loanDetailsModel;
    }
}
