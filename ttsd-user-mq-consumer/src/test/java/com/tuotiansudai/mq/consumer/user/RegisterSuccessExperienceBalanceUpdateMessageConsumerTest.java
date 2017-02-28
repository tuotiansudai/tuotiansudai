package com.tuotiansudai.mq.consumer.user;

import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.service.ExperienceBillService;
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
public class RegisterSuccessExperienceBalanceUpdateMessageConsumerTest {

    @MockBean
    private ExperienceBillService experienceBillService;

    @Autowired
    @Qualifier("registerSuccessExperienceBalanceUpdateMessageConsumer")
    private MessageConsumer consumer;

    @Test
    @Transactional
    public void shouldConsume() {

        final ArgumentCaptor<Long> experienceAmountCaptor = ArgumentCaptor.forClass(Long.class);
        final ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<ExperienceBillOperationType> experienceBillOperationTypeCaptor = ArgumentCaptor.forClass(ExperienceBillOperationType.class);
        final ArgumentCaptor<ExperienceBillBusinessType> experienceBillBusinessTypeCaptor = ArgumentCaptor.forClass(ExperienceBillBusinessType.class);

        final String message = "loginName";
        doNothing().when(experienceBillService).updateUserExperienceBalanceByLoginName(experienceAmountCaptor.capture(), loginNameCaptor.capture(), experienceBillOperationTypeCaptor.capture(), experienceBillBusinessTypeCaptor.capture());

        consumer.consume(message);

        assertEquals("688800", String.valueOf(experienceAmountCaptor.getValue()));
        assertEquals("loginName", loginNameCaptor.getValue());
        assertEquals(ExperienceBillOperationType.IN, experienceBillOperationTypeCaptor.getValue());
        assertEquals(ExperienceBillBusinessType.REGISTER, experienceBillBusinessTypeCaptor.getValue());
    }

}
