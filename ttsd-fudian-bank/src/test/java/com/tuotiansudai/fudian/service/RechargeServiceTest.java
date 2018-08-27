package com.tuotiansudai.fudian.service;

import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.BankRechargeDto;
import com.tuotiansudai.fudian.dto.RechargePayType;
import com.tuotiansudai.fudian.dto.request.RechargeRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/8/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class RechargeServiceTest {
    @InjectMocks
    private RechargeService rechargeService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private MessageQueueClient messageQueueClient;

    @Mock
    private BankConfig bankConfig;

    @Mock
    private SignatureHelper signatureHelper;

    @Mock
    private SelectMapper selectMapper;

    @Mock
    private InsertMapper insertMapper;

    @Mock
    private UpdateMapper updateMapper;

    @Mock
    private QueryTradeService queryTradeService;

    @Test
    public void rechargeSuccess() {
        ArgumentCaptor<RechargeRequestDto> dtoCaptor = ArgumentCaptor.forClass(RechargeRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> messageTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> messageTimeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        BankRechargeDto dto = new BankRechargeDto("loginName", "mobile", "bankUserName", "bankAccountNo", 111l, 1000l, RechargePayType.FAST_PAY);

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((RechargeRequestDto) o).setOrderNo("111111");
                ((RechargeRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        RechargeRequestDto rechargeRequestDto = rechargeService.recharge(Source.WEB, dto);
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(1)).put(messageKeyCaptor.capture(), messageHKeyCaptor.capture(), messageValueCaptor.capture());
        verify(this.redisTemplate, times(1)).expire(messageKeyCaptor.capture(), messageTimeoutCaptor.capture(), messageTimeUnitCaptor.capture());
        verify(this.insertMapper, times(1)).insertRecharge(dtoCaptor.capture());
        assertNotNull(rechargeRequestDto);
    }

    @Test(expected = NullPointerException.class)
    public void rechargeFalse() {
        BankRechargeDto dto = new BankRechargeDto();
        ArgumentCaptor<RechargeRequestDto> dtoCaptor = ArgumentCaptor.forClass(RechargeRequestDto.class);
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        RechargeRequestDto rechargeRequestDto = rechargeService.recharge(Source.WEB, dto);
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(0)).put(any(), any(), any());
        verify(this.redisTemplate, times(0)).expire(any(), any(), any());
        verify(this.insertMapper, times(0)).insertRecharge(dtoCaptor.capture());
        assertNull(rechargeRequestDto);
    }

    @Test
    public void merchantRechargeSuccess() {
        when(bankConfig.getMerchantUserName()).thenReturn("12312");
        when(bankConfig.getMerchantAccountNo()).thenReturn("11111");
        ArgumentCaptor<RechargeRequestDto> dtoCaptor = ArgumentCaptor.forClass(RechargeRequestDto.class);
        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((RechargeRequestDto) o).setOrderNo("111111");
                ((RechargeRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        RechargeRequestDto rechargeRequestDto = rechargeService.merchantRecharge(Source.WEB, "loginName", "mobile", 10000l, 1000l);
        verify(signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(insertMapper, times(1)).insertRecharge(dtoCaptor.capture());
        assertNotNull(rechargeRequestDto);
    }

    @Test(expected = NullPointerException.class)
    public void merchantRechargeFalse() {
        when(bankConfig.getMerchantUserName()).thenReturn("12312");
        when(bankConfig.getMerchantAccountNo()).thenReturn("11111");
        ArgumentCaptor<RechargeRequestDto> dtoCaptor = ArgumentCaptor.forClass(RechargeRequestDto.class);
        RechargeRequestDto rechargeRequestDto = rechargeService.merchantRecharge(Source.WEB, "loginName", "mobile", 10000l, 1000l);
        verify(signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(insertMapper, times(0)).insertRecharge(dtoCaptor.capture());
        assertNull(rechargeRequestDto);
    }

}
