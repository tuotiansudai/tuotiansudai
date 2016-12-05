package com.tuotiansudai.mq.consumer.activity;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
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

    @Test
    @Transactional
    public void shouldConsume() {
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        InvestSuccessMessage investSuccessMessage = buildMockedInvestSuccessMessage();

        ReflectionTestUtils.setField(consumer, "activityChristmasStartTime", new DateTime().minusDays(3).toDate());
        ReflectionTestUtils.setField(consumer, "activityChristmasEndTime", new DateTime().plusDays(3).toDate());

        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());

        consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));

        verify(mqClient, times(1)).sendMessage(any(), any());

        assertEquals(MessageQueue.CouponAssigning, messageQueueCaptor.getValue());
        assertEquals("test123:324", messageCaptor.getValue());
    }

    private  InvestSuccessMessage buildMockedInvestSuccessMessage() {
        InvestInfo investInfo = new InvestInfo();
        LoanDetailInfo loanDetailInfo = new LoanDetailInfo();

        investInfo.setInvestId(10000001);
        investInfo.setLoginName("test123");
        investInfo.setAmount(4000000);
        investInfo.setStatus("SUCCESS");
        investInfo.setTransferStatus("TRANSFERABLE");

        loanDetailInfo.setLoanId(200000001);
        loanDetailInfo.setActivity(true);
        loanDetailInfo.setActivityDesc("圣诞专享");

        InvestSuccessMessage investSuccessMessage = new InvestSuccessMessage(investInfo, loanDetailInfo);
        return investSuccessMessage;
    }
}
