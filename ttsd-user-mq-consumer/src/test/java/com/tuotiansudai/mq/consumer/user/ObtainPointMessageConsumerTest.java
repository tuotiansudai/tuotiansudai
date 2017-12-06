package com.tuotiansudai.mq.consumer.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.message.ObtainPointMessage;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.JsonConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ObtainPointMessageConsumerTest {

    @MockBean
    private AccountMapper accountMapper;

    @Autowired
    @Qualifier("obtainPointMessageConsumer")
    private MessageConsumer consumer;

    @Test
    @Transactional
    public void shouldConsume() {

        ObtainPointMessage obtainPointMessage = buildMockedObtainPointMessage();
        AccountModel accountModel = buildMockedAccount();

        when(accountMapper.lockByLoginName(anyString())).thenReturn(accountModel);
        doNothing().when(accountMapper).update(accountModel);

        try {
            consumer.consume(JsonConverter.writeValueAsString(obtainPointMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertEquals("loginName", accountModel.getLoginName());
        assertEquals(200L, accountModel.getPoint());
    }

    private ObtainPointMessage buildMockedObtainPointMessage() {

        ObtainPointMessage obtainPointMessage = new ObtainPointMessage("testAA", 100);
        return obtainPointMessage;
    }

    private AccountModel buildMockedAccount() {
        AccountModel accountModel = new AccountModel("loginName", "111", "111", new Date());
        accountModel.setPoint(100);
        return accountModel;
    }

}
