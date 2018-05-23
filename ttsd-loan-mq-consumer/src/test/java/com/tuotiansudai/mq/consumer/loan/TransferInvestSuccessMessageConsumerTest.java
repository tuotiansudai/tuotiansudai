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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TransferInvestSuccessMessageConsumerTest {
    @MockBean
    private PayWrapperClient payWrapperClient;

    @Autowired
    @Qualifier("transferInvestCallbackMessageConsumer")
    private MessageConsumer consumer;

    @Test
    public void shouldConsumeSuccess() {
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(payWrapperClient.investTransferCallback(captor.capture())).thenReturn(new BaseDto<PayDataDto>());
        String message = "12345";
        consumer.consume(message);
        assertEquals("12345", captor.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void shouldConsumeFail() {
        when(payWrapperClient.investTransferCallback(anyString())).thenReturn(new BaseDto<PayDataDto>(false));
        String message = "12345";
        consumer.consume(message);
    }
}
