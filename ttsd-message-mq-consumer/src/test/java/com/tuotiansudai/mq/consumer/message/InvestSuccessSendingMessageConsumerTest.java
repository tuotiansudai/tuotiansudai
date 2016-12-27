package com.tuotiansudai.mq.consumer.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
import com.tuotiansudai.message.UserInfo;
import com.tuotiansudai.message.util.UserMessageEventGenerator;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
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
import static org.mockito.Mockito.doNothing;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class InvestSuccessSendingMessageConsumerTest {

    @Autowired
    @Qualifier("investSuccessSendingMessageConsumer")
    private MessageConsumer consumer;

    @MockBean
    private UserMessageEventGenerator userMessageEventGenerator;

    @Test
    @Transactional
    public void shouldConsume() {

        InvestSuccessMessage investSuccessMessage = buildMockedInvestSuccessMessage();

        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        doNothing().when(userMessageEventGenerator).generateInvestSuccessEvent(captor.capture());

        try {
            consumer.consume(JsonConverter.writeValueAsString(investSuccessMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertEquals(1111, captor.getValue().longValue());
    }


    private InvestSuccessMessage buildMockedInvestSuccessMessage() {
        InvestInfo investInfo = new InvestInfo();
        LoanDetailInfo loanDetailInfo = new LoanDetailInfo();
        UserInfo info = new UserInfo();

        investInfo.setInvestId(1111);
        investInfo.setLoginName("loginName");
        investInfo.setAmount(1000L);
        investInfo.setStatus("SUCCESS");
        investInfo.setTransferStatus("TRANSFERABLE");

        InvestSuccessMessage investSuccessMessage = new InvestSuccessMessage(investInfo, loanDetailInfo, info);
        return investSuccessMessage;
    }

}
