package com.tuotiansudai.mq.consumer.loan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.membership.service.MembershipInvestService;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class InvestSuccessMembershipUpdateMessageConsumerTest {

    @MockBean
    private MembershipInvestService membershipInvestService;

    @Autowired
    @Qualifier("investSuccessMembershipUpdateMessageConsumer")
    private MessageConsumer consumer;

    @Test
    @Transactional
    public void shouldConsume() {

        InvestSuccessMessage investSuccessMessage = buildMockedInvestSuccessMessage();

        final ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<Long> amountCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<Long> investIdCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<String> loanNameCaptor = ArgumentCaptor.forClass(String.class);

        doNothing().when(membershipInvestService).afterInvestSuccess(loginNameCaptor.capture(), amountCaptor.capture(), investIdCaptor.capture(), loanNameCaptor.capture());

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals("loginName", loginNameCaptor.getValue());
        assertEquals(1000L, amountCaptor.getValue().longValue());
        assertEquals(1, investIdCaptor.getValue().longValue());
    }

    private InvestSuccessMessage buildMockedInvestSuccessMessage() {
        InvestInfo investInfo = new InvestInfo();
        LoanDetailInfo loanDetailInfo = new LoanDetailInfo();
        loanDetailInfo.setLoanId(1);

        investInfo.setInvestId(1);
        investInfo.setLoginName("loginName");
        investInfo.setAmount(1000L);
        investInfo.setStatus("SUCCESS");
        investInfo.setTransferStatus("TRANSFERABLE");

        InvestSuccessMessage investSuccessMessage = new InvestSuccessMessage(investInfo, loanDetailInfo);
        return investSuccessMessage;
    }

}
