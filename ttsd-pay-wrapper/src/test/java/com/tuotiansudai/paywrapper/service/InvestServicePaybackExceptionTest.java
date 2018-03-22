package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.impl.InvestServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})@Transactional
public class InvestServicePaybackExceptionTest {

    @InjectMocks
    private InvestServiceImpl investService;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private SmsWrapperClient smsWrapperClient;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    // case7: 超投，返款。抛出 PayException 异常
    @Test
    public void overInvestPaybackPayException() throws Exception {

        long requestModelId = 1L;
        String orderId= "11111";

        InvestNotifyRequestModel model = new InvestNotifyRequestModel();
        model.setStatus(NotifyProcessStatus.NOT_DONE.toString());
        model.setId(requestModelId);
        model.setOrderId(orderId);
        model.setRetCode("0000");

        long loanId = 77777777L;
        long investId = 1;
        long amount = 99L;
        String loginName = "zbx";
        long sumAmount = 1000L;

        InvestModel investModel = new InvestModel();
        investModel.setId(investId);
        investModel.setAmount(amount);
        investModel.setLoanId(loanId);
        investModel.setLoginName(loginName);
        investModel.setStatus(InvestStatus.WAIT_PAY);

        when(this.investMapper.lockById(investId)).thenReturn(investModel);
        when(this.investMapper.sumSuccessInvestAmount(loanId)).thenReturn(sumAmount);

        LoanModel loanModel = new LoanModel();
        loanModel.setId(loanId);
        loanModel.setLoanAmount(1000L);
        when(this.loanMapper.findById(loanId)).thenReturn(loanModel);

        AccountModel accountModel = new AccountModel(loginName, "mockPayUserId", "", new Date());
        when(this.accountMapper.findByLoginName(loginName)).thenReturn(accountModel);

        when(this.paySyncClient.send(Matchers.<Class<? extends ProjectTransferMapper>>any(), any(ProjectTransferRequestModel.class), Matchers.<Class<ProjectTransferResponseModel>>any())).thenThrow(PayException.class);

        when(this.smsWrapperClient.sendFatalNotify(any(SmsFatalNotifyDto.class))).thenReturn(null);
        investService.asyncInvestCallback(investId);

        ArgumentCaptor<InvestModel> investModelArgumentCaptor = ArgumentCaptor.forClass(InvestModel.class);
        verify(investMapper, times(1)).update(investModelArgumentCaptor.capture());
        assertThat(investModelArgumentCaptor.getValue().getStatus(), is(InvestStatus.OVER_INVEST_PAYBACK_FAIL));
    }

    // case7: 超投，返款抛出 Exception 异常，需要集成测试中验证
}
