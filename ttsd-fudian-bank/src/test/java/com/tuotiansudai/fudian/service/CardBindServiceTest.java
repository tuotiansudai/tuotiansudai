package com.tuotiansudai.fudian.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.BankRegisterDto;
import com.tuotiansudai.fudian.dto.request.BankUserRole;
import com.tuotiansudai.fudian.dto.request.CardBindRequestDto;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankBindCardMessage;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
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
public class CardBindServiceTest {

    @InjectMocks
    private CardBindService cardBindService;

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
    public void cardBindServiceSuccess(){
        ArgumentCaptor<CardBindRequestDto> dtoCaptor = ArgumentCaptor.forClass(CardBindRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> messageTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> messageTimeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        BankBaseDto dto = new BankBaseDto("loginName", "11111111111", "UU02683949835091001", "UA02683949835131001");

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RegisterRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((CardBindRequestDto) o).setOrderNo("111111");
                ((CardBindRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));

        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));

        cardBindService.bind(Source.WEB, dto, BankUserRole.INVESTOR);

        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(1)).put(messageKeyCaptor.capture(), messageHKeyCaptor.capture(), messageValueCaptor.capture());
        verify(this.redisTemplate, times(1)).expire(messageKeyCaptor.capture(), messageTimeoutCaptor.capture(), messageTimeUnitCaptor.capture());
        verify(this.insertMapper, times(1)).insertCardBind(dtoCaptor.capture());

        assertThat(dtoCaptor.getValue().getAccountNo(), is("UA02683949835131001"));
        assertThat(dtoCaptor.getValue().getUserName(), is("UU02683949835091001"));
        assertThat(messageKeyCaptor.getValue(), is(MessageFormat.format("BANK_CARD_BIND_MESSAGE_{0}", dtoCaptor.getValue().getOrderDate())));
        assertThat(messageHKeyCaptor.getValue(), is("111111"));
        assertThat(messageTimeoutCaptor.getValue(), is(7L));
        assertThat(messageTimeUnitCaptor.getValue(), is(TimeUnit.DAYS));
    }

    @Test
    public void cardBindServiceFail(){
        ArgumentCaptor<CardBindRequestDto> dtoCaptor = ArgumentCaptor.forClass(CardBindRequestDto.class);

        BankBaseDto dto = new BankBaseDto("loginName", "11111111111", "UU02683949835091001", "UA02683949835131001");

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RegisterRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((CardBindRequestDto) o).setOrderNo("111111");
                ((CardBindRequestDto) o).setRequestData(null);
                return false;
            }
        }));

        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        cardBindService.bind(Source.WEB, dto, BankUserRole.INVESTOR);

        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(0)).put(any(), any(), any());
        verify(this.insertMapper, times(0)).insertCardBind(any());

        assertNull(dtoCaptor.getValue().getRequestData());
    }

    @Test
    public void cardBindNotifyCallbackSuccess(){
        ArgumentCaptor<BankBindCardMessage> messageCaptor = ArgumentCaptor.forClass(BankBindCardMessage.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);

        String responseData = "{\"certInfo\":\"certInfo\",\"content\":{\"accountNo\":\"UA02683949835131001\",\"bank\":\"平安银行\",\"bankAccountNo\":\"6212***********3166\",\"bankCode\":\"102\",\"extMark\":\"{\\\"loginName\\\":\\\"032121w\\\",\\\"mobile\\\":\\\"13800000390\\\"}\",\"merchantNo\":\"M02608959047521001\",\"notifyUrl\":\"http://118.187.56.164:30001/callback/notify-url/card_bind\",\"orderDate\":\"20180810\",\"orderNo\":\"20180810000000000000\",\"returnUrl\":\"http://192.168.80.88:8484/callback/return-url/card_bind\",\"status\":\"1\",\"userName\":\"UU02683949835091001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";

        BankBindCardMessage bankBindCardMessage = new BankBindCardMessage("loginName", "18612801708", "UU02683949835091001", "UA02683949835131001", "111111", "20180810", true);
        Gson gson = new GsonBuilder().create();

        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        when(redisTemplate.opsForHash().get(any(), any())).thenReturn(gson.toJson(bankBindCardMessage));

        cardBindService.notifyCallback(responseData);
        verify(this.redisTemplate.opsForHash(), times(1)).get(messageKeyCaptor.capture(), messageHKeyCaptor.capture());
        verify(this.messageQueueClient, times(1)).publishMessage(eq(MessageTopic.BindBankCard), messageCaptor.capture());
        verify(this.updateMapper, times(1)).updateNotifyResponseData(anyString(), any());

        assertThat(messageCaptor.getValue().getBankAccountNo(), is("UA02683949835131001"));
        assertThat(messageCaptor.getValue().getCardNumber(), is("6212***********3166"));
        assertThat(messageKeyCaptor.getValue(), is("BANK_CARD_BIND_MESSAGE_20180810"));
        assertThat(messageHKeyCaptor.getValue(), is("20180810000000000000"));
    }

    @Test
    public void cardBindNotifyCallbackFail(){
        BankBindCardMessage bankBindCardMessage = new BankBindCardMessage("loginName", "18612801708", "UU02683949835091001", "UA02683949835131001", "111111", "20180810", true);
        Gson gson = new GsonBuilder().create();
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        when(redisTemplate.opsForHash().get(any(), any())).thenReturn(gson.toJson(bankBindCardMessage));
        cardBindService.notifyCallback(null);
        verify(this.redisTemplate.opsForHash(), times(0)).get(anyString(), anyString());
        verify(this.messageQueueClient, times(0)).publishMessage(any(), any());
        verify(this.updateMapper, times(0)).updateNotifyResponseData(anyString(), any());
    }

}
