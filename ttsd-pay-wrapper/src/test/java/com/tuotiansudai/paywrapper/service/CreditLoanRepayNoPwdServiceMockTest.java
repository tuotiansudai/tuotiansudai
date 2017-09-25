package com.tuotiansudai.paywrapper.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.service.impl.CreditLoanRepayNoPwdServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CreditLoanRepayNoPwdServiceMockTest {
    @InjectMocks
    private CreditLoanRepayNoPwdServiceImpl creditLoanRepayNoPwdService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PayAsyncClient payAsyncClient;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field redisWrapperClientField = this.creditLoanRepayNoPwdService.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.creditLoanRepayNoPwdService, this.redisWrapperClient);
    }

    @Test
    public void shouldLoanNoPwdRepayFailedWhenInvalidParameters() {
        int orderId = 1;
        String mobile = "13900000000";
        String loginName = "loginName";
        BaseDto<PayDataDto> dto = this.creditLoanRepayNoPwdService.creditLoanRepayNoPwd(orderId, mobile, 0);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("还款金额必须大于零"));


        when(accountMapper.findByLoginName(mobile)).thenReturn(null);
        dto = this.creditLoanRepayNoPwdService.creditLoanRepayNoPwd(orderId, mobile, 1);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("用户未开通支付账户"));

        when(accountMapper.findByMobile(mobile)).thenReturn(new AccountModel());
        when(redisWrapperClient.exists(MessageFormat.format("credit:loan:repaying:{0}", String.valueOf(orderId)))).thenReturn(true);
        dto = this.creditLoanRepayNoPwdService.creditLoanRepayNoPwd(orderId, mobile, 1);
        assertFalse(dto.getData().getStatus());
        assertThat(dto.getData().getMessage(), is("还款交易进行中, 请30分钟后查看"));

    }

}
