package com.tuotiansudai.mq.consumer.loan;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
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
public class LoanOutSuccessStartWorkMessageConsumerTest {

    @InjectMocks
    private LoanOutSuccessStartWorkMessageConsumer loanOutSuccessStartWorkMessageConsumer;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Mock
    private SmsWrapperClient smsWrapperClient;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Field redisWrapperClientField = this.loanOutSuccessStartWorkMessageConsumer.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.loanOutSuccessStartWorkMessageConsumer, this.redisWrapperClient);
    }

    @Test
    public void consumerTwoUserIsSuccess() throws JsonProcessingException {
        List<InvestModel> investModels = Lists.newArrayList(
                mockInvestModel(1,"2018-03-02 11:00:00", 1000, "loginName1"),
                mockInvestModel(2,"2018-03-03 11:00:00", 1000, "loginName1"),
                mockInvestModel(3,"2018-03-09 11:00:00", 1234, "loginName1"),
                mockInvestModel(4,"2018-03-09 11:00:00", 2000, "loginName2"),
                mockInvestModel(5,"2018-03-09 11:00:00", 3000, "loginName2")
        );

        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(investModels);
        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());

        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenReturn(new BaseDto(new PayDataDto(true)));

        loanOutSuccessStartWorkMessageConsumer.consume(JsonConverter.writeValueAsString(buildMockedLoanOutSuccessMessage()));

        verify(this.redisWrapperClient, times(2))
                .setex(redisKeyCaptor.capture(),  anyInt(), valueCaptor.capture());
        verify(this.payWrapperClient, times(2)).transferCash(requestModelCaptor.capture());

        assertThat(redisKeyCaptor.getAllValues().get(0), is("START_WORK_CASH_KEY:loginName1:1234"));
        assertThat(valueCaptor.getAllValues().get(0), is("success"));
        assertThat(redisKeyCaptor.getAllValues().get(1), is("START_WORK_CASH_KEY:loginName2:1234"));
        assertThat(valueCaptor.getAllValues().get(1), is("success"));
        assertThat(requestModelCaptor.getAllValues().get(0).getLoginName(), is("loginName1"));
        assertThat(requestModelCaptor.getAllValues().get(0).getAmount(), is("11"));
        assertThat(requestModelCaptor.getAllValues().get(1).getLoginName(), is("loginName2"));
        assertThat(requestModelCaptor.getAllValues().get(1).getAmount(), is("25"));
    }

    @Test
    public void consumerOneUserIsSuccess() throws JsonProcessingException {
        List<InvestModel> investModels = Lists.newArrayList(
                mockInvestModel(1,"2018-03-02 11:00:00", 1000, "loginName1"),
                mockInvestModel(2,"2018-03-03 11:00:00", 1000, "loginName1"),
                mockInvestModel(3,"2018-03-09 11:00:00", 1234, "loginName1"),
                mockInvestModel(4,"2018-03-09 11:00:00", 2000, "loginName2"),
                mockInvestModel(5,"2018-03-09 11:00:00", 3000, "loginName2")
        );

        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(investModels);
        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());

        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);
        when(this.redisWrapperClient.exists("START_WORK_CASH_KEY:loginName2:1234")).thenReturn(true);
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenReturn(new BaseDto(new PayDataDto(true)));

        loanOutSuccessStartWorkMessageConsumer.consume(JsonConverter.writeValueAsString(buildMockedLoanOutSuccessMessage()));

        verify(this.redisWrapperClient, times(1))
                .setex(redisKeyCaptor.capture(),  anyInt(), valueCaptor.capture());
        verify(this.payWrapperClient, times(1)).transferCash(requestModelCaptor.capture());

        assertThat(redisKeyCaptor.getValue(), is("START_WORK_CASH_KEY:loginName1:1234"));
        assertThat(valueCaptor.getValue(), is("success"));
        assertThat(requestModelCaptor.getValue().getLoginName(), is("loginName1"));
        assertThat(requestModelCaptor.getValue().getAmount(), is("11"));
    }

    @Test
    @Transactional
    public void consumerIsFail() throws JsonProcessingException {
        List<InvestModel> investModels = Lists.newArrayList(
                mockInvestModel(1,"2018-03-02 11:00:00", 1000, "loginName1"),
                mockInvestModel(2,"2018-03-03 11:00:00", 1000, "loginName1"),
                mockInvestModel(3,"2018-03-09 11:00:00", 1234, "loginName1"),
                mockInvestModel(4,"2018-03-09 11:00:00", 2000, "loginName2"),
                mockInvestModel(5,"2018-03-09 11:00:00", 3000, "loginName2")
        );

        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(investModels);
        when(loanMapper.findById(anyLong())).thenReturn(mockLoanModel());

        ArgumentCaptor<String> redisKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenThrow(Exception.class);

        loanOutSuccessStartWorkMessageConsumer.consume(JsonConverter.writeValueAsString(buildMockedLoanOutSuccessMessage()));

        verify(this.redisWrapperClient, times(2))
                .setex(redisKeyCaptor.capture(),  anyInt(), valueCaptor.capture());
        verify(this.payWrapperClient, times(2)).transferCash(requestModelCaptor.capture());
        verify(this.smsWrapperClient, times(2)).sendFatalNotify(any(SmsFatalNotifyDto.class));

        assertThat(redisKeyCaptor.getAllValues().get(0), is("START_WORK_CASH_KEY:loginName1:1234"));
        assertThat(valueCaptor.getAllValues().get(0), is("fail"));
        assertThat(redisKeyCaptor.getAllValues().get(1), is("START_WORK_CASH_KEY:loginName2:1234"));
        assertThat(valueCaptor.getAllValues().get(1), is("fail"));
        assertThat(requestModelCaptor.getAllValues().get(0).getLoginName(), is("loginName1"));
        assertThat(requestModelCaptor.getAllValues().get(0).getAmount(), is("11"));
        assertThat(requestModelCaptor.getAllValues().get(1).getLoginName(), is("loginName2"));
        assertThat(requestModelCaptor.getAllValues().get(1).getAmount(), is("25"));
    }

    private LoanOutSuccessMessage buildMockedLoanOutSuccessMessage() {
        return new LoanOutSuccessMessage(1234l);
    }

    private LoanModel mockLoanModel(){
        LoanModel loanModel = new LoanModel();
        loanModel.setId(1234l);
        loanModel.setName("name");
        loanModel.setActivityType(ActivityType.NORMAL);
        loanModel.setProductType(ProductType._360);
        return loanModel;
    }

    public InvestModel mockInvestModel(long investId, String date, long amount, String loginName) {
        InvestModel investModel = new InvestModel(investId, 1234l, null, amount, loginName, DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(), Source.WEB, "channel", 0.01);
        investModel.setTradingTime(DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
        return investModel;
    }
}
