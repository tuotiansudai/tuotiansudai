package com.tuotiansudai.scheduler.loan;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestProductTypeView;
import com.tuotiansudai.repository.model.ProductType;
import org.assertj.core.util.Lists;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class IphoneXActivitySendCashSchedulerTest {

    @InjectMocks
    private IphoneXActivitySendCashScheduler iphoneXActivitySendCashScheduler;

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
    public void shouldSendCashIsSuccess() throws Exception {

        if (DateTime.now().getYear() != 2016) {
            return;
        }

        InvestProductTypeView investProductTypeView1 = new InvestProductTypeView("0000", "userName0", "mobile0", 10000000, ProductType._360); //88
        InvestProductTypeView investProductTypeView2 = new InvestProductTypeView("1111", "userName1", "mobile1", 10000000, ProductType._180);
        InvestProductTypeView investProductTypeView3 = new InvestProductTypeView("1111", "userName1", "mobile1", 100000000, ProductType._90); //388
        InvestProductTypeView investProductTypeView4 = new InvestProductTypeView("2222", "userName2", "mobile2", 10000000, ProductType._180); //0
        InvestProductTypeView investProductTypeView5 = new InvestProductTypeView("3333", "userName3", "mobile3", 10000000, ProductType._360);
        InvestProductTypeView investProductTypeView6 = new InvestProductTypeView("3333", "userName3", "mobile3", 10000000, ProductType._90);
        InvestProductTypeView investProductTypeView7 = new InvestProductTypeView("3333", "userName3", "mobile3", 10000000, ProductType._180); //88

        List<InvestProductTypeView> list = Lists.newArrayList(investProductTypeView1, investProductTypeView2, investProductTypeView3, investProductTypeView4, investProductTypeView5, investProductTypeView6, investProductTypeView7);

        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);

        when(investMapper.findAmountOrderByNameAndProductType(any(), any(), null)).thenReturn(list);

        BaseDto<PayDataDto> baseDto = new BaseDto();
        baseDto.setSuccess(true);
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setStatus(false);
        baseDto.setData(payDataDto);
        when(payWrapperClient.transferCash(requestModelCaptor.capture())).thenReturn(baseDto);

        iphoneXActivitySendCashScheduler.iphoneXActivitySendCash();

        verify(payWrapperClient, times(3)).transferCash(any(TransferCashDto.class));
        assertThat(requestModelCaptor.getAllValues().size(), is(3));
        assertThat(requestModelCaptor.getAllValues().get(0).getAmount(), is("8800"));
        assertThat(requestModelCaptor.getAllValues().get(1).getAmount(), is("38800"));
        assertThat(requestModelCaptor.getAllValues().get(2).getAmount(), is("8800"));

    }

    @Test
    public void shouldSendCashIsFail() throws Exception{

        if (DateTime.now().getYear() != 2016) {
            return;
        }

        InvestProductTypeView investProductTypeView1 = new InvestProductTypeView("0000", "userName0", "mobile0", 10000000, ProductType._360); //88
        InvestProductTypeView investProductTypeView2 = new InvestProductTypeView("1111", "userName1", "mobile1", 10000000, ProductType._180);
        InvestProductTypeView investProductTypeView3 = new InvestProductTypeView("1111", "userName1", "mobile1", 100000000, ProductType._90); //388
        InvestProductTypeView investProductTypeView4 = new InvestProductTypeView("2222", "userName2", "mobile2", 10000000, ProductType._180); //0
        InvestProductTypeView investProductTypeView5 = new InvestProductTypeView("3333", "userName3", "mobile3", 10000000, ProductType._360);
        InvestProductTypeView investProductTypeView6 = new InvestProductTypeView("3333", "userName3", "mobile3", 10000000, ProductType._90);
        InvestProductTypeView investProductTypeView7 = new InvestProductTypeView("3333", "userName3", "mobile3", 10000000, ProductType._180); //88

        List<InvestProductTypeView> list = Lists.newArrayList(investProductTypeView1, investProductTypeView2, investProductTypeView3, investProductTypeView4, investProductTypeView5, investProductTypeView6, investProductTypeView7);

        ArgumentCaptor<TransferCashDto> requestModelCaptor = ArgumentCaptor.forClass(TransferCashDto.class);

        when(investMapper.findAmountOrderByNameAndProductType(any(), any(), null)).thenReturn(list);

        BaseDto<PayDataDto> baseDto = new BaseDto();
        baseDto.setSuccess(true);
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setStatus(false);
        baseDto.setData(payDataDto);
        when(payWrapperClient.transferCash(requestModelCaptor.capture())).thenReturn(baseDto);

        iphoneXActivitySendCashScheduler.iphoneXActivitySendCash();

        verify(smsWrapperClient, times(3)).sendFatalNotify(any(SmsFatalNotifyDto.class));

        assertThat(requestModelCaptor.getAllValues().size(), is(3));
        assertThat(requestModelCaptor.getAllValues().get(0).getAmount(), is("8800"));
        assertThat(requestModelCaptor.getAllValues().get(1).getAmount(), is("38800"));
        assertThat(requestModelCaptor.getAllValues().get(2).getAmount(), is("8800"));

    }
}
