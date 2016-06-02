package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.BaseSyncResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.impl.AdvanceRepayServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class AdvanceRepayPaybackInvestMockTest {

    @InjectMocks
    private AdvanceRepayServiceImpl advanceRepayService;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AmountTransfer amountTransfer;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private LoanService loanService;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldPaybackFirstPeriod() throws Exception {
        long loanId = 1;
        LoanRepayModel loanRepay1 = new LoanRepayModel(1, loanId, 1, 0, 10, new DateTime().plusDays(5).withTime(23, 59, 59, 0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(10);
        loanRepay1.setActualRepayDate(new DateTime().withMillisOfSecond(0).toDate());
        LoanRepayModel loanRepay2 = new LoanRepayModel(2, loanId, 2, 0, 10, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), RepayStatus.COMPLETE);
        loanRepay2.setActualRepayDate(new DateTime().withMillisOfSecond(0).toDate());

        when(loanRepayMapper.findById(loanRepay1.getId())).thenReturn(loanRepay1);

        String investor1LoginName = "investor1";
        String investor2LoginName = "investor2";
        InvestModel invest1 = new InvestModel(1, loanId, null, 10, investor1LoginName, new Date(), Source.WEB, null);
        InvestModel invest2 = new InvestModel(2, loanId, null, 20, investor2LoginName, new Date(), Source.WEB, null);
        List<InvestModel> successInvests = Lists.newArrayList(invest1, invest2);
        when(investMapper.findSuccessInvestsByLoanId(loanId)).thenReturn(successInvests);

        InvestRepayModel invest1InvestRepay1 = new InvestRepayModel(1, invest1.getId(), 1, 0, 3, 0, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        invest1InvestRepay1.setActualInterest(invest1InvestRepay1.getExpectedInterest());
        invest1InvestRepay1.setActualFee(invest1InvestRepay1.getExpectedFee());
        invest1InvestRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        invest1InvestRepay1.setRepayAmount(invest1.getAmount() + invest1InvestRepay1.getActualInterest() - invest1InvestRepay1.getActualFee());
        when(investRepayMapper.findByInvestIdAndPeriod(invest1.getId(), loanRepay1.getPeriod())).thenReturn(invest1InvestRepay1);

        InvestRepayModel invest2InvestRepay1 = new InvestRepayModel(1, invest2.getId(), 1, 0, 6, 1, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        invest2InvestRepay1.setActualInterest(invest2InvestRepay1.getExpectedInterest());
        invest2InvestRepay1.setActualFee(invest2InvestRepay1.getExpectedFee());
        invest2InvestRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        invest2InvestRepay1.setRepayAmount(invest2.getAmount() + invest2InvestRepay1.getActualInterest() - invest2InvestRepay1.getActualFee());
        when(investRepayMapper.findByInvestIdAndPeriod(invest2.getId(), loanRepay1.getPeriod())).thenReturn(invest2InvestRepay1);

        AccountModel investor1Account = new AccountModel(investor1LoginName, "userName", "id", investor1LoginName, investor1LoginName, new Date());
        when(accountMapper.findByLoginName(invest1.getLoginName())).thenReturn(investor1Account);
        AccountModel investor2Account = new AccountModel(investor2LoginName, "userName", "id", investor2LoginName, investor2LoginName, new Date());
        when(accountMapper.findByLoginName(invest2.getLoginName())).thenReturn(investor2Account);

        ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
        responseModel.setRetCode(BaseSyncResponseModel.SUCCESS_CODE);
        when(paySyncClient.send(eq(ProjectTransferMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class))).thenReturn(responseModel);

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        payDataDto.setStatus(true);

        when(loanService.updateLoanStatus(anyLong(), any(LoanStatus.class))).thenReturn(baseDto);

        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn("READY");

        advanceRepayService.paybackInvest(loanRepay1.getId());

        ArgumentCaptor<ProjectTransferRequestModel> requestModelArgumentCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);
        verify(paySyncClient, times(3)).send(eq(ProjectTransferMapper.class), requestModelArgumentCaptor.capture(), eq(ProjectTransferResponseModel.class));
        List<ProjectTransferRequestModel> requestModels = requestModelArgumentCaptor.getAllValues();

        assertThat(requestModels.get(0).getProjectId(), is(String.valueOf(loanId)));
        assertThat(requestModels.get(0).getOrderId().split("X")[0], is(String.valueOf(invest1InvestRepay1.getId())));
        assertThat(requestModels.get(0).getUserId(), is(investor1Account.getPayUserId()));
        assertThat(requestModels.get(0).getAmount(), is(String.valueOf(invest1.getAmount() + invest1InvestRepay1.getActualInterest() - invest1InvestRepay1.getActualFee())));

        assertThat(requestModels.get(1).getProjectId(), is(String.valueOf(loanId)));
        assertThat(requestModels.get(1).getOrderId().split("X")[0], is(String.valueOf(invest2InvestRepay1.getId())));
        assertThat(requestModels.get(1).getUserId(), is(investor2Account.getPayUserId()));
        assertThat(requestModels.get(1).getAmount(), is(String.valueOf(invest2.getAmount() + invest2InvestRepay1.getActualInterest() - invest2InvestRepay1.getActualFee())));

        assertThat(requestModels.get(2).getProjectId(), is(String.valueOf(loanId)));
        assertThat(requestModels.get(2).getOrderId().split("X")[0], is(String.valueOf(loanRepay1.getId())));
        assertThat(requestModels.get(2).getUserId(), is("7099088"));
        assertThat(requestModels.get(2).getAmount(), is(String.valueOf(loanRepay1.getActualInterest()
                - invest1InvestRepay1.getActualInterest() + invest1InvestRepay1.getActualFee()
                - invest2InvestRepay1.getActualInterest() + invest2InvestRepay1.getActualFee())));

        ArgumentCaptor<Long> updateLoanIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<LoanStatus> loanStatusArgumentCaptor = ArgumentCaptor.forClass(LoanStatus.class);
        verify(loanService, times(1)).updateLoanStatus(updateLoanIdArgumentCaptor.capture(), loanStatusArgumentCaptor.capture());
        assertThat(updateLoanIdArgumentCaptor.getValue(), is(loanId));
        assertThat(loanStatusArgumentCaptor.getValue(), is(LoanStatus.COMPLETE));

        ArgumentCaptor<String> redisKey1ArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisKey2ArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisValueArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, times(6)).hset(redisKey1ArgumentCaptor.capture(), redisKey2ArgumentCaptor.capture(), redisValueArgumentCaptor.capture());
    }

    @Test
    public void shouldPaybackLastPeriod() throws Exception {
        long loanId = 1;
        LoanRepayModel loanRepay1 = new LoanRepayModel(1, loanId, 1, 0, 10, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(10);
        loanRepay1.setActualRepayDate(new DateTime().minusDays(30).toDate());
        LoanRepayModel loanRepay2 = new LoanRepayModel(2, loanId, 2, 30, 10, new DateTime().plusDays(5).withTime(23, 59, 59, 0).toDate(), RepayStatus.COMPLETE);
        loanRepay2.setActualInterest(10);
        loanRepay2.setActualRepayDate(new DateTime().withMillisOfSecond(0).toDate());

        when(loanRepayMapper.findById(loanRepay2.getId())).thenReturn(loanRepay2);

        String investor1LoginName = "investor1";
        String investor2LoginName = "investor2";
        InvestModel invest1 = new InvestModel(1, loanId, null, 10, investor1LoginName, new Date(), Source.WEB, null);
        invest1.setId(1);
        InvestModel invest2 = new InvestModel(2, loanId, null, 20, investor2LoginName, new Date(), Source.WEB, null);
        invest2.setId(2);
        List<InvestModel> successInvests = Lists.newArrayList(invest1, invest2);
        when(investMapper.findSuccessInvestsByLoanId(loanId)).thenReturn(successInvests);

        InvestRepayModel invest1InvestRepay2 = new InvestRepayModel(1, invest1.getId(), 1, invest1.getAmount(), 3, 0, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        invest1InvestRepay2.setActualInterest(invest1InvestRepay2.getExpectedInterest());
        invest1InvestRepay2.setActualFee(invest1InvestRepay2.getExpectedFee());
        invest1InvestRepay2.setActualRepayDate(loanRepay2.getActualRepayDate());
        invest1InvestRepay2.setRepayAmount(invest1.getAmount() + invest1InvestRepay2.getActualInterest() - invest1InvestRepay2.getActualFee());
        when(investRepayMapper.findByInvestIdAndPeriod(invest1.getId(), loanRepay2.getPeriod())).thenReturn(invest1InvestRepay2);

        InvestRepayModel invest2InvestRepay2 = new InvestRepayModel(1, invest2.getId(), 1, invest2.getAmount(), 6, 1, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        invest2InvestRepay2.setActualInterest(invest2InvestRepay2.getExpectedInterest());
        invest2InvestRepay2.setActualFee(invest2InvestRepay2.getExpectedFee());
        invest2InvestRepay2.setActualRepayDate(loanRepay1.getActualRepayDate());
        invest2InvestRepay2.setRepayAmount(invest2.getAmount() + invest2InvestRepay2.getActualInterest() - invest2InvestRepay2.getActualFee());
        when(investRepayMapper.findByInvestIdAndPeriod(invest2.getId(), loanRepay2.getPeriod())).thenReturn(invest2InvestRepay2);

        AccountModel investor1Account = new AccountModel(investor1LoginName, "userName", "id", investor1LoginName, investor1LoginName, new Date());
        when(accountMapper.findByLoginName(invest1.getLoginName())).thenReturn(investor1Account);
        AccountModel investor2Account = new AccountModel(investor2LoginName, "userName", "id", investor2LoginName, investor2LoginName, new Date());
        when(accountMapper.findByLoginName(invest2.getLoginName())).thenReturn(investor2Account);

        ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
        responseModel.setRetCode(BaseSyncResponseModel.SUCCESS_CODE);
        when(paySyncClient.send(eq(ProjectTransferMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class))).thenReturn(responseModel);

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        payDataDto.setStatus(true);

        when(loanService.updateLoanStatus(anyLong(), any(LoanStatus.class))).thenReturn(baseDto);

        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn("READY");

        advanceRepayService.paybackInvest(loanRepay2.getId());

        ArgumentCaptor<ProjectTransferRequestModel> requestModelArgumentCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);
        verify(paySyncClient, times(3)).send(eq(ProjectTransferMapper.class), requestModelArgumentCaptor.capture(), eq(ProjectTransferResponseModel.class));
        List<ProjectTransferRequestModel> requestModels = requestModelArgumentCaptor.getAllValues();

        assertThat(requestModels.get(0).getProjectId(), is(String.valueOf(loanId)));
        assertThat(requestModels.get(0).getOrderId().split("X")[0], is(String.valueOf(invest1InvestRepay2.getId())));
        assertThat(requestModels.get(0).getUserId(), is(investor1Account.getPayUserId()));
        assertThat(requestModels.get(0).getAmount(), is(String.valueOf(invest1.getAmount() + invest1InvestRepay2.getActualInterest() - invest1InvestRepay2.getActualFee())));

        assertThat(requestModels.get(1).getProjectId(), is(String.valueOf(loanId)));
        assertThat(requestModels.get(1).getOrderId().split("X")[0], is(String.valueOf(invest2InvestRepay2.getId())));
        assertThat(requestModels.get(1).getUserId(), is(investor2Account.getPayUserId()));
        assertThat(requestModels.get(1).getAmount(), is(String.valueOf(invest2.getAmount() + invest2InvestRepay2.getActualInterest() - invest2InvestRepay2.getActualFee())));

        assertThat(requestModels.get(2).getProjectId(), is(String.valueOf(loanId)));
        assertThat(requestModels.get(2).getOrderId().split("X")[0], is(String.valueOf(loanRepay2.getId())));
        assertThat(requestModels.get(2).getUserId(), is("7099088"));
        assertThat(requestModels.get(2).getAmount(), is(String.valueOf(loanRepay1.getActualInterest()
                - invest1InvestRepay2.getActualInterest() + invest1InvestRepay2.getActualFee()
                - invest2InvestRepay2.getActualInterest() + invest2InvestRepay2.getActualFee())));

        ArgumentCaptor<Long> updateLoanIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<LoanStatus> loanStatusArgumentCaptor = ArgumentCaptor.forClass(LoanStatus.class);
        verify(loanService, times(1)).updateLoanStatus(updateLoanIdArgumentCaptor.capture(), loanStatusArgumentCaptor.capture());
        assertThat(updateLoanIdArgumentCaptor.getValue(), is(loanId));
        assertThat(loanStatusArgumentCaptor.getValue(), is(LoanStatus.COMPLETE));

        ArgumentCaptor<String> redisKey1ArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisKey2ArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisValueArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, times(6)).hset(redisKey1ArgumentCaptor.capture(), redisKey2ArgumentCaptor.capture(), redisValueArgumentCaptor.capture());
    }
}
