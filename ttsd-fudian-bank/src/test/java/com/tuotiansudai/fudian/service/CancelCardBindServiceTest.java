package com.tuotiansudai.fudian.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.request.*;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankBindCardMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
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
public class CancelCardBindServiceTest {

    @InjectMocks
    private CancelCardBindService cancelCardBindService;

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
    public void cancelCardBindServiceSuccess(){
        ArgumentCaptor<CancelCardBindRequestDto> dtoCaptor = ArgumentCaptor.forClass(CancelCardBindRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageValueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> messageTimeoutCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> messageTimeUnitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        BankBaseDto dto = new BankBaseDto("loginName", "11111111111", "UU02683949835091001", "UA02683949835131001");

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RegisterRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((CancelCardBindRequestDto) o).setOrderNo("111111");
                ((CancelCardBindRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));

        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));

        cancelCardBindService.cancel(Source.WEB, dto, true);

        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(1)).put(messageKeyCaptor.capture(), messageHKeyCaptor.capture(), messageValueCaptor.capture());
        verify(this.redisTemplate, times(1)).expire(messageKeyCaptor.capture(), messageTimeoutCaptor.capture(), messageTimeUnitCaptor.capture());
        verify(this.insertMapper, times(1)).insertCancelCardBind(dtoCaptor.capture());

        assertThat(dtoCaptor.getValue().getAccountNo(), is("UA02683949835131001"));
        assertThat(dtoCaptor.getValue().getUserName(), is("UU02683949835091001"));
        assertThat(messageKeyCaptor.getValue(), is(MessageFormat.format("BANK_CANCEL_CARD_BIND_MESSAGE_{0}", dtoCaptor.getValue().getOrderDate())));
        assertThat(messageHKeyCaptor.getValue(), is("111111"));
        assertThat(messageTimeoutCaptor.getValue(), is(7L));
        assertThat(messageTimeUnitCaptor.getValue(), is(TimeUnit.DAYS));
    }

    @Test
    public void cancelCardBindServiceFail(){
        ArgumentCaptor<CancelCardBindRequestDto> dtoCaptor = ArgumentCaptor.forClass(CancelCardBindRequestDto.class);

        BankBaseDto dto = new BankBaseDto("loginName", "11111111111", "UU02683949835091001", "UA02683949835131001");

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RegisterRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((CancelCardBindRequestDto) o).setOrderNo("111111");
                ((CancelCardBindRequestDto) o).setRequestData(null);
                return false;
            }
        }));

        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        cancelCardBindService.cancel(Source.WEB, dto, true);

        verify(this.signatureHelper, times(1)).sign(any(), dtoCaptor.capture());
        verify(this.redisTemplate.opsForHash(), times(0)).put(any(), any(), any());
        verify(this.insertMapper, times(0)).insertCancelCardBind(any());

        assertNull(dtoCaptor.getValue().getRequestData());
    }

    @Test
    public void cardBindNotifyCallbackSuccess(){
        ArgumentCaptor<CancelCardBindRequestDto> messageCaptor = ArgumentCaptor.forClass(CancelCardBindRequestDto.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageHKeyCaptor = ArgumentCaptor.forClass(String.class);

        String responseData = "{\"certInfo\":\"30820410308202F8A00302010202053001970530300D06092A864886F70D0101050500302B310B300906035504061302434E311C301A060355040A0C1343464341205253412054455354204F43413231301E170D3137303630323038353930315A170D3137303730323038353930315A303E310B300906035504061302434E310D300B060355040B0C04636663613120301E06035504030C17746573744F43413231313134393633393339343131323430820122300D06092A864886F70D01010105000382010F003082010A0282010100E0471C107E0383338341256DFF7DF2808FA67B57BBC2346C7BB4807D3C3A71CAC5C478D65D57671F6600C27DFC797E239E6D4845D582CA90CA57D6E479FE4191FF74D60B14C99572DE24F848FC7A3892627A2293437057312F7558E253A715905A563E4F929C17C2F7B67D66E3A4A1C5E90FC35F8A30C71232D663983D1EAFD3AA227098D58286FCCA3C853154E7849E015505A30F0C42EC47130A89ED606E5044C0E15CF97C8BA49CA16274AB4A5735662E13807A026A2A058C4EC36D0CFE639CD14FB7ECD18E76743BE74D09E2461DB5E54520D310BC5255E406CDD3838E378657FA9315F2EF4A2619DB5EB71F21D8ABD8A761DA370A5B551E8FA35ADAF6870203010001A382012630820122301F0603551D23041830168014CFDF99FB86221613392C075E8E3D772BB969EF8E3081D20603551D1F0481CA3081C7308194A08191A0818E86818B6C6461703A2F2F3231302E37342E34322E31303A3338392F636E3D63726C323432382C6F753D5253412C4F553D43524C2C4F3D4346434120534D322054455354204F434132312C433D434E3F63657274696669636174655265766F636174696F6E4C6973743F626173653F6F626A656374636C6173733D63524C446973747269627574696F6E506F696E74302EA02CA02A8628687474703A2F2F3231302E37342E34322E332F4F434132312F5253412F63726C323432382E63726C300B0603551D0F0404030203E8301D0603551D0E04160414283FCAD6B9FC269579FB8BC4769868BC00AF5466300D06092A864886F70D01010505000382010100AA547AD23E3BA80BED4273A5131B249D2B1247F67DFDB0E1E4C5A4643BA9EB72AF7C61605868C2A3EA446798B7C37E0988EE21689CEA0E5BC9C46F3BDF55872EF7825E68F4131BCD2E720A9E51EEE61EF02DA5C5D81EE1D6E4B93E04B485E8AAC7EA2C2A5DCAC65B459E1584320220EBD772D36E9CCD8845A75B506D7D069E8BBAD1DB0D5F4FC8B6078C1135F762A80A55B523A17146E2014A6BA387580D1D959BEE4E691D2D99A6D6C10152CA6D1EAB7BE21B0398D76B4E9CED90FAA8FD94B9CEEE15C14496D25421699EF39FC665DAD15D668BC0A9451B659E13552C0CEA0B97F2912983B287D5D8627337F488C0324F38E46CCAAA70CC17176E84D00AA3BE\",\"content\":{\"accountNo\":\"UA02727469484891001\",\"bank\":\"中国工商银行\",\"bankAccountNo\":\"6212***********3166\",\"bankCode\":\"102\",\"extMark\":\"{\\\"loginName\\\":\\\"rqrtuuvb\\\",\\\"mobile\\\":\\\"13260123051\\\"}\",\"merchantNo\":\"M02689149095591001\",\"notifyUrl\":\"http://39.107.217.19:10003/callback/notify-url/cancel_card_bind\",\"orderDate\":\"20180823\",\"orderNo\":\"20180823000000004069\",\"returnUrl\":\"http://qa.tuotiansudai.com:10004/callback/return-url/cancel_card_bind\",\"status\":\"4\",\"userName\":\"UU02727469481251001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"3CD5B8194C763CD45D0D07196AC8465A6F0D8AEE4AF20EFBBE3CF58024F027713C6C69EE703CD5753F02C045FE4245ECA7E10EF5FE68D7487DF6E86C0C2CCEEB75AAFC02A339CDCAED742277CACADDCD1D8484B26297B987EC4E4A8DA11CB28F70CB40286560957BCE96E611461E0AB1803E71EC0406B316E9DAE6B2FBF9934F08D6B4980EE23955551158EAB22652ECD2734790086ED08CF87CFE094B0B38097D474D307A922DFBF30FCEA380BC5FB8D43C918F93C5F7AF12EBFD0F3DA664473CF81501713C6494985DF016439E796FA111303AC47A144DD6868707F1AF3A62DDB4FB1A204A9C2AD5717B264029664534E0F455E915EFFFA6EC0C242F69163C\"}";

        BankBindCardMessage bankBindCardMessage = new BankBindCardMessage("loginName", "18612801708", "UU02683949835091001", "UA02683949835131001", "111111", "20180810", true);
        Gson gson = new GsonBuilder().create();

        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        when(redisTemplate.opsForHash().get(any(), any())).thenReturn(gson.toJson(bankBindCardMessage));

        cancelCardBindService.notifyCallback(responseData);
        verify(this.redisTemplate.opsForHash(), times(1)).get(messageKeyCaptor.capture(), messageHKeyCaptor.capture());
        verify(this.messageQueueClient, times(1)).publishMessage(eq(MessageTopic.BindBankCard), messageCaptor.capture());
        verify(this.updateMapper, times(1)).updateNotifyResponseData(anyString(), any());

        assertThat(messageCaptor.getValue().getBankAccountNo(), is("UA02683949835131001"));
        assertThat(messageCaptor.getValue().getCardNumber(), is("6212***********3166"));
        assertThat(messageKeyCaptor.getValue(), is("BANK_CARD_BIND_MESSAGE_20180810"));
        assertThat(messageHKeyCaptor.getValue(), is("20180810000000000000"));
    }

//    @Test
//    public void cardBindNotifyCallbackFail(){
//        BankBindCardMessage bankBindCardMessage = new BankBindCardMessage("loginName", "18612801708", "UU02683949835091001", "UA02683949835131001", "111111", "20180810", true);
//        Gson gson = new GsonBuilder().create();
//        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
//        when(redisTemplate.opsForHash().get(any(), any())).thenReturn(gson.toJson(bankBindCardMessage));
//        cardBindService.notifyCallback(null);
//        verify(this.redisTemplate.opsForHash(), times(0)).get(anyString(), anyString());
//        verify(this.messageQueueClient, times(0)).publishMessage(any(), any());
//        verify(this.updateMapper, times(0)).updateNotifyResponseData(anyString(), any());
//    }

}
