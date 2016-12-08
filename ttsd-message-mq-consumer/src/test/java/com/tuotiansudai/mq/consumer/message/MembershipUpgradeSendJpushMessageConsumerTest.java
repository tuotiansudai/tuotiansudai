package com.tuotiansudai.mq.consumer.message;

import com.tuotiansudai.message.util.UserMessageEventGenerator;
import com.tuotiansudai.mq.consumer.MessageConsumer;
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
public class MembershipUpgradeSendJpushMessageConsumerTest {

    @Autowired
    @Qualifier("membershipUpgradeSendJpushMessageConsumer")
    private MessageConsumer consumer;

    @MockBean
    private UserMessageEventGenerator userMessageEventGenerator;

    @Test
    @Transactional
    public void shouldConsume() {

        String loginName = "loginName";
        long membershipId = 1111;

        final ArgumentCaptor<String> loginNameCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<Long> membershipIdCaptor = ArgumentCaptor.forClass(Long.class);

        doNothing().when(userMessageEventGenerator).generateMembershipUpgradeEvent(loginNameCaptor.capture(), membershipIdCaptor.capture());

        consumer.consume(loginName + ":" + membershipId);

        assertEquals(loginName, loginNameCaptor.getValue());
        assertEquals(membershipId, membershipIdCaptor.getValue().longValue());
    }
}