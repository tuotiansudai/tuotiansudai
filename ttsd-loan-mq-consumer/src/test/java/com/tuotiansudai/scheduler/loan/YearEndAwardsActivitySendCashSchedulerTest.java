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
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class YearEndAwardsActivitySendCashSchedulerTest {
    @InjectMocks
    private YearEndAwardsActivitySendCashScheduler yearEndAwardsActivitySendCashScheduler;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Mock
    private SmsWrapperClient smsWrapperClient;

    @Mock
    private InvestMapper investMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void sendCashSuccess(){
        if (DateTime.now().getYear() != 2018){
            return;
        }
        List<InvestProductTypeView> list = getMockList();
        when(investMapper.findAmountOrderByNameAndProductType(any(), any(), any(), anyString())).thenReturn(list);
        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);
        BaseDto<PayDataDto> baseDto = new BaseDto(new PayDataDto(true));
        when(payWrapperClient.transferCash(requestModelCaptor.capture())).thenReturn(baseDto);
        yearEndAwardsActivitySendCashScheduler.yearEndAwardsSendCash();
        verify(payWrapperClient, times(4)).transferCash(any(TransferCashDto.class));
        assertThat(requestModelCaptor.getAllValues().size(), is(4));
        List<TransferCashDto> list1 = requestModelCaptor.getAllValues().stream().sorted((t1, t2)-> t1.getLoginName().compareTo(t2.getLoginName())).collect(Collectors.toList());
        assertThat(list1.get(0).getAmount(), is("600000"));
        assertThat(list1.get(1).getAmount(), is("5700000"));
        assertThat(list1.get(2).getAmount(), is("150000"));
        assertThat(list1.get(3).getAmount(), is("2400000"));
    }

    @Test
    public void sendCashFail(){
        if (DateTime.now().getYear() != 2018){
            return;
        }
        List<InvestProductTypeView> list = getMockList();
        when(investMapper.findAmountOrderByNameAndProductType(any(), any(), any(), anyString())).thenReturn(list);
        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);
        BaseDto<PayDataDto> baseDto = new BaseDto(new PayDataDto(false));
        when(payWrapperClient.transferCash(requestModelCaptor.capture())).thenReturn(baseDto);
        yearEndAwardsActivitySendCashScheduler.yearEndAwardsSendCash();
        verify(payWrapperClient, times(4)).transferCash(any(TransferCashDto.class));
        verify(smsWrapperClient, times(4)).sendFatalNotify(any(SmsFatalNotifyDto.class));
        assertThat(requestModelCaptor.getAllValues().size(), is(4));
        List<TransferCashDto> list1 = requestModelCaptor.getAllValues().stream().sorted((t1, t2)-> t1.getLoginName().compareTo(t2.getLoginName())).collect(Collectors.toList());
        assertThat(list1.get(0).getAmount(), is("600000"));
        assertThat(list1.get(1).getAmount(), is("5700000"));
        assertThat(list1.get(2).getAmount(), is("150000"));
        assertThat(list1.get(3).getAmount(), is("2400000"));
    }

    public List<InvestProductTypeView> getMockList(){
        InvestProductTypeView investProductTypeView1 = new InvestProductTypeView("0000", "userName0", "mobile0", 100000000, ProductType._360); //100000000
        InvestProductTypeView investProductTypeView2 = new InvestProductTypeView("1111", "userName1", "mobile1", 900000000, ProductType._360);
        InvestProductTypeView investProductTypeView3 = new InvestProductTypeView("1111", "userName1", "mobile1", 100000000, ProductType._180); //950000000
        InvestProductTypeView investProductTypeView4 = new InvestProductTypeView("2222", "userName2", "mobile2", 50000000, ProductType._180);  //25000000
        InvestProductTypeView investProductTypeView5 = new InvestProductTypeView("3333", "userName3", "mobile3", 100000000, ProductType._180);
        InvestProductTypeView investProductTypeView6 = new InvestProductTypeView("3333", "userName3", "mobile3", 100000000, ProductType._180);
        InvestProductTypeView investProductTypeView7 = new InvestProductTypeView("3333", "userName3", "mobile3", 300000000, ProductType._360); //400000000
        return Lists.newArrayList(investProductTypeView1, investProductTypeView2, investProductTypeView3, investProductTypeView4, investProductTypeView5, investProductTypeView6, investProductTypeView7);
    }
}
