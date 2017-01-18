package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.anxin.service.AnxinSignService;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.InvestSmsNotifyDto;
import com.tuotiansudai.job.AnxinCreateContractJob;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.LoanOutSuccessHandleJob;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerBindProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.MerUpdateProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerBindProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerUpdateProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerBindProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerUpdateProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.impl.LoanServiceImpl;
import com.tuotiansudai.quartz.TriggeredJobBuilder;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.SendCloudMailUtil;
import com.umpay.api.exception.ReqDataException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.SchedulerException;
import org.quartz.core.QuartzSchedulerResources;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
@Transactional
public class LoanServiceTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PaySyncClient paySyncClient;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private UMPayRealTimeStatusService umPayRealTimeStatusService;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    @Mock
    private JobManager jobManager;

    @Mock
    private RepayGeneratorService repayGeneratorService;

    @Mock
    private ReferrerRewardService referrerRewardService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SendCloudMailUtil sendCloudMailUtil;

    @Mock
    private SmsWrapperClient smsWrapperClient;

    @Mock
    private AnxinSignService anxinSignService;

    @Mock
    private MQWrapperClient mqWrapperClient;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateLoanIsSuccess() throws ReqDataException, PayException {
        LoanModel loanModel = this.fakeLoanModel();
        AccountModel accountModel = this.getFakeAccount();

        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);

        MerBindProjectResponseModel merBindProjectResponseModel = new MerBindProjectResponseModel();
        merBindProjectResponseModel.setRetCode("0000");
        MerUpdateProjectResponseModel merUpdateProjectResponseModel = new MerUpdateProjectResponseModel();
        merUpdateProjectResponseModel.setRetCode("0000");

        when(paySyncClient.send(eq(MerBindProjectMapper.class), any(MerBindProjectRequestModel.class), eq(MerBindProjectResponseModel.class))).thenReturn(merBindProjectResponseModel);
        when(paySyncClient.send(eq(MerUpdateProjectMapper.class), any(MerUpdateProjectRequestModel.class), eq(MerUpdateProjectResponseModel.class))).thenReturn(merUpdateProjectResponseModel);

        loanService.createLoan(fakeLoanModel().getId());

        ArgumentCaptor<MerBindProjectRequestModel> merBindProjectRequestModelArgumentCaptor = ArgumentCaptor.forClass(MerBindProjectRequestModel.class);
        ArgumentCaptor<MerUpdateProjectRequestModel> merUpdateProjectRequestModelArgumentCaptor = ArgumentCaptor.forClass(MerUpdateProjectRequestModel.class);
        verify(paySyncClient, times(1)).send(eq(MerBindProjectMapper.class), merBindProjectRequestModelArgumentCaptor.capture(), eq(MerBindProjectResponseModel.class));
        verify(paySyncClient, times(2)).send(eq(MerUpdateProjectMapper.class), merUpdateProjectRequestModelArgumentCaptor.capture(), eq(MerUpdateProjectResponseModel.class));

        MerBindProjectRequestModel merBindProjectRequestModelArgumentCaptorValue = merBindProjectRequestModelArgumentCaptor.getValue();
        assertThat(merBindProjectRequestModelArgumentCaptorValue.getLoanUserId(), is(accountModel.getPayUserId()));
        assertThat(merBindProjectRequestModelArgumentCaptorValue.getProjectAmount(), is(String.valueOf(loanModel.getLoanAmount())));
        assertThat(merBindProjectRequestModelArgumentCaptorValue.getProjectId(), is(String.valueOf(loanModel.getId())));
        assertThat(merBindProjectRequestModelArgumentCaptorValue.getProjectName(), is(String.valueOf(loanModel.getId())));

        List<MerUpdateProjectRequestModel> merUpdateProjectRequestModelArgumentCaptorAllValues = merUpdateProjectRequestModelArgumentCaptor.getAllValues();
        assertThat(merUpdateProjectRequestModelArgumentCaptorAllValues.get(0).getProjectId(), is(String.valueOf(loanModel.getId())));
        assertThat(merUpdateProjectRequestModelArgumentCaptorAllValues.get(0).getProjectState(), is("0"));

        assertThat(merUpdateProjectRequestModelArgumentCaptorAllValues.get(1).getProjectId(), is(String.valueOf(loanModel.getId())));
        assertThat(merUpdateProjectRequestModelArgumentCaptorAllValues.get(1).getProjectState(), is("1"));
    }


    protected AccountModel getFakeAccount() {
        return new AccountModel("loginName", "payUserId", "payAccountId", new Date());
    }

    private LoanModel fakeLoanModel() {
        LoanModel loanModel = new LoanModel();
        loanModel.setId(0);
        loanModel.setAgentLoginName("agent");
        loanModel.setLoanAmount(1);
        return loanModel;
    }

    private InvestModel getFakeInvestModel(long loanId, long investId, String loginName) {
        InvestModel model = new InvestModel(1, loanId, null, 1000000L, loginName, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null, 0.1);
        model.setStatus(InvestStatus.SUCCESS);
        return model;
    }


    @Test
    public void shouldLoanOutIdempotentIsOk() throws PayException, SchedulerException {
        String loginName = "testLoanOut";
        LoanModel loanModel = this.fakeLoanModel();
        loanModel.setStatus(LoanStatus.RECHECK);
        List<InvestModel> investModels = Lists.newArrayList(getFakeInvestModel(loanModel.getId(), 0, loginName));
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setStatus(true);
        baseDto.setData(payDataDto);
        ProjectTransferResponseModel projectTransferResponseModel = new ProjectTransferResponseModel();
        projectTransferResponseModel.setRetCode("0000");
        AccountModel accountModel = this.getFakeAccount();
        MerUpdateProjectResponseModel merUpdateProjectResponseModel = new MerUpdateProjectResponseModel();
        merUpdateProjectResponseModel.setRetCode("0000");
        QuartzSchedulerResources quartzSchedulerResources = new QuartzSchedulerResources();
        quartzSchedulerResources.setName("test");
        TriggeredJobBuilder triggeredJobBuilder = mock(TriggeredJobBuilder.class);


        when(triggeredJobBuilder.addJobData(anyObject(), anyLong())).thenReturn(triggeredJobBuilder);
        when(triggeredJobBuilder.withIdentity(anyString(), anyString())).thenReturn(triggeredJobBuilder);
        when(triggeredJobBuilder.replaceExistingJob(anyBoolean())).thenReturn(triggeredJobBuilder);
        when(triggeredJobBuilder.runOnceAt(any(Date.class))).thenReturn(triggeredJobBuilder);
        doNothing().when(triggeredJobBuilder).submit();
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(investModels);
        when(umPayRealTimeStatusService.checkLoanAmount(anyLong(), anyLong())).thenReturn(baseDto);
        when(paySyncClient.send(eq(ProjectTransferMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class))).thenReturn(projectTransferResponseModel);
        when(redisWrapperClient.setnx(anyString(), anyString())).thenReturn(true);
        when(redisWrapperClient.del(anyString())).thenReturn(true);
        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn("");
        when(redisWrapperClient.hset(anyString(), anyString(), anyString())).thenReturn(1l);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        when(paySyncClient.send(eq(MerUpdateProjectMapper.class), any(MerUpdateProjectRequestModel.class), eq(MerUpdateProjectResponseModel.class))).thenReturn(merUpdateProjectResponseModel);
        when(jobManager.newJob(any(JobType.class), eq(LoanOutSuccessHandleJob.class))).thenReturn(triggeredJobBuilder);
        doNothing().when(mqWrapperClient).sendMessage(any(MessageQueue.class), anyObject());

        BaseDto<PayDataDto> baseDto1 = loanService.loanOut(loanModel.getId());
        verify(paySyncClient, times(1)).send(eq(ProjectTransferMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class));
        verify(redisWrapperClient, times(1)).setnx(anyString(), anyString());
        verify(redisWrapperClient, times(2)).hset(anyString(), anyString(), anyString());
        verify(redisWrapperClient, times(2)).hget(anyString(), anyString());
        assertTrue(baseDto1.getData().getStatus());

        loanModel.setStatus(LoanStatus.RECHECK);
        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn(SyncRequestStatus.SUCCESS.name());
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        baseDto1 = loanService.loanOut(loanModel.getId());
        verify(paySyncClient, times(1)).send(eq(ProjectTransferMapper.class), any(ProjectTransferRequestModel.class), eq(ProjectTransferResponseModel.class));
        assertTrue(!baseDto1.getData().getStatus());
    }

    @Test
    public void shouldPostLoanOutIsOk() throws PayException, SchedulerException {
        UserModel userModel = getUserModelTest();
        LoanModel loanModel = getFakeLoan(userModel.getLoginName(), userModel.getLoginName(), LoanStatus.RAISING, ActivityType.NORMAL);
        InvestModel investModel = new InvestModel();
        TriggeredJobBuilder triggeredJobBuilder = mock(TriggeredJobBuilder.class);


        when(triggeredJobBuilder.addJobData(anyObject(), anyLong())).thenReturn(triggeredJobBuilder);
        when(triggeredJobBuilder.withIdentity(anyString(), anyString())).thenReturn(triggeredJobBuilder);
        when(triggeredJobBuilder.replaceExistingJob(anyBoolean())).thenReturn(triggeredJobBuilder);
        when(triggeredJobBuilder.runOnceAt(any(Date.class))).thenReturn(triggeredJobBuilder);
        doNothing().when(triggeredJobBuilder).submit();
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(investMapper.findSuccessInvestsByLoanId(anyLong())).thenReturn(Lists.newArrayList(investModel));
        doNothing().when(repayGeneratorService).generateRepay(anyLong());
        doNothing().when(referrerRewardService).rewardReferrer(any(LoanModel.class), anyList());
        when(userMapper.findByLoginName(anyString())).thenReturn(userModel);
        when(loanMapper.findById(anyLong())).thenReturn(loanModel);
        when(sendCloudMailUtil.sendMailByLoanOut(anyString(), anyMap())).thenReturn(true);
        when(smsWrapperClient.sendInvestNotify(any(InvestSmsNotifyDto.class))).thenReturn(new BaseDto<>());
        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn("");
        when(redisWrapperClient.hset(anyString(), anyString(), anyString())).thenReturn(1l);
        when(anxinSignService.createLoanContracts(anyLong())).thenReturn(new BaseDto());
        when(jobManager.newJob(any(JobType.class), eq(AnxinCreateContractJob.class))).thenReturn(triggeredJobBuilder);

        loanService.postLoanOut(loanModel.getId());
        verify(smsWrapperClient, times(1)).sendInvestNotify(any(InvestSmsNotifyDto.class));
        verify(sendCloudMailUtil, times(1)).sendMailByLoanOut(anyString(), anyMap());

        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn(SyncRequestStatus.SUCCESS.name());
        loanService.postLoanOut(loanModel.getId());
        verify(smsWrapperClient, times(1)).sendInvestNotify(any(InvestSmsNotifyDto.class));
        verify(sendCloudMailUtil, times(1)).sendMailByLoanOut(anyString(), anyMap());

    }

    public UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    private LoanModel getFakeLoan(String loanerLoginName, String agentLoginName, LoanStatus loanStatus, ActivityType activityType) {
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(111l);
        fakeLoanModel.setName("loanName");
        fakeLoanModel.setLoanerLoginName(loanerLoginName);
        fakeLoanModel.setLoanerUserName("借款人");
        fakeLoanModel.setLoanerIdentityNumber("111111111111111111");
        fakeLoanModel.setAgentLoginName(agentLoginName);
        fakeLoanModel.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        fakeLoanModel.setPeriods(3);
        fakeLoanModel.setStatus(loanStatus);
        fakeLoanModel.setActivityType(activityType);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setPledgeType(PledgeType.HOUSE);
        fakeLoanModel.setCreatedTime(new Date());
        return fakeLoanModel;
    }
}