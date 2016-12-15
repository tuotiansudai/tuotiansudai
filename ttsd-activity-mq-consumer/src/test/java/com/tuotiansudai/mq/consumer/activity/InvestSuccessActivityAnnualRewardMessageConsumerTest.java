package com.tuotiansudai.mq.consumer.activity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.AnnualPrizeMapper;
import com.tuotiansudai.activity.repository.model.AnnualPrizeModel;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
import com.tuotiansudai.message.UserInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
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

import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class InvestSuccessActivityAnnualRewardMessageConsumerTest {

    @Autowired
    @Qualifier("investSuccessActivityAnnualRewardMessageConsumer")
    private MessageConsumer consumer;

    @MockBean
    private MQWrapperClient mqClient;

    @MockBean
    private AnnualPrizeMapper annualPrizeMapper;

    @Test
    @Transactional
    public void shouldSecondCouponIsOk() {
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        InvestSuccessMessage investSuccessMessage = buildMockedInvestSuccessMessage();

        ReflectionTestUtils.setField(consumer, "annualTime", Lists.newArrayList(DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));

        when(annualPrizeMapper.find(anyString())).thenReturn(null);

        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        verify(mqClient, times(2)).sendMessage(any(), any());

        assertEquals(MessageQueue.CouponAssigning, messageQueueCaptor.getValue());
        assertEquals("test123:323", messageCaptor.getValue());
    }

    @Test
    @Transactional
    public void shouldFirstCouponIsOk() {
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        InvestSuccessMessage investSuccessMessage = buildMockedInvestSuccessMessage();
        investSuccessMessage.getInvestInfo().setAmount(500000L);

        ReflectionTestUtils.setField(consumer, "annualTime", Lists.newArrayList(DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));

        when(annualPrizeMapper.find(anyString())).thenReturn(null);

        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        verify(mqClient, times(1)).sendMessage(any(), any());

        assertEquals(MessageQueue.CouponAssigning, messageQueueCaptor.getValue());
        assertEquals("test123:323", messageCaptor.getValue());
    }

    @Test
    @Transactional
    public void shouldAgainInvestSendCouponIsOk() {
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        InvestSuccessMessage investSuccessMessage = buildMockedInvestSuccessMessage();
        investSuccessMessage.getInvestInfo().setAmount(400000L);

        ReflectionTestUtils.setField(consumer, "annualTime", Lists.newArrayList(DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));

        AnnualPrizeModel annualPrizeModel = new AnnualPrizeModel();
        annualPrizeModel.setInvestAmount(100000L);
        when(annualPrizeMapper.find(anyString())).thenReturn(annualPrizeModel);
        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        verify(mqClient, times(1)).sendMessage(any(), any());

        assertEquals(MessageQueue.CouponAssigning, messageQueueCaptor.getValue());
        assertEquals("test123:323", messageCaptor.getValue());
    }

    private  InvestSuccessMessage buildMockedInvestSuccessMessage() {
        InvestInfo investInfo = new InvestInfo();
        LoanDetailInfo loanDetailInfo = new LoanDetailInfo();
        UserInfo userInfo = new UserInfo();

        investInfo.setInvestId(10000001);
        investInfo.setLoginName("test123");
        investInfo.setAmount(100000000);
        investInfo.setStatus("SUCCESS");
        investInfo.setTransferStatus("TRANSFERABLE");

        loanDetailInfo.setLoanId(200000001);
        loanDetailInfo.setActivity(true);
        loanDetailInfo.setActivityDesc("元旦专享");

        userInfo.setMobile("123");
        userInfo.setUserName("123");
        userInfo.setLoginName("123");

        InvestSuccessMessage investSuccessMessage = new InvestSuccessMessage(investInfo, loanDetailInfo, userInfo);
        return investSuccessMessage;
    }
}
