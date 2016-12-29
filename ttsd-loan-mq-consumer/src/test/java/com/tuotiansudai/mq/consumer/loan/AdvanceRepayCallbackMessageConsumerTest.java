package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AdvanceRepayCallbackMessageConsumerTest {

    @MockBean
    private PayWrapperClient payWrapperClient;

    @Autowired
    @Qualifier("advanceRepayCallbackMessageConsumer")
    private MessageConsumer consumer;

    @Test
    public void shouldConsume() {
        final ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        when(payWrapperClient.advanceRepayInvestPayback(captor.capture())).thenReturn(new BaseDto<PayDataDto>());
        String message = "1000005";
        consumer.consume(message);
        assertEquals("1000005", String.valueOf(captor.getValue()));
    }

    @Test(expected = RuntimeException.class)
    public void shouldConsumeFail() {
        when(payWrapperClient.normalRepayInvestPayback(anyLong())).thenReturn(new BaseDto<PayDataDto>(false));
        String message = "100004";
        consumer.consume(message);
    }
}
