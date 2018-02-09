package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.service.impl.InvestServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})@Transactional
public class InvestServiceMockTest {

    @InjectMocks
    private InvestServiceImpl investService;

    @Mock
    private PayAsyncClient payAsyncClient;

    @Mock
    MQWrapperClient mqWrapperClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSendMessageWhenCallbackSuccess() {
        when(payAsyncClient.parseCallbackRequest(any(Map.class), anyString(), any(Class.class), any(Class.class))).thenReturn(new BaseCallbackRequestModel());

        investService.investCallback(null, null);

        verify(mqWrapperClient, never()).sendMessage(any(MessageQueue.class), anyString());
    }

    @Test
    public void shouldNotSendMessageWhenCallbackFail() {
        when(payAsyncClient.parseCallbackRequest(any(Map.class), anyString(), any(Class.class), any(Class.class))).thenReturn(null);

        investService.investCallback(null, null);

        verify(mqWrapperClient, times(0)).sendMessage(MessageQueue.InvestCallback, "0");
    }
}
