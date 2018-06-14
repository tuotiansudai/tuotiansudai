package com.tuotiansudai.mq.consumer.loan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
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
public class LoanOutSuccessSendCashRewardMessageConsumerTest {

    @InjectMocks
    private LoanOutSuccessSendCashRewardMessageConsumer loanOutSuccessSendCashRewardMessageConsumer;

    @Mock
    private PayWrapperClient payWrapperClient;

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
        Field redisWrapperClientField = this.loanOutSuccessSendCashRewardMessageConsumer.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.loanOutSuccessSendCashRewardMessageConsumer, this.redisWrapperClient);
    }

    @Test
    @Transactional
    public void consumerIsSuccess() throws JsonProcessingException {
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

        loanOutSuccessSendCashRewardMessageConsumer.consume(JsonConverter.writeValueAsString(loanOutSuccessMessage));

        verify(this.redisWrapperClient, times(2))
                .hset(redisKeyCaptor.capture(), redisHKeyCaptor.capture(), valueCaptor.capture(), anyInt());
        verify(this.payWrapperClient, times(2)).transferCash(requestModelCaptor.capture());

        assertThat(redisKeyCaptor.getAllValues().get(0), is("LOAN_OUT_SUCCESS_SEND_REWARD_KEY"));
        assertThat(redisHKeyCaptor.getAllValues().get(0), is("123:test1"));
        assertThat(valueCaptor.getAllValues().get(0), is("success"));
        assertThat(redisKeyCaptor.getAllValues().get(1), is("LOAN_OUT_SUCCESS_SEND_REWARD_KEY"));
        assertThat(redisHKeyCaptor.getAllValues().get(1), is("123:test2"));
        assertThat(valueCaptor.getAllValues().get(1), is("success"));
        assertThat(requestModelCaptor.getAllValues().get(0).getLoginName(), is("test1"));
        assertThat(requestModelCaptor.getAllValues().get(0).getAmount(), is("1000"));
        assertThat(requestModelCaptor.getAllValues().get(1).getLoginName(), is("test2"));
        assertThat(requestModelCaptor.getAllValues().get(1).getAmount(), is("2000"));
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

        loanOutSuccessSendCashRewardMessageConsumer.consume(JsonConverter.writeValueAsString(loanOutSuccessMessage));

        verify(this.redisWrapperClient, times(2))
                .hset(redisKeyCaptor.capture(), redisHKeyCaptor.capture(), valueCaptor.capture(), anyInt());
        verify(this.payWrapperClient, times(2)).transferCash(requestModelCaptor.capture());

        assertThat(redisKeyCaptor.getAllValues().get(0), is("LOAN_OUT_SUCCESS_SEND_REWARD_KEY"));
        assertThat(redisHKeyCaptor.getAllValues().get(0), is("123:test1"));
        assertThat(valueCaptor.getAllValues().get(0), is("fail"));
        assertThat(redisKeyCaptor.getAllValues().get(1), is("LOAN_OUT_SUCCESS_SEND_REWARD_KEY"));
        assertThat(redisHKeyCaptor.getAllValues().get(1), is("123:test2"));
        assertThat(valueCaptor.getAllValues().get(1), is("fail"));
        assertThat(requestModelCaptor.getAllValues().get(0).getLoginName(), is("test1"));
        assertThat(requestModelCaptor.getAllValues().get(0).getAmount(), is("1000"));
        assertThat(requestModelCaptor.getAllValues().get(1).getLoginName(), is("test2"));
        assertThat(requestModelCaptor.getAllValues().get(1).getAmount(), is("2000"));
    }

    private LoanOutSuccessMessage buildMockedLoanOutSuccessMessage() {
        return new LoanOutSuccessMessage(123l);
    }

    private LoanDetailsModel getMockedLoanDetailsModel(long loanId) {
        LoanDetailsModel loanDetailsModel = new LoanDetailsModel();
        loanDetailsModel.setNonTransferable(false);
        loanDetailsModel.setDeclaration("declaration");
        loanDetailsModel.setLoanId(loanId);
        loanDetailsModel.setGrantReward(true);
        loanDetailsModel.setRewardRate(0.1D);
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
        investAchievementView1.setAmount(10000);
        investAchievementView1.setLoginName("test1");

        InvestAchievementView investAchievementView2 = new InvestAchievementView();
        investAchievementView2.setAmount(20000);
        investAchievementView2.setLoginName("test2");

        return Arrays.asList(investAchievementView1, investAchievementView2);
    }
}
