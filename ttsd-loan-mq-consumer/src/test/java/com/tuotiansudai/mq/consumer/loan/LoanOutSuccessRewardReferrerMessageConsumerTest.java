package com.tuotiansudai.mq.consumer.loan;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class LoanOutSuccessRewardReferrerMessageConsumerTest {
    @Autowired
    @Qualifier("loanOutSuccessRewardReferrerMessageConsumer")
    private MessageConsumer consumer;

    @MockBean
    private PayWrapperClient payWrapperClient;

    @MockBean
    private SmsWrapperClient smsWrapperClient;

    @Test
    @Transactional
    public void shouldConsumeIsOk() throws JsonProcessingException {
        LoanOutSuccessMessage loanOutSuccessMessage = buildMockedLoanOutSuccessMessage();
        when(payWrapperClient.sendRewardReferrer(anyLong())).thenReturn(new BaseDto<>(true));

        consumer.consume(JsonConverter.writeValueAsString(loanOutSuccessMessage));
        verify(smsWrapperClient, times(0)).sendFatalNotify(any(SmsFatalNotifyDto.class));
    }

    @Test
    @Transactional
    public void shouldConsumeIsFail() throws JsonProcessingException {
        LoanOutSuccessMessage loanOutSuccessMessage = buildMockedLoanOutSuccessMessage();
        when(payWrapperClient.sendRewardReferrer(anyLong())).thenReturn(new BaseDto<>(false));

        try {
            consumer.consume(JsonConverter.writeValueAsString(loanOutSuccessMessage));
        }catch (RuntimeException e){}
        verify(smsWrapperClient, times(1)).sendFatalNotify(any(SmsFatalNotifyDto.class));
    }

    private LoanOutSuccessMessage buildMockedLoanOutSuccessMessage(){
        return new LoanOutSuccessMessage(123l);
    }
}
