package com.tuotiansudai.mq.consumer.activity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.AnnualPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.InvestRewardMapper;
import com.tuotiansudai.activity.repository.model.AnnualPrizeModel;
import com.tuotiansudai.activity.repository.model.InvestRewardModel;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class InvestSuccessActivityRewardMessageConsumerTest {

    @Autowired
    @Qualifier("investSuccessActivityRewardMessageConsumer")
    private MessageConsumer consumer;

    @MockBean
    private MQWrapperClient mqClient;

    @MockBean
    private AnnualPrizeMapper annualPrizeMapper;

    @MockBean
    private InvestRewardMapper investRewardMapper;

    @Test
    @Transactional
    public void shouldConsume() {
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        InvestSuccessMessage investSuccessMessage = buildMockedInvestSuccessMessage();

        ReflectionTestUtils.setField(consumer, "activityChristmasStartTime", new DateTime().minusDays(3).toDate());
        ReflectionTestUtils.setField(consumer, "activityChristmasEndTime", new DateTime().plusDays(3).toDate());

        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());
        when(investRewardMapper.findByMobile(anyString())).thenReturn(null);

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

    @Test
    @Transactional
    public void shouldSecondCouponIsOk() {
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        InvestSuccessMessage investSuccessMessage = buildMockedInvestAnnualSuccessMessage();

        ReflectionTestUtils.setField(consumer, "annualTime", Lists.newArrayList(DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));

        when(annualPrizeMapper.findByMobile(anyString())).thenReturn(null);
        doNothing().when(annualPrizeMapper).create(any(AnnualPrizeModel.class));
        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        verify(mqClient, times(14)).sendMessage(any(), any());

        assertEquals(MessageQueue.CouponAssigning, messageQueueCaptor.getValue());
        assertEquals("test123:359", messageCaptor.getValue());
    }

    @Test
    @Transactional
    public void shouldFirstCouponIsOk() {
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        InvestSuccessMessage investSuccessMessage = buildMockedInvestAnnualSuccessMessage();
        investSuccessMessage.getInvestInfo().setAmount(500000L);

        ReflectionTestUtils.setField(consumer, "annualTime", Lists.newArrayList(DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));

        when(annualPrizeMapper.findByMobile(anyString())).thenReturn(null);

        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        verify(mqClient, times(5)).sendMessage(any(), any());

        assertEquals(MessageQueue.CouponAssigning, messageQueueCaptor.getValue());
        assertEquals("test123:351", messageCaptor.getValue());
    }

    @Test
    @Transactional
    public void shouldSpringFestivalCompleteFirstTaskIsOk(){
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        InvestSuccessMessage investSuccessMessage = buildMockedInvestAnnualSuccessMessage();
        investSuccessMessage.getLoanDetailInfo().setActivityDesc("normal");
        investSuccessMessage.getInvestInfo().setAmount(100000L);
        investSuccessMessage.getLoanDetailInfo().setDuration(180);

        ReflectionTestUtils.setField(consumer, "springFestivalTime", Lists.newArrayList(DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));
        when(investRewardMapper.findByMobile(anyString())).thenReturn(null);
        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());
        doNothing().when(investRewardMapper).create(any());

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        verify(mqClient, times(2)).sendMessage(any(), any());
    }

    @Test
    @Transactional
    public void shouldSpringFestivalCompleteAllTaskIsOk(){
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        InvestSuccessMessage investSuccessMessage = buildMockedInvestAnnualSuccessMessage();
        investSuccessMessage.getLoanDetailInfo().setActivityDesc("normal");
        investSuccessMessage.getInvestInfo().setAmount(3000000L);
        investSuccessMessage.getLoanDetailInfo().setDuration(180);

        ReflectionTestUtils.setField(consumer, "springFestivalTime", Lists.newArrayList(DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));
        when(investRewardMapper.findByMobile(anyString())).thenReturn(null);
        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());
        doNothing().when(investRewardMapper).create(any());

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        verify(mqClient, times(12)).sendMessage(any(), any());
    }

    @Test
    @Transactional
    public void shouldSpringFestivalSecondInvestIsOk(){
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        InvestSuccessMessage investSuccessMessage = buildMockedInvestAnnualSuccessMessage();
        investSuccessMessage.getLoanDetailInfo().setActivityDesc("normal");
        investSuccessMessage.getInvestInfo().setAmount(500000L);
        investSuccessMessage.getLoanDetailInfo().setDuration(180);

        InvestRewardModel investRewardModel = new InvestRewardModel("test", "test", "test", 1000L, 0L);

        ReflectionTestUtils.setField(consumer, "springFestivalTime", Lists.newArrayList(DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));
        when(investRewardMapper.findByMobile(anyString())).thenReturn(investRewardModel);
        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());
        doNothing().when(investRewardMapper).create(any());

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        verify(mqClient, times(4)).sendMessage(any(), any());
    }

    @Test
    @Transactional
    public void shouldSpringFestivalSecondInvestCompleteAllTaskIsOk(){
        final ArgumentCaptor<MessageQueue> messageQueueCaptor = ArgumentCaptor.forClass(MessageQueue.class);
        final ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        InvestSuccessMessage investSuccessMessage = buildMockedInvestAnnualSuccessMessage();
        investSuccessMessage.getLoanDetailInfo().setActivityDesc("normal");
        investSuccessMessage.getInvestInfo().setAmount(3000000L);
        investSuccessMessage.getLoanDetailInfo().setDuration(180);

        InvestRewardModel investRewardModel = new InvestRewardModel("test", "test", "test", 1000L, 2L);

        ReflectionTestUtils.setField(consumer, "springFestivalTime", Lists.newArrayList(DateTime.now().plusDays(-1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")), DateTime.now().plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));
        when(investRewardMapper.findByMobile(anyString())).thenReturn(investRewardModel);
        doNothing().when(mqClient).sendMessage(messageQueueCaptor.capture(), messageCaptor.capture());
        doNothing().when(investRewardMapper).create(any());

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        verify(mqClient, times(8)).sendMessage(any(), any());
    }



    private  InvestSuccessMessage buildMockedInvestAnnualSuccessMessage() {
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
        loanDetailInfo.setActivityDesc("新年专享");

        userInfo.setMobile("123");
        userInfo.setUserName("123");
        userInfo.setLoginName("123");

        InvestSuccessMessage investSuccessMessage = new InvestSuccessMessage(investInfo, loanDetailInfo, userInfo);
        return investSuccessMessage;
    }
}
