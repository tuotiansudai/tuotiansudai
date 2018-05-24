package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.loanout.LoanService;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.BaseSyncResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.impl.NormalRepayServiceImpl;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})public class NormalRepayPaybackInvestMockTest {

    @InjectMocks
    private NormalRepayServiceImpl normalRepayService;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private CouponRepayMapper couponRepayMapper;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private LoanService loanService;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);

        Field redisWrapperClientField = this.normalRepayService.getClass().getDeclaredField("redisWrapperClient");
        redisWrapperClientField.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(redisWrapperClientField, redisWrapperClientField.getModifiers() & ~Modifier.FINAL);
        redisWrapperClientField.set(this.normalRepayService, this.redisWrapperClient);
    }

    @Test
    public void shouldPaybackFirstPeriodWhenLoanIsRepaying() throws Exception {
        long loanId = 1;
        LoanRepayModel loanRepay1 = new LoanRepayModel(1, loanId, 1, 10, 10, new DateTime().withTime(23, 59, 59, 0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(10);
        loanRepay1.setActualRepayDate(new DateTime().withMillisOfSecond(0).toDate());
        loanRepay1.setRepayAmount(loanRepay1.getActualInterest());
        LoanRepayModel loanRepay2 = new LoanRepayModel(2, loanId, 2, 10, 10, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), RepayStatus.REPAYING);

        when(loanRepayMapper.findById(loanRepay1.getId())).thenReturn(loanRepay1);
        when(loanRepayMapper.findLastLoanRepay(loanId)).thenReturn(loanRepay2);

        String investor1LoginName = "investor1";
        String investor2LoginName = "investor2";
        InvestModel invest1 = new InvestModel(1, loanId, null, investor1LoginName, 10, 0.1, false, new Date(), Source.WEB, null);
        InvestModel invest2 = new InvestModel(2, loanId, null, investor2LoginName, 20, 0.1, false, new Date(), Source.WEB, null);
        List<InvestModel> successInvests = Lists.newArrayList(invest1, invest2);
        when(investMapper.findSuccessInvestsByLoanId(loanId)).thenReturn(successInvests);

        InvestRepayModel invest1InvestRepay1 = new InvestRepayModel(1, invest1.getId(), 1, 0, 3, 0, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        invest1InvestRepay1.setActualInterest(invest1InvestRepay1.getExpectedInterest());
        invest1InvestRepay1.setActualFee(invest1InvestRepay1.getExpectedFee());
        invest1InvestRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        invest1InvestRepay1.setRepayAmount(invest1InvestRepay1.getActualInterest() - invest1InvestRepay1.getActualFee());

        when(investRepayMapper.findByInvestIdAndPeriod(invest1.getId(), loanRepay1.getPeriod())).thenReturn(invest1InvestRepay1);

        InvestRepayModel invest2InvestRepay1 = new InvestRepayModel(1, invest2.getId(), 1, 0, 6, 1, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        invest2InvestRepay1.setActualInterest(invest2InvestRepay1.getExpectedInterest());
        invest2InvestRepay1.setActualFee(invest2InvestRepay1.getExpectedFee());
        invest2InvestRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        invest2InvestRepay1.setRepayAmount(invest2InvestRepay1.getActualInterest() - invest2InvestRepay1.getActualFee());
        when(investRepayMapper.findByInvestIdAndPeriod(invest2.getId(), loanRepay1.getPeriod())).thenReturn(invest2InvestRepay1);

        AccountModel investor1Account = new AccountModel(investor1LoginName, investor1LoginName, investor1LoginName, new Date());
        when(accountMapper.findByLoginName(invest1.getLoginName())).thenReturn(investor1Account);
        AccountModel investor2Account = new AccountModel(investor2LoginName, investor2LoginName, investor2LoginName, new Date());
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

        assertTrue(normalRepayService.paybackInvest(loanRepay1.getId()));

        ArgumentCaptor<ProjectTransferRequestModel> requestModelArgumentCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);
        verify(paySyncClient, times(3)).send(eq(ProjectTransferMapper.class), requestModelArgumentCaptor.capture(), eq(ProjectTransferResponseModel.class));
        List<ProjectTransferRequestModel> requestModels = requestModelArgumentCaptor.getAllValues();

        assertThat(requestModels.get(0).getProjectId(), is(String.valueOf(loanId)));
        assertThat(requestModels.get(0).getOrderId().split("X")[0], is(String.valueOf(invest1InvestRepay1.getId())));
        assertThat(requestModels.get(0).getUserId(), is(investor1Account.getPayUserId()));
        assertThat(requestModels.get(0).getAmount(), is(String.valueOf(invest1InvestRepay1.getExpectedInterest() - invest1InvestRepay1.getExpectedFee())));

        assertThat(requestModels.get(1).getProjectId(), is(String.valueOf(loanId)));
        assertThat(requestModels.get(1).getOrderId().split("X")[0], is(String.valueOf(invest2InvestRepay1.getId())));
        assertThat(requestModels.get(1).getUserId(), is(investor2Account.getPayUserId()));
        assertThat(requestModels.get(1).getAmount(), is(String.valueOf(invest2InvestRepay1.getExpectedInterest() - invest2InvestRepay1.getExpectedFee())));

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
        assertThat(loanStatusArgumentCaptor.getValue(), is(LoanStatus.REPAYING));

        ArgumentCaptor<String> redisKey1ArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisKey2ArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisValueArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, times(6)).hset(redisKey1ArgumentCaptor.capture(), redisKey2ArgumentCaptor.capture(), redisValueArgumentCaptor.capture());
    }

    @Test
    public void shouldPayBackThePeriodWhenInvestRepayIsComplete() throws Exception {
        long loanId = 1;
        LoanRepayModel loanRepay1 = new LoanRepayModel(1, loanId, 1, 10, 10, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), RepayStatus.REPAYING);
        loanRepay1.setActualInterest(10);
        loanRepay1.setActualRepayDate(new DateTime().minusDays(30).toDate());
        LoanRepayModel loanRepay2 = new LoanRepayModel(2, loanId, 2, 30, 10, new DateTime().withTime(23, 59, 59, 0).toDate(), RepayStatus.COMPLETE);
        loanRepay2.setActualInterest(10);
        loanRepay2.setActualRepayDate(new DateTime().withMillisOfSecond(0).toDate());

        when(loanRepayMapper.findById(loanRepay2.getId())).thenReturn(loanRepay2);
        when(loanRepayMapper.findLastLoanRepay(loanId)).thenReturn(loanRepay2);

        String investor1LoginName = "investor1";
        String investor2LoginName = "investor2";
        InvestModel invest1 = new InvestModel(1, loanId, null, investor1LoginName, 10, 0.1, false, new Date(), Source.WEB, null);
        InvestModel invest2 = new InvestModel(2, loanId, null, investor2LoginName, 20, 0.1, false, new Date(), Source.WEB, null);
        List<InvestModel> successInvests = Lists.newArrayList(invest1, invest2);
        when(investMapper.findSuccessInvestsByLoanId(loanId)).thenReturn(successInvests);

        InvestRepayModel invest1InvestRepay2 = new InvestRepayModel(2, invest1.getId(), 1, 10, 3, 0, loanRepay2.getRepayDate(), RepayStatus.WAIT_PAY);
        invest1InvestRepay2.setActualInterest(invest1InvestRepay2.getExpectedInterest());
        invest1InvestRepay2.setActualFee(invest1InvestRepay2.getExpectedFee());
        invest1InvestRepay2.setActualRepayDate(loanRepay1.getActualRepayDate());
        invest1InvestRepay2.setRepayAmount(invest1InvestRepay2.getCorpus() + invest1InvestRepay2.getActualInterest() - invest1InvestRepay2.getActualFee());
        when(investRepayMapper.findByInvestIdAndPeriod(invest1.getId(), loanRepay2.getPeriod())).thenReturn(invest1InvestRepay2);

        InvestRepayModel invest2InvestRepay2 = new InvestRepayModel(2, invest2.getId(), 1, 20, 6, 1, loanRepay2.getRepayDate(), RepayStatus.COMPLETE);
        invest2InvestRepay2.setActualInterest(invest2InvestRepay2.getExpectedInterest());
        invest2InvestRepay2.setActualFee(invest2InvestRepay2.getExpectedFee());
        invest2InvestRepay2.setActualRepayDate(loanRepay1.getActualRepayDate());
        invest2InvestRepay2.setRepayAmount(invest2InvestRepay2.getCorpus() + invest2InvestRepay2.getActualInterest() - invest2InvestRepay2.getActualFee());
        when(investRepayMapper.findByInvestIdAndPeriod(invest2.getId(), loanRepay2.getPeriod())).thenReturn(invest2InvestRepay2);

        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn("READY");

        AccountModel investor1Account = new AccountModel(investor1LoginName, investor1LoginName, investor1LoginName, new Date());
        when(accountMapper.findByLoginName(invest1.getLoginName())).thenReturn(investor1Account);
        AccountModel investor2Account = new AccountModel(investor2LoginName, investor2LoginName, investor2LoginName, new Date());
        when(accountMapper.findByLoginName(invest2.getLoginName())).thenReturn(investor2Account);

        ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
        responseModel.setRetCode(BaseSyncResponseModel.SUCCESS_CODE);
        when(paySyncClient.send(eq(ProjectTransferMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class))).thenReturn(responseModel);

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        payDataDto.setStatus(true);

        when(loanService.updateLoanStatus(anyLong(), any(LoanStatus.class))).thenReturn(baseDto);

        assertTrue(normalRepayService.paybackInvest(loanRepay2.getId()));

        ArgumentCaptor<ProjectTransferRequestModel> requestModelArgumentCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);
        verify(paySyncClient, times(2)).send(eq(ProjectTransferMapper.class), requestModelArgumentCaptor.capture(), eq(ProjectTransferResponseModel.class));

    }

    @Test
    public void shouldPaybackLastPeriodWhenLoanIsRepaying() throws Exception {
        long loanId = 1;
        LoanRepayModel loanRepay1 = new LoanRepayModel(1, loanId, 1, 10, 10, new DateTime().minusDays(30).withTime(23, 59, 59, 0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(10);
        loanRepay1.setActualRepayDate(new DateTime().minusDays(30).toDate());
        LoanRepayModel loanRepay2 = new LoanRepayModel(2, loanId, 2, 30, 10, new DateTime().withTime(23, 59, 59, 0).toDate(), RepayStatus.COMPLETE);
        loanRepay2.setActualInterest(10);
        loanRepay2.setActualRepayDate(new DateTime().withMillisOfSecond(0).toDate());

        when(loanRepayMapper.findById(loanRepay2.getId())).thenReturn(loanRepay2);
        when(loanRepayMapper.findLastLoanRepay(loanId)).thenReturn(loanRepay2);

        String investor1LoginName = "investor1";
        String investor2LoginName = "investor2";
        InvestModel invest1 = new InvestModel(1, loanId, null, investor1LoginName, 10, 0.1, false, new Date(), Source.WEB, null);
        InvestModel invest2 = new InvestModel(2, loanId, null, investor2LoginName, 20, 0.1, false, new Date(), Source.WEB, null);
        List<InvestModel> successInvests = Lists.newArrayList(invest1, invest2);
        when(investMapper.findSuccessInvestsByLoanId(loanId)).thenReturn(successInvests);

        InvestRepayModel invest1InvestRepay2 = new InvestRepayModel(2, invest1.getId(), 1, 10, 3, 0, loanRepay2.getRepayDate(), RepayStatus.WAIT_PAY);
        invest1InvestRepay2.setActualInterest(invest1InvestRepay2.getExpectedInterest());
        invest1InvestRepay2.setActualFee(invest1InvestRepay2.getExpectedFee());
        invest1InvestRepay2.setActualRepayDate(loanRepay1.getActualRepayDate());
        invest1InvestRepay2.setRepayAmount(invest1InvestRepay2.getCorpus() + invest1InvestRepay2.getActualInterest() - invest1InvestRepay2.getActualFee());
        when(investRepayMapper.findByInvestIdAndPeriod(invest1.getId(), loanRepay2.getPeriod())).thenReturn(invest1InvestRepay2);

        InvestRepayModel invest2InvestRepay2 = new InvestRepayModel(2, invest2.getId(), 1, 20, 6, 1, loanRepay2.getRepayDate(), RepayStatus.WAIT_PAY);
        invest2InvestRepay2.setActualInterest(invest2InvestRepay2.getExpectedInterest());
        invest2InvestRepay2.setActualFee(invest2InvestRepay2.getExpectedFee());
        invest2InvestRepay2.setActualRepayDate(loanRepay1.getActualRepayDate());
        invest2InvestRepay2.setRepayAmount(invest2InvestRepay2.getCorpus() + invest2InvestRepay2.getActualInterest() - invest2InvestRepay2.getActualFee());
        when(investRepayMapper.findByInvestIdAndPeriod(invest2.getId(), loanRepay2.getPeriod())).thenReturn(invest2InvestRepay2);

        AccountModel investor1Account = new AccountModel(investor1LoginName, investor1LoginName, investor1LoginName, new Date());
        when(accountMapper.findByLoginName(invest1.getLoginName())).thenReturn(investor1Account);
        AccountModel investor2Account = new AccountModel(investor2LoginName, investor2LoginName, investor2LoginName, new Date());
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

        assertTrue(normalRepayService.paybackInvest(loanRepay2.getId()));

        ArgumentCaptor<ProjectTransferRequestModel> requestModelArgumentCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);
        verify(paySyncClient, times(3)).send(eq(ProjectTransferMapper.class), requestModelArgumentCaptor.capture(), eq(ProjectTransferResponseModel.class));
        List<ProjectTransferRequestModel> requestModels = requestModelArgumentCaptor.getAllValues();

        assertThat(requestModels.get(0).getProjectId(), is(String.valueOf(loanId)));
        assertThat(requestModels.get(0).getOrderId().split("X")[0], is(String.valueOf(invest1InvestRepay2.getId())));
        assertThat(requestModels.get(0).getUserId(), is(investor1Account.getPayUserId()));
        assertThat(requestModels.get(0).getAmount(), is(String.valueOf(invest1InvestRepay2.getCorpus() + invest1InvestRepay2.getExpectedInterest() - invest1InvestRepay2.getExpectedFee())));

        assertThat(requestModels.get(1).getProjectId(), is(String.valueOf(loanId)));
        assertThat(requestModels.get(1).getOrderId().split("X")[0], is(String.valueOf(invest2InvestRepay2.getId())));
        assertThat(requestModels.get(1).getUserId(), is(investor2Account.getPayUserId()));
        assertThat(requestModels.get(1).getAmount(), is(String.valueOf(invest2InvestRepay2.getCorpus() + invest2InvestRepay2.getExpectedInterest() - invest2InvestRepay2.getExpectedFee())));

        assertThat(requestModels.get(2).getProjectId(), is(String.valueOf(loanId)));
        assertThat(requestModels.get(2).getOrderId().split("X")[0], is(String.valueOf(loanRepay2.getId())));
        assertThat(requestModels.get(2).getUserId(), is("7099088"));
        assertThat(requestModels.get(2).getAmount(), is(String.valueOf(loanRepay2.getActualInterest()
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

    @Test
    public void shouldPaybackUpdateInvestRepayWhenLoanIsRepayingAndAmountIsZero() throws Exception {
        long loanId = 1;
        LoanRepayModel loanRepay1 = new LoanRepayModel(1, loanId, 1, 0, 0, new DateTime().withTime(23, 59, 59, 0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(0);
        loanRepay1.setActualRepayDate(new Date());
        LoanRepayModel loanRepay2 = new LoanRepayModel(2, loanId, 2, 1, 0, new DateTime().plusDays(30).withTime(23, 59, 59, 0).toDate(), RepayStatus.REPAYING);

        LoanModel loanModel = new LoanModel();
        loanModel.setName("loanName");
        doNothing().when(mqWrapperClient).sendMessage(any(MessageQueue.class), anyObject());
        when(loanMapper.findById(loanId)).thenReturn(loanModel);
        when(loanRepayMapper.findById(loanRepay1.getId())).thenReturn(loanRepay1);
        when(loanRepayMapper.findLastLoanRepay(loanId)).thenReturn(loanRepay2);

        String investor1LoginName = "investor1";
        InvestModel invest1 = new InvestModel(1, loanId, null, investor1LoginName, 1, 0.1, false, new Date(), Source.WEB, null);
        when(investMapper.findById(invest1.getId())).thenReturn(invest1);
        List<InvestModel> successInvests = Lists.newArrayList(invest1);
        when(investMapper.findSuccessInvestsByLoanId(loanId)).thenReturn(successInvests);
        when(couponRepayMapper.findCouponRepayByInvestIdAndPeriod(invest1.getId(), loanRepay2.getPeriod())).thenReturn(null);

        InvestRepayModel invest1InvestRepay1 = new InvestRepayModel(1, invest1.getId(), 1, 0, 0, 0, loanRepay1.getRepayDate(), RepayStatus.WAIT_PAY);
        invest1InvestRepay1.setActualInterest(invest1InvestRepay1.getExpectedInterest());
        invest1InvestRepay1.setActualFee(invest1InvestRepay1.getExpectedFee());
        invest1InvestRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        invest1InvestRepay1.setRepayAmount(invest1InvestRepay1.getActualInterest() - invest1InvestRepay1.getActualFee());
        when(investRepayMapper.findByInvestIdAndPeriod(invest1.getId(), loanRepay1.getPeriod())).thenReturn(invest1InvestRepay1);

        AccountModel investor1Account = new AccountModel(investor1LoginName, investor1LoginName, investor1LoginName, new Date());
        when(accountMapper.findByLoginName(invest1.getLoginName())).thenReturn(investor1Account);

        ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
        responseModel.setRetCode(BaseSyncResponseModel.SUCCESS_CODE);
        when(paySyncClient.send(eq(ProjectTransferMapper.class), any(BaseSyncRequestModel.class), eq(ProjectTransferResponseModel.class))).thenReturn(responseModel);

        doNothing().when(investRepayMapper).update(any(InvestRepayModel.class));

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        payDataDto.setStatus(true);
        when(loanService.updateLoanStatus(anyLong(), any(LoanStatus.class))).thenReturn(baseDto);

        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn("READY");

        assertTrue(normalRepayService.paybackInvest(loanRepay1.getId()));

        verify(paySyncClient, never()).send(eq(ProjectTransferMapper.class), any(BaseSyncRequestModel.class), eq(ProjectTransferResponseModel.class));

        ArgumentCaptor<InvestRepayModel> investRepayArgumentCaptor = ArgumentCaptor.forClass(InvestRepayModel.class);
        verify(investRepayMapper, times(1)).update(investRepayArgumentCaptor.capture());
        assertThat(investRepayArgumentCaptor.getValue().getActualInterest(), is(0L));
        assertThat(investRepayArgumentCaptor.getValue().getActualFee(), is(0L));
        assertThat(investRepayArgumentCaptor.getValue().getStatus(), is(RepayStatus.COMPLETE));

        ArgumentCaptor<Long> updateLoanIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<LoanStatus> loanStatusArgumentCaptor = ArgumentCaptor.forClass(LoanStatus.class);
        verify(loanService, times(1)).updateLoanStatus(updateLoanIdArgumentCaptor.capture(), loanStatusArgumentCaptor.capture());
        assertThat(updateLoanIdArgumentCaptor.getValue(), is(loanId));
        assertThat(loanStatusArgumentCaptor.getValue(), is(LoanStatus.REPAYING));

        ArgumentCaptor<String> redisKey1ArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisKey2ArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisValueArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, times(2)).hset(redisKey1ArgumentCaptor.capture(), redisKey2ArgumentCaptor.capture(), redisValueArgumentCaptor.capture());
    }

    @Test
    public void shouldPaybackLastPeriodWhenLoanIsRepayingAndRetryFeeTransfer() throws Exception {
        long loanId = 1;
        LoanRepayModel loanRepay1 = new LoanRepayModel(1, loanId, 1, 30, 10, new DateTime().withTime(23, 59, 59, 0).toDate(), RepayStatus.COMPLETE);
        loanRepay1.setActualInterest(10);
        loanRepay1.setActualRepayDate(new DateTime().withMillisOfSecond(0).toDate());

        when(loanRepayMapper.findById(loanRepay1.getId())).thenReturn(loanRepay1);
        when(loanRepayMapper.findLastLoanRepay(loanId)).thenReturn(loanRepay1);

        String investor1LoginName = "investor1";
        InvestModel invest1 = new InvestModel(1, loanId, null, investor1LoginName, 10, 0.1, false, new Date(), Source.WEB, null);
        List<InvestModel> successInvests = Lists.newArrayList(invest1);
        when(investMapper.findSuccessInvestsByLoanId(loanId)).thenReturn(successInvests);

        InvestRepayModel invest1InvestRepay1 = new InvestRepayModel(2, invest1.getId(), 1, 30, 10, 1, loanRepay1.getRepayDate(), RepayStatus.COMPLETE);
        invest1InvestRepay1.setActualInterest(invest1InvestRepay1.getExpectedInterest());
        invest1InvestRepay1.setActualFee(invest1InvestRepay1.getExpectedFee());
        invest1InvestRepay1.setActualRepayDate(loanRepay1.getActualRepayDate());
        invest1InvestRepay1.setRepayAmount(invest1InvestRepay1.getCorpus() + invest1InvestRepay1.getActualInterest() - invest1InvestRepay1.getActualFee());
        when(investRepayMapper.findByInvestIdAndPeriod(invest1.getId(), loanRepay1.getPeriod())).thenReturn(invest1InvestRepay1);


        AccountModel investor1Account = new AccountModel(investor1LoginName, investor1LoginName, investor1LoginName, new Date());
        when(accountMapper.findByLoginName(invest1.getLoginName())).thenReturn(investor1Account);

        ProjectTransferResponseModel responseModel = new ProjectTransferResponseModel();
        responseModel.setRetCode(BaseSyncResponseModel.SUCCESS_CODE);

        when(paySyncClient.send(eq(ProjectTransferMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class))).thenReturn(responseModel);

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        payDataDto.setStatus(true);

        when(loanService.updateLoanStatus(anyLong(), any(LoanStatus.class))).thenReturn(baseDto);

        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn("READY");

        assertTrue(normalRepayService.paybackInvest(loanRepay1.getId()));

        ArgumentCaptor<ProjectTransferRequestModel> requestModelArgumentCaptor = ArgumentCaptor.forClass(ProjectTransferRequestModel.class);
        verify(paySyncClient, times(1)).send(eq(ProjectTransferMapper.class), requestModelArgumentCaptor.capture(), eq(ProjectTransferResponseModel.class));
        ProjectTransferRequestModel requestModel = requestModelArgumentCaptor.getValue();
        assertThat(invest1InvestRepay1.getActualFee(), is(Long.parseLong(requestModel.getAmount())));

        ArgumentCaptor<Long> updateLoanIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<LoanStatus> loanStatusArgumentCaptor = ArgumentCaptor.forClass(LoanStatus.class);
        verify(loanService, times(1)).updateLoanStatus(updateLoanIdArgumentCaptor.capture(), loanStatusArgumentCaptor.capture());
        assertThat(updateLoanIdArgumentCaptor.getValue(), is(loanId));
        assertThat(loanStatusArgumentCaptor.getValue(), is(LoanStatus.COMPLETE));

        ArgumentCaptor<String> redisKey1ArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisKey2ArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> redisValueArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(redisWrapperClient, times(2)).hset(redisKey1ArgumentCaptor.capture(), redisKey2ArgumentCaptor.capture(), redisValueArgumentCaptor.capture());
    }
}
