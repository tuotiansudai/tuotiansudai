package com.tuotiansudai.mq.consumer.user;

import com.tuotiansudai.membership.service.MembershipInvestService;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
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
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class InvestSuccessMembershipUpdateMessageConsumerTest {

    @MockBean
    private MembershipInvestService membershipInvestService;

    @MockBean
    private InvestMapper investMapper;

    @Autowired
    @Qualifier("investSuccessMembershipUpdateMessageConsumer")
    private MessageConsumer consumer;


    @Test
    @Transactional
    public void shouldConsume() {

        InvestModel investModel = new InvestModel();
        investModel.setLoginName("loginName");
        investModel.setAmount(1000L);
        investModel.setId(1);

        final ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<Long> amountCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<Long> investIdCaptor = ArgumentCaptor.forClass(Long.class);

        when(investMapper.findById(anyLong())).thenReturn(investModel);

        doNothing().when(membershipInvestService).afterInvestSuccess(loginNameCaptor.capture(), amountCaptor.capture(), investIdCaptor.capture());

        consumer.consume("1");
        assertEquals("loginName", loginNameCaptor.getValue());
        assertEquals(1000L, amountCaptor.getValue().longValue());
        assertEquals(1, investIdCaptor.getValue().longValue());
    }
}
