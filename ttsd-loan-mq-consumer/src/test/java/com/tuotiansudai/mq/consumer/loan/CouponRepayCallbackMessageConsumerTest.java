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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CouponRepayCallbackMessageConsumerTest {

    @MockBean
    private PayWrapperClient payWrapperClient;

    @Autowired
    @Qualifier("couponRepayCallbackMessageConsumer")
    private MessageConsumer consumer;

    @Test
    public void shouldConsume() {
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(payWrapperClient.couponRepayCallback(captor.capture())).thenReturn(new BaseDto<PayDataDto>());
        String message = "100006";
        consumer.consume(message);
        assertEquals("100006", captor.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void shouldConsumeFail() {
        when(payWrapperClient.couponRepayCallback(anyString())).thenReturn(new BaseDto<PayDataDto>(false));
        String message = "50000001";
        consumer.consume(message);
    }
}
