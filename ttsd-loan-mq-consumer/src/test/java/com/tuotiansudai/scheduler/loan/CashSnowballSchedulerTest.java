package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestProductTypeView;
import com.tuotiansudai.repository.model.ProductType;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class CashSnowballSchedulerTest {

    @InjectMocks
    private CashSnowballActivityScheduler cashSnowballActivityScheduler;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Mock
    private SmsWrapperClient smsWrapperClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void activityEndSendCashIsSuccess(){
        if (DateTime.now().getYear() != 2018){
            return;
        }
        when(investMapper.findAmountOrderByNameAndProductType(any(), any(), any())).thenReturn(getMockList());
        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);
        when(payWrapperClient.transferCash(any(TransferCashDto.class))).thenReturn(new BaseDto(new PayDataDto(true)));
        cashSnowballActivityScheduler.cashSnowballActivityEndSendCash();

        verify(payWrapperClient, times(3)).transferCash(requestModelCaptor.capture());
        List<TransferCashDto> list1 = requestModelCaptor.getAllValues().stream().sorted((t1, t2)-> t1.getLoginName().compareTo(t2.getLoginName())).collect(Collectors.toList());
        assertThat(requestModelCaptor.getAllValues().size(), is(3));
        assertThat(list1.get(0).getAmount(), is("30000"));
        assertThat(list1.get(1).getAmount(), is("30000"));
        assertThat(list1.get(2).getAmount(), is("60000"));
    }

    @Test
    public void activityEndSendCashIsFail(){
        if (DateTime.now().getYear() != 2018){
            return;
        }
        when(investMapper.findAmountOrderByNameAndProductType(any(), any(), any())).thenReturn(getMockList());
        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);

        when(payWrapperClient.transferCash(requestModelCaptor.capture())).thenThrow(Exception.class);
        cashSnowballActivityScheduler.cashSnowballActivityEndSendCash();
        verify(smsWrapperClient,times(3)).sendFatalNotify(any(SmsFatalNotifyDto.class));
        List<TransferCashDto> list1 = requestModelCaptor.getAllValues().stream().sorted((t1, t2)-> t1.getLoginName().compareTo(t2.getLoginName())).collect(Collectors.toList());
        assertThat(requestModelCaptor.getAllValues().size(), is(3));
        assertThat(list1.get(0).getAmount(), is("30000"));
        assertThat(list1.get(1).getAmount(), is("30000"));
        assertThat(list1.get(2).getAmount(), is("60000"));


    }

    public List<InvestProductTypeView> getMockList(){
        InvestProductTypeView investProductTypeView1 = new InvestProductTypeView("0000", "userName0", "mobile0", 1000000, ProductType._360); //0
        InvestProductTypeView investProductTypeView2 = new InvestProductTypeView("1111", "userName1", "mobile1", 19000000, ProductType._360);
        InvestProductTypeView investProductTypeView3 = new InvestProductTypeView("1111", "userName1", "mobile1", 5000000, ProductType._180); //300
        InvestProductTypeView investProductTypeView4 = new InvestProductTypeView("2222", "userName2", "mobile2", 50000000, ProductType._180);  //300
        InvestProductTypeView investProductTypeView5 = new InvestProductTypeView("3333", "userName3", "mobile3", 10000000, ProductType._180);
        InvestProductTypeView investProductTypeView6 = new InvestProductTypeView("3333", "userName3", "mobile3", 10000000, ProductType._180);
        InvestProductTypeView investProductTypeView7 = new InvestProductTypeView("3333", "userName3", "mobile3", 30000000, ProductType._360); //600
        return Lists.newArrayList(investProductTypeView1, investProductTypeView2, investProductTypeView3, investProductTypeView4, investProductTypeView5, investProductTypeView6, investProductTypeView7);
    }
}
