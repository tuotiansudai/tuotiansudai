package com.tuotiansudai.service;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.jpush.client.MobileAppJPushClient;
import com.tuotiansudai.jpush.repository.mapper.JPushAlertMapper;
import com.tuotiansudai.jpush.repository.model.*;
import com.tuotiansudai.jpush.service.impl.JPushAlertServiceImpl;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class JPushAlertServiceTest {
    @InjectMocks
    private JPushAlertServiceImpl jPushAlertService;

    @Mock
    private InvestMapper investMapper;

    @Mock
    private InvestRepayMapper investRepayMapper;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private JPushAlertMapper jPushAlertMapper;

    @Mock
    private LoanRepayMapper loanRepayMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private MobileAppJPushClient mobileAppJPushClient;

    @Mock
    private RedisWrapperClient redisWrapperClient;

    private static final long loanId = 10000000001L;
    private static final long loanRepayId = 100000003L;
    private static final long investId = 3939399323434L;

    private static final long loanId2 = 20000000001L;
    private static final long loanRepayId2 = 200000003L;
    private static final long investId2 = 39393993235555L;

    private static final long loanId3 = 30000000001L;
    private static final long loanRepayId3 = 300000003L;
    private static final long investId3 = 49393993235555L;

    private static final String JPUSH_ID_KEY = "api:jpushId:store";

    private InvestRepayModel createInvestRepayHasDefaultInterest(long investId, RepayStatus repayStatus, int period) {
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(idGenerator.generate());
        investRepayModel.setInvestId(investId);
        investRepayModel.setStatus(repayStatus);
        investRepayModel.setCorpus(100);
        investRepayModel.setExpectedInterest(100);
        investRepayModel.setDefaultInterest(100);
        investRepayModel.setActualInterest(100);
        investRepayModel.setExpectedFee(0);
        investRepayModel.setActualFee(0);
        investRepayModel.setPeriod(period);
        investRepayModel.setRepayDate(new Date());
        investRepayModel.setActualRepayDate(new Date());
        investRepayModel.setCreatedTime(new Date());
        return investRepayModel;
    }

    private InvestRepayModel createInvestRepayNoDefaultInterest(long investId, RepayStatus repayStatus, int period) {
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setId(idGenerator.generate());
        investRepayModel.setInvestId(investId);
        investRepayModel.setStatus(repayStatus);
        investRepayModel.setCorpus(100);
        investRepayModel.setExpectedInterest(100);
        investRepayModel.setActualInterest(100);
        investRepayModel.setDefaultInterest(0);
        investRepayModel.setExpectedInterest(100);
        investRepayModel.setExpectedFee(100);
        investRepayModel.setActualFee(100);
        investRepayModel.setPeriod(period);
        investRepayModel.setRepayDate(new Date());
        investRepayModel.setActualRepayDate(new Date());
        investRepayModel.setCreatedTime(new Date());
        return investRepayModel;
    }

    private JPushAlertModel createJPushAlert() {
        JPushAlertModel jPushAlertModel = new JPushAlertModel();
        jPushAlertModel.setId(1005);
        jPushAlertModel.setName("用户资金变动推送-还款");
        jPushAlertModel.setPushType(PushType.REPAY_ALERT);
        jPushAlertModel.setPushSource(PushSource.ANDROID);
        jPushAlertModel.setContent("亲爱的天宝，您刚刚收到一笔{0}元的项目还款，请点击查看");
        jPushAlertModel.setIsAutomatic(true);
        jPushAlertModel.setCreatedTime(new Date());
        jPushAlertModel.setJumpTo(JumpTo.INVEST_RECEIVABLES);
        jPushAlertModel.setStatus(PushStatus.ENABLED);
        return jPushAlertModel;
    }

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        createJPushAlert();
    }

    private void publicMockMethod(long loanId1, int currentPeriod, String loginName, long investId, String registrationIds, InvestRepayModel investRepayModel) {
        LoanRepayModel loanRepayModel = new LoanRepayModel();
        loanRepayModel.setLoanId(loanId1);
        loanRepayModel.setPeriod(currentPeriod);
        when(loanRepayMapper.findById(anyLong())).thenReturn(loanRepayModel);

        List<InvestNotifyInfo> notifyInfos = new ArrayList<InvestNotifyInfo>();
        InvestNotifyInfo investNotifyInfo = new InvestNotifyInfo();
        investNotifyInfo.setInvestId(investId);
        investNotifyInfo.setLoginName(loginName);
        notifyInfos.add(investNotifyInfo);
        when(investMapper.findSuccessInvestMobileEmailAndAmount(anyLong())).thenReturn(notifyInfos);

        when(jPushAlertMapper.findJPushAlertByPushType(any(PushType.class))).thenReturn(createJPushAlert());

        when(investRepayMapper.findByInvestIdAndPeriod(anyInt(), anyInt())).thenReturn(investRepayModel);

        when(mobileAppJPushClient.sendPushAlertByRegistrationIds(anyString(), anyList(), anyString(), anyString(), anyString(), any(PushSource.class))).thenReturn(true);

        when(redisWrapperClient.hexists(JPUSH_ID_KEY, loginName)).thenReturn(true);
        when(redisWrapperClient.hget(anyString(), anyString())).thenReturn(registrationIds);
    }

    @Test
    public void shouldGetDefaultInterestWhenHasDefaultInterest() {
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();

        investRepayModels.add(createInvestRepayNoDefaultInterest(investId2, RepayStatus.COMPLETE, 1));
        investRepayModels.add(createInvestRepayHasDefaultInterest(investId, RepayStatus.OVERDUE, 2));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId2, RepayStatus.REPAYING, 1));

        publicMockMethod(loanId, 2, "testuser123", investId, "abdisierieruis123", investRepayModels.get(1));

        jPushAlertService.autoJPushRepayAlert(loanRepayId2, false);

        ArgumentCaptor argumentJPushAlertId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentAlert = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentextraKey = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentextraValue = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PushSource> argumentPushSource = ArgumentCaptor.forClass(PushSource.class);
        ArgumentCaptor<ArrayList<String>> argumentRegistrationIds = ArgumentCaptor.forClass((Class<ArrayList<String>>) new ArrayList<String>().getClass());

        verify(mobileAppJPushClient, times(1)).sendPushAlertByRegistrationIds((String) argumentJPushAlertId.capture(), argumentRegistrationIds.capture(), (String) argumentAlert.capture(), (String) argumentextraKey.capture(), (String) argumentextraValue.capture(), argumentPushSource.capture());

        assertEquals(String.valueOf(createJPushAlert().getId()), argumentJPushAlertId.getValue());
        assertEquals(createJPushAlert().getContent().replace("{0}", "2.00"), argumentAlert.getValue());
    }

    @Test
    public void shouldGetDefaultInterestWhenNoDefaultInterest() {
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId2, RepayStatus.COMPLETE, 1));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId2, RepayStatus.COMPLETE, 2));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId2, RepayStatus.REPAYING, 3));

        publicMockMethod(loanId2, 3, "testuser1234", investId2, "abdisierieruis1234", investRepayModels.get(2));

        jPushAlertService.autoJPushRepayAlert(loanRepayId2, false);

        ArgumentCaptor argumentJPushAlertId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentAlert = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentextraKey = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentextraValue = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PushSource> argumentPushSource = ArgumentCaptor.forClass(PushSource.class);
        ArgumentCaptor<ArrayList<String>> argumentRegistrationIds = ArgumentCaptor.forClass((Class<ArrayList<String>>) new ArrayList<String>().getClass());

        verify(mobileAppJPushClient, times(1)).sendPushAlertByRegistrationIds((String) argumentJPushAlertId.capture(), argumentRegistrationIds.capture(), (String) argumentAlert.capture(), (String) argumentextraKey.capture(), (String) argumentextraValue.capture(), argumentPushSource.capture());

        assertEquals(String.valueOf(createJPushAlert().getId()), argumentJPushAlertId.getValue());
        assertEquals(createJPushAlert().getContent().replace("{0}", "1.00"), argumentAlert.getValue());

    }

    @Test
    public void shouldGetDefaultInterestWhenGapDefaultInterest() {
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId3, RepayStatus.COMPLETE, 1));
        investRepayModels.add(createInvestRepayHasDefaultInterest(investId3, RepayStatus.OVERDUE, 2));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId3, RepayStatus.OVERDUE, 3));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId3, RepayStatus.REPAYING, 4));
        investRepayModels.add(createInvestRepayNoDefaultInterest(investId3, RepayStatus.REPAYING, 5));

        publicMockMethod(loanId3, 3, "testuser12345", investId3, "abdisierieruis12345", investRepayModels.get(2));

        jPushAlertService.autoJPushRepayAlert(loanRepayId3, false);

        ArgumentCaptor argumentJPushAlertId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentAlert = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentextraKey = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor argumentextraValue = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PushSource> argumentPushSource = ArgumentCaptor.forClass(PushSource.class);
        ArgumentCaptor<ArrayList<String>> argumentRegistrationIds = ArgumentCaptor.forClass((Class<ArrayList<String>>) new ArrayList<String>().getClass());

        verify(mobileAppJPushClient, times(1)).sendPushAlertByRegistrationIds((String) argumentJPushAlertId.capture(), argumentRegistrationIds.capture(), (String) argumentAlert.capture(), (String) argumentextraKey.capture(), (String) argumentextraValue.capture(), argumentPushSource.capture());

        assertEquals(String.valueOf(createJPushAlert().getId()), argumentJPushAlertId.getValue());
        assertEquals(createJPushAlert().getContent().replace("{0}", "1.00"), argumentAlert.getValue());
    }
}
