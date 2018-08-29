package com.tuotiansudai.fudian.service;

import com.tuotiansudai.fudian.dto.BankInvestDto;
import com.tuotiansudai.fudian.dto.request.LoanInvestRequestDto;
import com.tuotiansudai.fudian.dto.request.RechargeRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/8/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class LoanInvestServiceTest {
    @InjectMocks
    private LoanInvestService loanInvestService;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private RedissonClient redissonClient;
    @Mock
    private BankClient bankClient;
    @Mock
    private MessageQueueClient messageQueueClient;
    @Mock
    private SignatureHelper signatureHelper;
    @Mock
    private QueryTradeService queryTradeService;
    @Mock
    private InsertMapper insertMapper;
    @Mock
    private UpdateMapper updateMapper;
    @Mock
    private SelectMapper selectMapper;

    @Test
    public void investSuccess() {
        ArgumentCaptor<LoanInvestRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanInvestRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> messageTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> messageTimeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);
        BankInvestDto dto = new BankInvestDto("loginName", "mobile", "bankUserName", "bankAccountNo", 2l, 2l, "loanTxNo", 2l, "loanName");

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanInvestRequestDto) o).setOrderNo("111111");
                ((LoanInvestRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        LoanInvestRequestDto loanInvestRequestDto = loanInvestService.invest(Source.WEB, dto);
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(1)).put(messageKeyCaptor.capture(), messageHKeyCaptor.capture(), messageValueCaptor.capture());
        verify(this.redisTemplate, times(1)).expire(messageKeyCaptor.capture(), messageTimeoutCaptor.capture(), messageTimeUnitCaptor.capture());
        verify(this.insertMapper, times(1)).insertLoanInvest(dtoCaptor.capture());
        assertNotNull(loanInvestRequestDto);
    }

    @Test
    public void investFalse() {
        ArgumentCaptor<LoanInvestRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanInvestRequestDto.class);
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        LoanInvestRequestDto loanInvestRequestDto = loanInvestService.invest(Source.WEB, new BankInvestDto());
        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(0)).put(any(), any(), any());
        verify(this.redisTemplate, times(0)).expire(anyString(), anyInt(), any(TimeUnit.DAYS.getClass()));
        verify(this.insertMapper, times(0)).insertLoanInvest(dtoCaptor.capture());
        assertNull(loanInvestRequestDto);
    }

    @Test
    public void fastInvestSuccess() {
        ArgumentCaptor<LoanInvestRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanInvestRequestDto.class);
        BankInvestDto dto = new BankInvestDto("loginName", "mobile", "bankUserName", "bankAccountNo", 2l, 2l, "loanTxNo", 2l, "loanName");
        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RechargeRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanInvestRequestDto) o).setOrderNo("111111");
                ((LoanInvestRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        when(signatureHelper.verifySign(anyString())).thenReturn(true);

        verify(this.insertMapper, times(1)).insertLoanInvest(dtoCaptor.capture());
    }

}
