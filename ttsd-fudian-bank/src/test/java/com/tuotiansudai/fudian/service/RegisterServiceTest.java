package com.tuotiansudai.fudian.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.dto.BankRegisterDto;
import com.tuotiansudai.fudian.dto.request.BankUserRole;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class RegisterServiceTest {

    @InjectMocks
    private RegisterService registerService;

    @Mock
    private SignatureHelper signatureHelper;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private InsertMapper insertMapper;

    @Mock
    private MessageQueueClient messageQueueClient;

    @Mock
    private UpdateMapper updateMapper;

    @Test
    public void registerServiceSuccess(){
        ArgumentCaptor<RegisterRequestDto> dtoCaptor = ArgumentCaptor.forClass(RegisterRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> messageTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> messageTimeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        BankRegisterDto dto = new BankRegisterDto("loginName", "11111111111", "token", "realName", "111111111111111111");

        doNothing().when(signatureHelper).sign(any(), argThat(o -> {
            ((RegisterRequestDto) o).setOrderNo("111111");
            ((RegisterRequestDto) o).setRequestData("requestData");
            return false;
        }));

        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));

        registerService.register(Source.WEB, BankUserRole.INVESTOR, dto);

        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(1)).put(messageKeyCaptor.capture(), messageHKeyCaptor.capture(), messageValueCaptor.capture());
        verify(this.redisTemplate, times(1)).expire(messageKeyCaptor.capture(), messageTimeoutCaptor.capture(), messageTimeUnitCaptor.capture());
        verify(this.insertMapper, times(1)).insertRegister(dtoCaptor.capture());

        assertThat(dtoCaptor.getValue().getMobilePhone(), is("11111111111"));
        assertThat(dtoCaptor.getValue().getIdentityCode(), is("111111111111111111"));
        assertThat(messageKeyCaptor.getValue(), is(MessageFormat.format("BANK_REGISTER_MESSAGE_{0}", dtoCaptor.getValue().getOrderDate())));
        assertThat(messageHKeyCaptor.getValue(), is("111111"));
        assertThat(messageTimeoutCaptor.getValue(), is(7L));
        assertThat(messageTimeUnitCaptor.getValue(), is(TimeUnit.DAYS));
    }

    @Test
    public void registerServiceFail(){
        ArgumentCaptor<RegisterRequestDto> dtoCaptor = ArgumentCaptor.forClass(RegisterRequestDto.class);

        BankRegisterDto dto = new BankRegisterDto("loginName", "11111111111", "token", "realName", "111111111111111111");

        doNothing().when(signatureHelper).sign(any(), argThat(o -> {
            ((RegisterRequestDto) o).setOrderNo("111111");
            ((RegisterRequestDto) o).setRequestData(null);
            return false;
        }));

        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        registerService.register(Source.WEB, BankUserRole.INVESTOR, dto);

        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(0)).put(any(), any(), any());
        verify(this.insertMapper, times(0)).insertRegister(any());

        assertNull(dtoCaptor.getValue().getRequestData());
    }

    @Test
    public void registerNotifyCallbackSuccess(){
        ArgumentCaptor<BankRegisterMessage> messageCaptor = ArgumentCaptor.forClass(BankRegisterMessage.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);

        String responseData = "{\"certInfo\":\"certInfo\",\"content\":{\"accountNo\":\"UA02724627346051001\",\"bank\":\"浦发银行\",\"bankCardNo\":\"1111********2222\",\"bankCode\":\"310\",\"extMark\":\"{\\\"loginName\\\":\\\"loginName\\\",\\\"mobile\\\":\\\"18612801708\\\"}\",\"identityCode\":\"110101199003078179\",\"identityType\":\"1\",\"merchantNo\":\"M02689149095591001\",\"mobilePhone\":\"18612801708\",\"notifyUrl\":\"http://39.107.217.19:10003/callback/notify-url/register\",\"oldPhone\":\"18612801708\",\"orderDate\":\"20180820\",\"orderNo\":\"20180820000000001351\",\"realName\":\"李泽\",\"regDate\":\"20180820\",\"returnUrl\":\"http://qa.tuotiansudai.com:10001/callback/return-url/register\",\"roleType\":\"1\",\"userName\":\"UU02724627342471001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";

        BankRegisterMessage bankRegisterMessage = new BankRegisterMessage(
                "loginName","18612801708","110101199003078179","readName","token",
                null, null,"111111", "20180820",true);
        Gson gson = new GsonBuilder().create();


        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        when(redisTemplate.opsForHash().get(any(), any())).thenReturn(gson.toJson(bankRegisterMessage));

        registerService.notifyCallback(responseData);
        verify(this.redisTemplate.opsForHash(), times(1)).get(messageKeyCaptor.capture(), messageHKeyCaptor.capture());
        verify(this.messageQueueClient, times(1)).sendMessage(eq(MessageQueue.RegisterBankAccount_Success), messageCaptor.capture());
        verify(this.updateMapper, times(1)).updateNotifyResponseData(anyString(), any());

        assertThat(messageCaptor.getValue().getBankAccountNo(), is("UA02724627346051001"));
        assertThat(messageKeyCaptor.getValue(), is("BANK_REGISTER_MESSAGE_20180820"));
        assertThat(messageHKeyCaptor.getValue(), is("20180820000000001351"));
    }

    @Test
    public void registerNotifyCallbackFail(){
        BankRegisterMessage bankRegisterMessage = new BankRegisterMessage(
                "loginName","18612801708","110101199003078179","readName","token",
                null, null,"111111", "20180820",true);
        Gson gson = new GsonBuilder().create();
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        when(redisTemplate.opsForHash().get(any(), any())).thenReturn(gson.toJson(bankRegisterMessage));
        registerService.notifyCallback(null);
        verify(this.redisTemplate.opsForHash(), times(0)).get(anyString(), anyString());
        verify(this.messageQueueClient, times(0)).sendMessage(any(), any());
        verify(this.updateMapper, times(0)).updateNotifyResponseData(anyString(), any());
    }

}
