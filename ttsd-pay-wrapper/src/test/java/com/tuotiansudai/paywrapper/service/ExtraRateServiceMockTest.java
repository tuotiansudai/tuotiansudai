package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.job.ExtraRateInvestCallbackJob;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.extrarate.service.impl.ExtraRateServiceImpl;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
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
public class ExtraRateServiceMockTest {

    @InjectMocks
    private ExtraRateServiceImpl extraRateService;

    @Mock
    private PayAsyncClient payAsyncClient;

    @Mock
    RedisWrapperClient redisWrapperClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldIncrRedisTriggerWhenCallbackSuccess() {
        when(payAsyncClient.parseCallbackRequest(any(Map.class), anyString(), any(Class.class), any(Class.class))).thenReturn(new BaseCallbackRequestModel());

        extraRateService.extraRateInvestCallback(null, null);

        verify(redisWrapperClient, times(1)).incr(ExtraRateInvestCallbackJob.REPAY_EXTRA_RATE_JOB_TRIGGER_KEY);
    }

    @Test
    public void shouldNotIncrTriggerWhenCallbackFail() {
        when(payAsyncClient.parseCallbackRequest(any(Map.class), anyString(), any(Class.class), any(Class.class))).thenReturn(null);

        extraRateService.extraRateInvestCallback(null, null);

        verify(redisWrapperClient, times(0)).incr(ExtraRateInvestCallbackJob.REPAY_EXTRA_RATE_JOB_TRIGGER_KEY);
    }
}
