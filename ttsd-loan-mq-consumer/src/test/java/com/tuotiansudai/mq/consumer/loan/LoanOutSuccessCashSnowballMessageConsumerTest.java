package com.tuotiansudai.mq.consumer.loan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class LoanOutSuccessCashSnowballMessageConsumerTest {

    @InjectMocks
    private LoanOutSuccessCashSnowballMessageConsumer loanOutSuccessCashSnowballMessageConsumer;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Mock
    private SmsWrapperClient smsWrapperClient;

    @Mock
    private LoanDetailsMapper loanDetailsMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field redisWrapperClientField = this.loanOutSuccessCashSnowballMessageConsumer.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.loanOutSuccessCashSnowballMessageConsumer, this.redisWrapperClient);
    }

    @Test
    @Transactional
    public void firstConsumerIsSuccess() throws JsonProcessingException {
        LoanOutSuccessMessage loanOutSuccessMessage = buildMockedLoanOutSuccessMessage();
        LoanDetailsModel loanDetailsModel = getMockedLoanDetailsModel(loanOutSuccessMessage.getLoanId());
        List<InvestAchievementView> investAchievementViews = getMockedInvestAchievementViews();
        when(investMapper.findAmountOrderByLoanId(anyLong(), any(), any(), any())).thenReturn(investAchievementViews);
        when(loanDetailsMapper.getByLoanId(anyLong())).thenReturn(loanDetailsModel);
        when(loanMapper.findById(anyLong())).thenReturn(getMockedLoanModel(loanOutSuccessMessage.getLoanId()));
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenReturn(new BaseDto(new PayDataDto(true)));

        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);

        loanOutSuccessCashSnowballMessageConsumer.consume(JsonConverter.writeValueAsString(loanOutSuccessMessage));

        verify(this.redisWrapperClient, times(3))
                .hset(redisKeyCaptor.capture(), redisHKeyCaptor.capture(), valueCaptor.capture(), anyInt());
        verify(this.payWrapperClient, times(1)).transferCash(requestModelCaptor.capture());

        assertThat(redisKeyCaptor.getAllValues().get(0), is("CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT"));
        assertThat(redisHKeyCaptor.getAllValues().get(0), is("test1"));
        assertThat(valueCaptor.getAllValues().get(0), is("500000"));
        assertThat(redisKeyCaptor.getAllValues().get(1), is("CASH_SNOWBALL_CASH_KEY"));
        assertThat(redisHKeyCaptor.getAllValues().get(1), is("123:test1"));
        assertThat(valueCaptor.getAllValues().get(1), is("success"));
        assertThat(redisKeyCaptor.getAllValues().get(2), is("CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT"));
        assertThat(redisHKeyCaptor.getAllValues().get(2), is("test2"));
        assertThat(valueCaptor.getAllValues().get(2), is("990000"));
        assertThat(requestModelCaptor.getValue().getLoginName(), is("test1"));
        assertThat(requestModelCaptor.getValue().getAmount(), is("10000"));
    }

    @Test
    @Transactional
    public void secondConsumerIsSuccess() throws JsonProcessingException {
        LoanOutSuccessMessage loanOutSuccessMessage = buildMockedLoanOutSuccessMessage();
        LoanDetailsModel loanDetailsModel = getMockedLoanDetailsModel(loanOutSuccessMessage.getLoanId());
        List<InvestAchievementView> investAchievementViews = getMockedInvestAchievementViews();
        when(investMapper.findAmountOrderByLoanId(anyLong(), any(), any(), any())).thenReturn(investAchievementViews);
        when(loanDetailsMapper.getByLoanId(anyLong())).thenReturn(loanDetailsModel);
        when(loanMapper.findById(anyLong())).thenReturn(getMockedLoanModel(loanOutSuccessMessage.getLoanId()));
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenReturn(new BaseDto(new PayDataDto(true)));
        when(redisWrapperClient.hexists(eq("CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT"), anyString())).thenReturn(true);
        when(redisWrapperClient.hget(eq("CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT"), anyString())).thenReturn("10000");

        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);

        loanOutSuccessCashSnowballMessageConsumer.consume(JsonConverter.writeValueAsString(loanOutSuccessMessage));

        verify(this.redisWrapperClient, times(4))
                .hset(redisKeyCaptor.capture(), redisHKeyCaptor.capture(), valueCaptor.capture(), anyInt());
        verify(this.payWrapperClient, times(2)).transferCash(requestModelCaptor.capture());

        assertThat(redisKeyCaptor.getAllValues().get(0), is("CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT"));
        assertThat(redisHKeyCaptor.getAllValues().get(0), is("test1"));
        assertThat(valueCaptor.getAllValues().get(0), is("510000"));
        assertThat(redisKeyCaptor.getAllValues().get(1), is("CASH_SNOWBALL_CASH_KEY"));
        assertThat(redisHKeyCaptor.getAllValues().get(1), is("123:test1"));
        assertThat(valueCaptor.getAllValues().get(1), is("success"));
        assertThat(redisKeyCaptor.getAllValues().get(2), is("CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT"));
        assertThat(redisHKeyCaptor.getAllValues().get(2), is("test2"));
        assertThat(valueCaptor.getAllValues().get(2), is("0"));
        assertThat(redisKeyCaptor.getAllValues().get(3), is("CASH_SNOWBALL_CASH_KEY"));
        assertThat(redisHKeyCaptor.getAllValues().get(3), is("123:test2"));
        assertThat(valueCaptor.getAllValues().get(3), is("success"));
        assertThat(requestModelCaptor.getAllValues().get(0).getLoginName(), is("test1"));
        assertThat(requestModelCaptor.getAllValues().get(0).getAmount(), is("10000"));
        assertThat(requestModelCaptor.getAllValues().get(1).getLoginName(), is("test2"));
        assertThat(requestModelCaptor.getAllValues().get(1).getAmount(), is("10000"));
    }

    @Test
    @Transactional
    public void consumerIsFail() throws JsonProcessingException {
        LoanOutSuccessMessage loanOutSuccessMessage = buildMockedLoanOutSuccessMessage();
        LoanDetailsModel loanDetailsModel = getMockedLoanDetailsModel(loanOutSuccessMessage.getLoanId());
        List<InvestAchievementView> investAchievementViews = getMockedInvestAchievementViews();
        when(investMapper.findAmountOrderByLoanId(anyLong(), any(), any(), any())).thenReturn(investAchievementViews);
        when(loanDetailsMapper.getByLoanId(anyLong())).thenReturn(loanDetailsModel);
        when(loanMapper.findById(anyLong())).thenReturn(getMockedLoanModel(loanOutSuccessMessage.getLoanId()));
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenThrow(Exception.class);

        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisHKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);

        loanOutSuccessCashSnowballMessageConsumer.consume(JsonConverter.writeValueAsString(loanOutSuccessMessage));

        verify(this.redisWrapperClient, times(3))
                .hset(redisKeyCaptor.capture(), redisHKeyCaptor.capture(), valueCaptor.capture(), anyInt());
        verify(this.payWrapperClient, times(1)).transferCash(requestModelCaptor.capture());

        verify(this.smsWrapperClient, times(1)).sendFatalNotify(any(SmsFatalNotifyDto.class));

        assertThat(redisKeyCaptor.getAllValues().get(0), is("CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT"));
        assertThat(redisHKeyCaptor.getAllValues().get(0), is("test1"));
        assertThat(valueCaptor.getAllValues().get(0), is("500000"));
        assertThat(redisKeyCaptor.getAllValues().get(1), is("CASH_SNOWBALL_CASH_KEY"));
        assertThat(redisHKeyCaptor.getAllValues().get(1), is("123:test1"));
        assertThat(valueCaptor.getAllValues().get(1), is("fail"));
        assertThat(redisKeyCaptor.getAllValues().get(2), is("CASH_SNOWBALL_USER_SURPLUS_ANNUALIZED_AMOUNT"));
        assertThat(redisHKeyCaptor.getAllValues().get(2), is("test2"));
        assertThat(valueCaptor.getAllValues().get(2), is("990000"));
        assertThat(requestModelCaptor.getValue().getLoginName(), is("test1"));
        assertThat(requestModelCaptor.getValue().getAmount(), is("10000"));
    }

    private LoanOutSuccessMessage buildMockedLoanOutSuccessMessage() {
        return new LoanOutSuccessMessage(123l);
    }

    private LoanDetailsModel getMockedLoanDetailsModel(long loanId) {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setNonTransferable(false);
        loanDetailsModel.setDeclaration("declaration");
        loanDetailsModel.setActivity(true);
        loanDetailsModel.setActivityDesc("逢万返百");
        loanDetailsModel.setLoanId(loanId);
        return loanDetailsModel;
    }

    private LoanModel getMockedLoanModel(long loanId){
        LoanModel loanModel = new LoanModel();
        loanModel.setId(loanId);
        loanModel.setName("loanName");
        loanModel.setActivityType(ActivityType.NORMAL);
        loanModel.setProductType(ProductType._360);
        return loanModel;
    }

    private List<InvestAchievementView> getMockedInvestAchievementViews() {
        InvestAchievementView investAchievementView1 = new InvestAchievementView();
        investAchievementView1.setAmount(1500000);
        investAchievementView1.setLoginName("test1");

        InvestAchievementView investAchievementView2 = new InvestAchievementView();
        investAchievementView2.setAmount(990000);
        investAchievementView2.setLoginName("test2");

        return Arrays.asList(investAchievementView1, investAchievementView2);
    }
}
