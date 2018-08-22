package com.tuotiansudai.fudian.service;

import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankRegisterDto;
import com.tuotiansudai.fudian.dto.request.BankUserRole;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class RegisterServiceTest {

    @InjectMocks
    private RegisterService registerService;

    @Mock
    private SignatureHelper signatureHelper;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private InsertMapper insertMapper;


    @Test
    public void registerSuccess(){
        ArgumentCaptor<RegisterRequestDto> dtoCaptor = ArgumentCaptor.forClass(RegisterRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> messageTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> messageTimeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        BankRegisterDto dto = new BankRegisterDto("loginName", "11111111111", "token", "realName", "111111111111111111");
        RegisterRequestDto requestDto = new RegisterRequestDto(Source.WEB, dto.getLoginName(), dto.getMobile(), dto.getRealName(), BankUserRole.INVESTOR.getCode(), dto.getIdentityCode());
        requestDto.setOrderNo("1111111111");
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                return requestDto;
            }
        }).when(signatureHelper).sign(any(), eq(requestDto));

        registerService.register(Source.WEB, BankUserRole.INVESTOR, dto);

        verify(this.redisTemplate, times(1)).opsForHash().put(messageKeyCaptor.capture(), messageHKeyCaptor, messageValueCaptor.capture());
        verify(this.redisTemplate, times(1)).expire(messageKeyCaptor.capture(), messageTimeoutCaptor.capture(), messageTimeUnitCaptor.capture());
        verify(this.insertMapper, times(1)).insertRegister(dtoCaptor.capture());

        assertThat(dtoCaptor.getValue().getMobilePhone(), is("11111111111"));
        assertThat(dtoCaptor.getValue().getIdentityCode(), is("111111111111111111"));



    }
}
