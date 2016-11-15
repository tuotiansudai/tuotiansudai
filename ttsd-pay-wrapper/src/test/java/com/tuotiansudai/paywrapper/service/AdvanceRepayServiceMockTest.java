package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.job.AdvanceRepayCallbackJob;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.service.impl.AdvanceRepayServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@Transactional
public class AdvanceRepayServiceMockTest {

    @InjectMocks
    private AdvanceRepayServiceImpl advanceRepayService;

    @Mock
    private PayAsyncClient payAsyncClient;

    @Mock
    RedisWrapperClient redisWrapperClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldIncrRedisTriggerWhenNormalRepayCallbackSuccess() throws Exception {
        when(payAsyncClient.parseCallbackRequest(any(Map.class), anyString(), any(Class.class), any(Class.class))).thenReturn(new BaseCallbackRequestModel());

        advanceRepayService.investPaybackCallback(null, null);

        verify(redisWrapperClient, times(1)).incr(AdvanceRepayCallbackJob.ADVANCE_REPAY_JOB_TRIGGER_KEY);
    }

    @Test
    public void shouldNotIncrTriggerWhenNormalRepayCallbackFail() throws Exception {
        when(payAsyncClient.parseCallbackRequest(any(Map.class), anyString(), any(Class.class), any(Class.class))).thenReturn(null);

        advanceRepayService.investPaybackCallback(null, null);

        verify(redisWrapperClient, times(0)).incr(AdvanceRepayCallbackJob.ADVANCE_REPAY_JOB_TRIGGER_KEY);
    }
}
